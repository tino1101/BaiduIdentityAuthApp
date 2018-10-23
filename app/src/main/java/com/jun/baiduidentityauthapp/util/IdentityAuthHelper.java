package com.jun.baiduidentityauthapp.util;

import android.util.Log;
import com.google.gson.Gson;
import com.jun.baiduidentityauthapp.model.FaceMatchResponse;
import com.jun.baiduidentityauthapp.model.FaceVerifyResponse;
import com.jun.baiduidentityauthapp.model.IDCardDetectionResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 身份认证(活体检测，身份证识别和校验)
 */
public class IdentityAuthHelper {

    public static final String API_KEY = "Your API KEY";
    public static final String SECRET_KEY = "Your SECRET KEY";

    private IdentityAuthService faceService;

    private static IdentityAuthHelper instance;

    public static synchronized IdentityAuthHelper getInstance() {
        if (instance == null) {
            instance = new IdentityAuthHelper();
        }
        return instance;
    }

    private IdentityAuthHelper() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("JunRetrofit", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        faceService = new Retrofit.Builder().client(new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://aip.baidubce.com/")
                .build().create(IdentityAuthService.class);
    }

    private <T> void getAccessToken(Function<AccessToken, Observable<T>> mapper, final CallBack callback) {
        faceService.getAccessToken().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .flatMap(mapper)
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        callback.onResponse(t);
                    }
                });
    }

    /**
     * 脸部识别活体认证
     */
    public void faceVerify(final String image, final CallBack callback) {
        getAccessToken(new Function<AccessToken, Observable<FaceVerifyResponse>>() {
            @Override
            public Observable<FaceVerifyResponse> apply(AccessToken accessToken) throws Exception {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("image", image);
                    jsonObject.put("image_type", "BASE64");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(jsonObject));
                return faceService.faceVerify(accessToken.getAccess_token(), body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        }, callback);
    }

    /**
     * 活体认证和身份证比对
     */
    public void match(final String livingImage, final String cardImage, final CallBack callback) {
        getAccessToken(new Function<AccessToken, Observable<FaceMatchResponse>>() {
            @Override
            public Observable<FaceMatchResponse> apply(AccessToken accessToken) throws Exception {
                List<Map<String, Object>> images = new ArrayList<>();
                Map<String, Object> image1 = new HashMap<>();
                image1.put("image", livingImage);
                image1.put("image_type", "BASE64");
                image1.put("face_type", "LIVE");
                Map<String, Object> image2 = new HashMap<>();
                image2.put("image", cardImage);
                image2.put("image_type", "BASE64");
                image2.put("face_type", "CERT");
                images.add(image1);
                images.add(image2);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(images));
                return faceService.match(accessToken.getAccess_token(), body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        }, callback);
    }

    /**
     * 获取身份证信息
     */
    public void idCard(final String cardImage, final CallBack callback) {
        getAccessToken(new Function<AccessToken, Observable<IDCardDetectionResponse>>() {
            @Override
            public Observable<IDCardDetectionResponse> apply(AccessToken accessToken) throws Exception {
                Map<String, Object> map = new HashMap<>();
                map.put("detect_direction", "true");
                map.put("id_card_side", "front");
                map.put("image", cardImage);
                return faceService.idCard(accessToken.getAccess_token(), map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        }, callback);
    }

    public class AccessToken {
        private String access_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }

    public interface IdentityAuthService {
        @GET("oauth/2.0/token?" + "grant_type=client_credentials" + "&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY)
        Observable<AccessToken> getAccessToken();

        @POST("rest/2.0/face/v3/faceverify")
        Observable<FaceVerifyResponse> faceVerify(@Query("access_token") String token, @Body RequestBody body);

        @POST("rest/2.0/face/v3/match")
        Observable<FaceMatchResponse> match(@Query("access_token") String token, @Body RequestBody body);

        @FormUrlEncoded
        @POST("rest/2.0/ocr/v1/idcard")
        Observable<IDCardDetectionResponse> idCard(@Query("access_token") String token, @FieldMap Map<String, Object> map);
    }

    public interface CallBack<T> {
        void onResponse(T t);
    }
}
package com.jun.baiduidentityauthapp.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.jun.baiduidentityauthapp.R;
import com.jun.baiduidentityauthapp.model.FaceVerifyResponse;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper;
import com.jun.baiduidentityauthapp.util.UiUtil;
import com.jun.baiduidentityauthapp.widget.CameraPreview;
import com.jun.baiduidentityauthapp.widget.FaceScanView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 活体检测
 */
public class LivingDetection2Activity extends Activity {

    private CameraPreview mCameraPreview;
    private FaceScanView faceScanView;
    private ImageView insideCircleImageView;
    private ImageView outsideCircleImageView;
    public static String livingImage = "";

    /**
     * 检测失败重试，最多三次
     */
    private int tryTime;

    private boolean isStop;
    private String picFileParent = "";
    /**
     * 截图输出文件
     */
    private File mPicFile;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mPicFile = null;
                    mCameraPreview.setOneShotPreviewCallback();
                    break;
                case 1:
                    toIDCardAuthActivity();
                    break;
                case 2:
                    toLivingBodyAuthFailureActivity();
                    break;
            }
        }
    };

    private void toIDCardAuthActivity() {
        Log.i("LivingDetection1", "跳转身份证识别");
        cancel();
        startActivity(new Intent(this, IDCardAuthActivity.class));
        finish();
    }

    private void toLivingBodyAuthFailureActivity() {
        Log.i("LivingDetection1", "跳转验证失败");
        cancel();
        startActivity(new Intent(this, LivingAuthFailureActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_living_detection);
        initViews();
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private void startRotation(View target, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", values);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(60000);
        animator.start();
    }

    private void initViews() {
        mCameraPreview = findViewById(R.id.camera);
        faceScanView = findViewById(R.id.faceView);
        insideCircleImageView = findViewById(R.id.inside_circle_image_view);
        outsideCircleImageView = findViewById(R.id.outside_circle_image_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCameraPreview.getLayoutParams();
        params.width = faceScanView.getViewWidth();
        params.height = faceScanView.getViewWidth();
        params.topMargin = UiUtil.dip2px(this, 100);
        mCameraPreview.setLayoutParams(params);
        insideCircleImageView.setLayoutParams(params);
        outsideCircleImageView.setLayoutParams(params);
        startRotation(insideCircleImageView, 0f, 360f);
        startRotation(outsideCircleImageView, 0f, -360f);
        mCameraPreview.setOnTakePicCallBack(new CameraPreview.OnTakePicCallBack() {
            @Override
            public void onPictureTaken(final byte[] bytes) {
                if (tryTime == 0) {
                    tryTime++;
                    return;
                }
                Observable.create(new ObservableOnSubscribe<File>() {
                    @Override
                    public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                        Log.i("LivingDetection1", "当前线程：" + Thread.currentThread().getName());
                        mPicFile = getOutputPicFile();
                        if (mPicFile == null) {
                            emitter.onNext(null);
                            emitter.onComplete();
                            return;
                        }
                        Camera.Size size = mCameraPreview.getPreviewSize(); //获取预览大小
                        if (null != size) {
                            final int w = size.width;  //宽度
                            final int h = size.height;
                            final YuvImage image = new YuvImage(bytes, ImageFormat.NV21, w, h, null);
                            ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length);
                            if (image.compressToJpeg(new Rect(0, 0, w, h), 100, os)) {
                                byte[] tmp = os.toByteArray();
                                byte2File(tmp);
                                livingImage = Base64.encodeToString(tmp, Base64.DEFAULT);
                            }
                        }
                        emitter.onNext(mPicFile);
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                if (null != mPicFile) faceVerify();
                            }
                        });
            }
        });
    }

    /**
     * 视频身份信息核验
     */
    private void faceVerify() {
        IdentityAuthHelper.getInstance().faceVerify(livingImage, new IdentityAuthHelper.CallBack<FaceVerifyResponse>() {
            @Override
            public void onResponse(FaceVerifyResponse faceVerifyResponse) {
                if (!isStop) {
                    if (null != faceVerifyResponse && faceVerifyResponse.getError_code() == 0 && null != faceVerifyResponse.getResult()
                            && faceVerifyResponse.getResult().getFace_liveness() > 0.393241) {
                        Log.i("LivingDetection1", "第" + tryTime + "次认证结果：成功");
                        mHandler.sendEmptyMessage(1);
                    } else {
                        Log.i("LivingDetection1", "第" + tryTime + "次认证结果：失败");
                        if (tryTime > 2) {
                            mHandler.sendEmptyMessage(2);
                        } else {
                            Log.i("LivingDetection1", "第" + (tryTime + 1) + "次尝试");
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                    tryTime++;
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mCameraPreview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraPreview.onPause();
        isStop = true;
    }

    private void cancel() {
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        mHandler.removeMessages(2);
    }

    @Override
    public void onDestroy() {
        mCameraPreview.releaseRes();
        super.onDestroy();
        cancel();
        Observable.just(picFileParent).subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String folder) throws Exception {
                if (!TextUtils.isEmpty(folder)) delFolder(folder);
            }
        });
    }

    public void byte2File(byte[] buf) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mPicFile);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File getOutputPicFile() {
        File parentFile = null;
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            parentFile = Environment.getExternalStorageDirectory();
        } else {
            parentFile = getApplicationContext().getCacheDir();
        }
        File mediaStorageDir = new File(parentFile, "YSQ/pic");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "PIC_" + timeStamp + ".png");
        if (null != mediaFile && TextUtils.isEmpty(picFileParent))
            picFileParent = mediaFile.getParent();
        return mediaFile;
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 /sdcard/data/
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            System.out.println(path + tempList[i]);
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]); // 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]); // 再删除空文件夹
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @return boolean
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }
}
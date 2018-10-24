package com.jun.baiduidentityauthapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.jun.baiduidentityauthapp.R;
import com.jun.baiduidentityauthapp.model.FaceMatchResponse;
import com.jun.baiduidentityauthapp.model.IDCardDetectionResponse;
import com.jun.baiduidentityauthapp.widget.CustomDialog;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper.CallBack;
import com.jun.baiduidentityauthapp.util.ToastUtil;
import com.jun.baiduidentityauthapp.widget.CameraPreview;

/**
 * 身份证检测识别
 */
public class IDCardDetectionActivity extends Activity implements OnClickListener, OnInfoListener {

    private CameraPreview mCameraPreview;
    private Button takePicButton;

    /**
     * 身份证照片
     */
    private String cardImage = "";
    private double matchScore = -1;
    private String name = "";
    private String sex = "";
    private String nation = "";
    private String birthday = "";
    private String address = "";
    private String number = "";
    private CustomDialog dialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.dismiss();
            Intent i = new Intent(IDCardDetectionActivity.this, IdentityAuthResultActivity.class);
            i.putExtra("name", name);
            i.putExtra("sex", sex);
            i.putExtra("nation", nation);
            i.putExtra("birthday", birthday);
            i.putExtra("address", address);
            i.putExtra("number", number);
            i.putExtra("matchScore", matchScore);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new CustomDialog(this, R.style.CustomDialog);
        setContentView(R.layout.activity_id_card_detection);
        initViews();
    }

    private void initViews() {
        mCameraPreview = findViewById(R.id.camera);
        mCameraPreview.setMaxDuration(10000);
        mCameraPreview.setOnInfoListener(this);
        mCameraPreview.setOnTakePicCallBack(new CameraPreview.OnTakePicCallBack() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                dialog.show();
                cardImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                cardDetect();
            }
        });
        takePicButton = findViewById(R.id.take_pic_button);
        takePicButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_pic_button:
                mCameraPreview.takePicture();
                break;
            default:
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            takePicButton.performClick();
        }
    }

    /**
     * 身份信息核验
     */
    private void cardDetect() {
        IdentityAuthHelper.getInstance().idCard(cardImage, new CallBack<IDCardDetectionResponse>() {

            @Override
            public void onResponse(IDCardDetectionResponse idCardDetectionResponse) {
                if (null != idCardDetectionResponse) {
                    if (null != idCardDetectionResponse.getWords_result() && idCardDetectionResponse.getWords_result_num() > 0) {
                        IDCardDetectionResponse.WordsResultBean resultBean = idCardDetectionResponse.getWords_result();
                        name = null != resultBean.get姓名() ? resultBean.get姓名().getWords() : "";
                        sex = null != resultBean.get性别() ? resultBean.get性别().getWords() : "";
                        nation = null != resultBean.get民族() ? resultBean.get民族().getWords() : "";
                        birthday = null != resultBean.get出生() ? resultBean.get出生().getWords() : "";
                        address = null != resultBean.get住址() ? resultBean.get住址().getWords() : "";
                        number = null != resultBean.get公民身份号码() ? resultBean.get公民身份号码().getWords() : "";
                        match();
                    } else {
                        dialog.dismiss();
                        ToastUtil.showToast(IDCardDetectionActivity.this, "未识别到身份证");
                    }
                } else {
                    dialog.dismiss();
                    ToastUtil.showToast(IDCardDetectionActivity.this, "身份证识别错误");
                }
            }
        });
    }

    private void match() {
        IdentityAuthHelper.getInstance().match(LivingDetectionActivity.livingImage, cardImage, new CallBack<FaceMatchResponse>() {

            @Override
            public void onResponse(FaceMatchResponse faceMatchResponse) {
                if (null != faceMatchResponse && faceMatchResponse.getError_code() == 0 && null != faceMatchResponse.getResult()) {
                    matchScore = faceMatchResponse.getResult().getScore();
                    mHandler.sendEmptyMessage(0);
                } else {
                    matchScore = -1;
                    if (null != faceMatchResponse && !TextUtils.isEmpty(faceMatchResponse.getError_msg()))
                        ToastUtil.showToast(IDCardDetectionActivity.this, faceMatchResponse.getError_msg());
                    mHandler.sendEmptyMessage(1);
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
    }

    @Override
    public void onDestroy() {
        mCameraPreview.releaseRes();
        super.onDestroy();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
}
package com.jun.baiduidentityauthapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.jun.baiduidentityauthapp.R;
import com.jun.baiduidentityauthapp.model.FaceVerifyResponse;
import com.jun.baiduidentityauthapp.util.UiUtil;
import com.jun.baiduidentityauthapp.widget.CustomDialog;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper.CallBack;
import com.jun.baiduidentityauthapp.widget.CameraPreview;

/**
 * 活体检测
 */
public class LivingDetectionActivity extends Activity implements OnClickListener, OnInfoListener {

    private CameraPreview mCameraPreview;
    private Button takePicButton;
    public static String livingImage = "";
    private CustomDialog dialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                dialog.dismiss();
                Intent i;
                if (msg.what == 0) {
                    i = new Intent(LivingDetectionActivity.this, IDCardAuthActivity.class);
                } else {
                    i = new Intent(LivingDetectionActivity.this, LivingAuthFailureActivity.class);
                }
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new CustomDialog(this, R.style.CustomDialog);
        setContentView(R.layout.activity_living_detection);
        initViews();
    }

    private void initViews() {
        mCameraPreview = findViewById(R.id.camera);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCameraPreview.getLayoutParams();
        params.width = UiUtil.getScreenWidth(this) * 64 / 100;
        params.height = UiUtil.getScreenWidth(this) * 64 / 100;
        params.topMargin = UiUtil.dip2px(this, 100);
        mCameraPreview.setLayoutParams(params);
        mCameraPreview.setMaxDuration(10000);
        mCameraPreview.setOnInfoListener(this);
        mCameraPreview.setOnTakePicCallBack(new CameraPreview.OnTakePicCallBack() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                dialog.show();
                livingImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                faceVerify();
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
     * 视频身份信息核验
     */
    private void faceVerify() {
        IdentityAuthHelper.getInstance().faceVerify(livingImage, new CallBack<FaceVerifyResponse>() {
            @Override
            public void onResponse(FaceVerifyResponse faceVerifyResponse) {
                if (null != faceVerifyResponse && faceVerifyResponse.getError_code() == 0 && null != faceVerifyResponse.getResult()
                        && faceVerifyResponse.getResult().getFace_liveness() > 0.393241) {
                    mHandler.sendEmptyMessage(0);
                } else {
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
}
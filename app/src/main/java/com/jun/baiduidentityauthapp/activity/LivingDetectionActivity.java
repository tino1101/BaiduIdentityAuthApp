package com.jun.baiduidentityauthapp.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.jun.baiduidentityauthapp.R;
import com.jun.baiduidentityauthapp.model.FaceVerifyResponse;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper.CallBack;
import com.jun.baiduidentityauthapp.util.UiUtil;
import com.jun.baiduidentityauthapp.widget.CameraPreview;
import com.jun.baiduidentityauthapp.widget.CustomDialog;
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
public class LivingDetectionActivity extends Activity implements OnClickListener {

    private CameraPreview mCameraPreview;
    private FaceScanView faceScanView;
    private ImageView insideCircleImageView;
    private ImageView outsideCircleImageView;
    //    private Button takePicButton;
    public static String livingImage = "";
    private CustomDialog dialog;


    /**
     * 录制视频输出文件
     */
    private File mVideoFile;
    /**
     * 截图输出文件
     */
    private File mPicFile;
    /**
     * 是否录制视频中
     */
    private boolean mIsShooting;
    /**
     * 检测失败重试，最多三次
     */
    private int tryTime;

    private boolean isStop;
    private String videoFileParent = "";
    private String picFileParent = "";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    clear();
                    mIsShooting = true;
                    try {
                        Log.i("LivingDetection", "开始第" + (tryTime + 1) + "次录像");
                        mVideoFile = mCameraPreview.startRecordVideo();
                        if (null != mVideoFile && TextUtils.isEmpty(videoFileParent))
                            videoFileParent = mVideoFile.getParent();
                        mHandler.sendEmptyMessageDelayed(1, 500);
                    } catch (Exception e) {
                        toLivingBodyAuthFailureActivity();
                    }
                    break;
                case 1:
                    mHandler.sendEmptyMessageDelayed(2, 500);
                    break;
                case 2:
                    if (mIsShooting) {
                        try {
                            Log.i("LivingDetection", "结束第" + (tryTime + 1) + "次录像");
                            stopVideoRecord();
                        } catch (Exception e) {
                            toLivingBodyAuthFailureActivity();
                        }
                    }
                    break;
                case 3:
                    toIDCardAuthActivity();
                    break;
                case 4:
                    toLivingBodyAuthFailureActivity();
                    break;
            }
        }
    };

    private void toIDCardAuthActivity() {
        Log.i("LivingDetection", "跳转身份证识别");
        cancel();
        startActivity(new Intent(this, IDCardAuthActivity.class));
        finish();
    }

    private void toLivingBodyAuthFailureActivity() {
        Log.i("LivingDetection", "跳转验证失败");
        cancel();
        startActivity(new Intent(this, LivingAuthFailureActivity.class));
        finish();
    }


//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            try {
//                dialog.dismiss();
//                Intent i;
//                if (msg.what == 0) {
//                    i = new Intent(LivingDetectionActivity.this, IDCardAuthActivity.class);
//                } else {
//                    i = new Intent(LivingDetectionActivity.this, LivingAuthFailureActivity.class);
//                }
//                startActivity(i);
//                finish();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new CustomDialog(this, R.style.CustomDialog);
        setContentView(R.layout.activity_living_detection);
        initViews();
        mHandler.sendEmptyMessageDelayed(0, 1000);
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
//        mCameraPreview.setOnTakePicCallBack(new CameraPreview.OnTakePicCallBack() {
//            @Override
//            public void onPictureTaken(byte[] bytes) {
//                dialog.show();
//                livingImage = Base64.encodeToString(bytes, Base64.DEFAULT);
//                faceVerify();
//            }
//        });
//        takePicButton = findViewById(R.id.take_pic_button);
//        takePicButton.setOnClickListener(this);
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

//    /**
//     * 视频身份信息核验
//     */
//    private void faceVerify() {
//        IdentityAuthHelper.getInstance().faceVerify(livingImage, new CallBack<FaceVerifyResponse>() {
//            @Override
//            public void onResponse(FaceVerifyResponse faceVerifyResponse) {
//                if (null != faceVerifyResponse && faceVerifyResponse.getError_code() == 0 && null != faceVerifyResponse.getResult()
//                        && faceVerifyResponse.getResult().getFace_liveness() > 0.393241) {
//                    mHandler.sendEmptyMessage(0);
//                } else {
//                    mHandler.sendEmptyMessage(1);
//                }
//            }
//        });
//    }

    /**
     * 视频身份信息核验
     */
    private void faceVerify() {
        IdentityAuthHelper.getInstance().faceVerify(livingImage, new CallBack<FaceVerifyResponse>() {
            @Override
            public void onResponse(FaceVerifyResponse faceVerifyResponse) {
                if (!isStop) {
                    if (null != faceVerifyResponse && faceVerifyResponse.getError_code() == 0 && null != faceVerifyResponse.getResult()
                            && faceVerifyResponse.getResult().getFace_liveness() > 0.393241) {
                        Log.i("LivingDetection", "第" + (tryTime + 1) + "次认证结果：成功");
                        mHandler.sendEmptyMessage(3);
                    } else {
                        Log.i("LivingDetection", "第" + (tryTime + 1) + "次认证结果：失败");
                        if (tryTime > 1) {
                            mHandler.sendEmptyMessage(4);
                        } else {
                            Log.i("LivingDetection", "第" + (tryTime + 2) + "次尝试");
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
        mHandler.removeMessages(3);
        mHandler.removeMessages(4);
    }

    private void clear() {
        mVideoFile = null;
        mPicFile = null;
        mIsShooting = false;
    }

    private void stopVideoRecord() {
        mCameraPreview.stopRecordCamera();
        mIsShooting = false;
        if (mVideoFile == null) {
            return;
        }
        mPicFile = getOutputPicFile();
        if (mPicFile == null) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFile.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(mPicFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fout);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                emitter.onNext(mPicFile);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        if (null != file) {
                            byte[] bytes = File2byte(file.getPath());
                            livingImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                            faceVerify();
                        }

                    }
                });
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

    @Override
    public void onDestroy() {
        mCameraPreview.releaseRes();
        super.onDestroy();
        cancel();
        Observable.just(videoFileParent, picFileParent).subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String folder) throws Exception {
                if (!TextUtils.isEmpty(folder)) delFolder(folder);
            }
        });
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

    public byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
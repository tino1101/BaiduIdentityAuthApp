package com.jun.baiduidentityauthapp.util;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    /**
     * 给定一个Bitmap，进行保存
     */
    public static void saveJpeg(Bitmap bm) {
        String savePath = "/mnt/sdcard/rectPhoto/";
        File folder = new File(savePath);
        if (!folder.exists()) {//如果文件夹不存在则创建
            folder.mkdir();
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = savePath + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            //			//如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800
            //			Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

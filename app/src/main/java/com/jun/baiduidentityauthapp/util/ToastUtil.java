package com.jun.baiduidentityauthapp.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil {
    public static void showToast(final Context context, final String message) {
        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

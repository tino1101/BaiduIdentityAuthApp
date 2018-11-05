package com.jun.baiduidentityauthapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class IdCardScanView extends View {
    private Context context;

    public IdCardScanView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public IdCardScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
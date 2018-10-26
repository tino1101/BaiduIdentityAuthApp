package com.jun.baiduidentityauthapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import com.jun.baiduidentityauthapp.util.UiUtil;

public class ShadeView extends View {
    private Path path;
    private Context context;
    private int radius;
    private int xOffset;
    private int yOffset;

    public ShadeView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ShadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        path = new Path();
        xOffset = UiUtil.getScreenWidth(context) / 2;
        radius = UiUtil.getScreenWidth(context) * 280 / 375 / 2;
        yOffset = radius + UiUtil.dip2px(context, 100);
        path.addCircle(xOffset, yOffset, radius, Path.Direction.CW);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawColor(Color.parseColor("#88000000"));
    }
}
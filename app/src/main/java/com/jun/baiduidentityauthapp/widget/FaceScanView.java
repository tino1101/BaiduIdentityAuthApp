package com.jun.baiduidentityauthapp.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.jun.baiduidentityauthapp.R;
import com.jun.baiduidentityauthapp.util.UiUtil;

public class FaceScanView extends View {
    private Path path;
    private Context context;
    private int radius;
    private int xOffset;
    private int yOffset;
    private Paint paint;

    public FaceScanView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FaceScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        xOffset = UiUtil.getScreenWidth(context) / 2;
        radius = UiUtil.getScreenWidth(context) * 260 / 375 / 2;
        yOffset = radius + getFinderMarginTop();
        path.addCircle(xOffset, yOffset, radius, Path.Direction.CW);
    }

    private int getFinderMarginTop() {
        return UiUtil.dip2px(context, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawColor(Color.BLACK);
        canvas.restore();

        Bitmap insideCircleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.auth_scan_inside_circle);
        Rect insideCircleRect = new Rect(UiUtil.getScreenWidth(context) * 57 / 375, getFinderMarginTop(),
                UiUtil.getScreenWidth(context) - UiUtil.getScreenWidth(context) * 57 / 375, getFinderMarginTop() + UiUtil.getScreenWidth(context) * 260 / 375);

        Bitmap circleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.auth_scan_circle);
        Rect circleRect = new Rect(UiUtil.getScreenWidth(context) * 57 / 375 - UiUtil.dip2px(context, 10), getFinderMarginTop() - UiUtil.dip2px(context, 10),
                UiUtil.getScreenWidth(context) - UiUtil.getScreenWidth(context) * 57 / 375 + UiUtil.dip2px(context, 10), getFinderMarginTop() + UiUtil.getScreenWidth(context) * 260 / 375 + UiUtil.dip2px(context, 10));

        canvas.drawBitmap(insideCircleBitmap, null, insideCircleRect, paint);
        canvas.drawBitmap(circleBitmap, null, circleRect, paint);
    }
}
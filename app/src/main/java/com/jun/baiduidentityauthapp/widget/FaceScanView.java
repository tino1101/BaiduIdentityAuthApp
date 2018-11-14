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
    private int viewWidth;

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
        viewWidth = UiUtil.getScreenWidth(context) * 260 / 375 + 2 * UiUtil.dip2px(context, 20);
        paint = new Paint();
        path = new Path();
        xOffset = viewWidth / 2;
        radius = UiUtil.getScreenWidth(context) * 260 / 375 / 2;
        yOffset = radius + UiUtil.dip2px(context, 20);
        path.addCircle(xOffset, yOffset, radius, Path.Direction.CW);
    }

    public int getViewWidth() {
        return viewWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = viewWidth;
        int heightSize = widthSize;
        int HEIGHT_MODE = MeasureSpec.getMode(heightMeasureSpec);
        int HEIGHT_SIZE = MeasureSpec.getSize(heightMeasureSpec);
        int WIDTH_MODE = MeasureSpec.getMode(widthMeasureSpec);
        int WIDTH_SIZE = MeasureSpec.getSize(widthMeasureSpec);
        if (HEIGHT_MODE == MeasureSpec.EXACTLY) {
            heightSize = HEIGHT_SIZE;
        } else if (HEIGHT_MODE == MeasureSpec.AT_MOST) {
            heightSize = Math.min(HEIGHT_SIZE, heightSize);
        }
        if (WIDTH_MODE == MeasureSpec.EXACTLY) {
            widthSize = WIDTH_SIZE;
        } else if (WIDTH_MODE == MeasureSpec.AT_MOST) {
            widthSize = Math.min(WIDTH_SIZE, widthSize);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawColor(Color.BLACK);
        canvas.restore();
        Bitmap insideCircleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.auth_scan_inside_circle);
        Rect insideCircleRect = new Rect(0, 0, viewWidth, viewWidth);
        Bitmap circleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.auth_scan_circle);
        Rect circleRect = new Rect(0, 0, viewWidth, viewWidth);
        canvas.drawBitmap(insideCircleBitmap, null, insideCircleRect, paint);
        canvas.drawBitmap(circleBitmap, null, circleRect, paint);
    }
}
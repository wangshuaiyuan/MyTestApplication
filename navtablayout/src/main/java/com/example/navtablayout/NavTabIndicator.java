package com.example.navtablayout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by wsy on 01/11/2018
 * tab指示器
 */
public class NavTabIndicator extends AbsTabIndicatorRender {

    private Paint mPaint = new Paint();

    public NavTabIndicator() {
        mPaint.setColor(Color.parseColor("#ff4081"));
    }

    /**
     * 渲染
     */
    public void draw(Canvas canvas) {
        mPaint.setColor(mIndicatorColor);
        int tabWidth = mTabItemWidth;
        if (tabWidth == 0) {
            return;
        }
        canvas.drawRect(mSelectedTabCenterX - mTabItemWidth / 2, mCanvasRect.top, mSelectedTabCenterX + mTabItemWidth / 2, mCanvasRect.bottom, mPaint);
    }
}

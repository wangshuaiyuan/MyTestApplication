package com.example.navtablayout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;

import com.example.navtablayout.core.AbsTabIndicatorRender;

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
    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Rect drawArea) {
        int height = drawArea.bottom - drawArea.top;
        int width = drawArea.right - drawArea.left;

        if (height <= getTopPadding() + getBottomPadding() || width <= getLeftPadding() + getRightPadding()) {
            return;
        }
        Rect realDrawArea = new Rect();
        realDrawArea.left = drawArea.left + getLeftPadding();
        realDrawArea.right = drawArea.right - getRightPadding();
        realDrawArea.top = drawArea.top + getTopPadding();
        realDrawArea.bottom = drawArea.bottom - getBottomPadding();
        canvas.drawRect(realDrawArea, mPaint);
    }
}

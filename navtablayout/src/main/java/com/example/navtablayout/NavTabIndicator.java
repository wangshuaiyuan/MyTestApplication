package com.example.navtablayout;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by wsy on 01/11/2018
 * tab指示器
 */
public class NavTabIndicator extends AbsNavTabIndicator{

    private Paint mPaint = new Paint();

    /**
     * 选中的指示器高度
     */
    private int mIndicatorHeight;

    /**
     * 指示器宽度
     */
    private int mIndicatorWidth;

    /**
     * 当前选中位置,从0开始
     */
    private float mSelectedIndex = 0;

    /**
     * 选中的indicator在x轴中心位置
     */
    private float mCenterInX = 0;

    /**
     * 总tab数
     */
    private int mTotalCount;

    /**
     * 整个indicator区域宽度
     */
    private int mAreaWidth;

    /**
     * 整个indicator区域长度
     */
    private int mAreaHeight;


    private int mActualIndicatorHeight;

    /**
     * 可渲染范围
     */
    private Rect mCanvasRect = new Rect();

    /**
     * 渲染
     */
    public void draw(Canvas canvas) {
        if (mIndicatorWidth <= 0 || mIndicatorHeight <= 0) {
            return;
        }
        if (mAreaWidth <= 0 || mAreaHeight <= 0) {
            return;
        }

        int divide = mAreaWidth / mTotalCount;
        int extra = (divide - mIndicatorWidth) / 2;

        int l = (int) ((divide) * mSelectedIndex + extra);
        int r = (int) ((divide + 1) * mSelectedIndex - extra);
        canvas.drawRect(l, mCanvasRect.top, r, mCanvasRect.top + mActualIndicatorHeight, mPaint);
    }


    /**
     * 更新范围
     *
     * @param l left 左
     * @param t top 顶部
     * @param r right 右侧
     * @param b bottom 底部
     */
    protected void updateDisplayRange(int l, int t, int r, int b) {
        mCanvasRect.set(l, t, r, b);
        mAreaWidth = mCanvasRect.right - mCanvasRect.left;
        mAreaHeight = mCanvasRect.bottom - mCanvasRect.top;
        mActualIndicatorHeight = Math.min(mIndicatorHeight, mAreaHeight);
    }

    public void setSelectedIndex(int index) {
        mSelectedIndex = index;
    }

    public void setIndicatorWidth(int width) {
        mIndicatorWidth = width;
    }

    public void setIndicatorHeight(int height) {
        mIndicatorHeight = height;
    }

    public void setTotalCount(int count) {
        mTotalCount = count;
    }
}

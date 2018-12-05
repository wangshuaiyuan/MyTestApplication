package com.example.navtablayout.core;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by wsy on 02/11/2018
 * 指示器渲染器
 */
public abstract class AbsTabIndicatorRender {


    /**
     * 高度
     */
    private int mHeight = 100;
    /**
     *
     */
    private int mTopPadding, mBottomPadding, mLeftPadding, mRightPadding;
    /**
     * 指示器颜色
     */
    @ColorInt
    protected int mIndicatorColor;

    /**
     * 设置
     *
     * @param color
     */
    public void setIndicatorColor(@ColorInt int color) {
        mIndicatorColor = color;
    }

    /**
     * 获取高度
     */
    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }


    /**
     * 渲染,leftLimit,rightLimit,height,
     */
    public abstract void draw(@NonNull Canvas canvas, @NonNull Rect drawArea);

    /**
     * 停止动画
     */
    protected void stopAnimtor() {

    }

    public int getTopPadding() {
        return mTopPadding;
    }

    public void setTopPadding(int mTopPadding) {
        this.mTopPadding = mTopPadding;
    }

    public int getBottomPadding() {
        return mBottomPadding;
    }

    public void setBottomPadding(int mBottomPadding) {
        this.mBottomPadding = mBottomPadding;
    }

    public int getLeftPadding() {
        return mLeftPadding;
    }

    public void setLeftPadding(int mLeftPadding) {
        this.mLeftPadding = mLeftPadding;
    }

    public int getRightPadding() {
        return mRightPadding;
    }

    public void setRightPadding(int mRightPadding) {
        this.mRightPadding = mRightPadding;
    }
}

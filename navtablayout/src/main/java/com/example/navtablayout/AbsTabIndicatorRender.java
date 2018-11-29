package com.example.navtablayout;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

/**
 * Created by wsy on 02/11/2018
 */
public abstract class AbsTabIndicatorRender {

    /**
     * 已选中position
     */
    protected float mSelectedTabCenterX = 200;

    /**
     * 标签条目宽度,起参照作用
     */
    protected int mTabItemWidth = 100;

    private int mHeight = 100;

    private int mWidth = 100;

    /**
     * 指示器颜色
     */
    @ColorInt
    protected int mIndicatorColor;


    /**
     * 设置
     * @param color
     */
    public void setIndicatorColor(@ColorInt int color){
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

    public int getWidth() {
        return mWidth;
    }


    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * 可渲染范围
     */
    protected Rect mCanvasRect = new Rect();


    /**
     * 渲染
     */
    abstract void draw(Canvas canvas);

    /**
     * 更新渲染区域
     */
    void updateDisplayArea(int l, int t, int r, int b) {
        mCanvasRect.set(l, t, r, b);
    }

    /**
     * 更新选中索引
     */
    void updateSelCenterX(float centerX) {
        mSelectedTabCenterX = centerX;
    }

    /**
     * 标签条目宽度
     */
    void setTabItemWidth(int tabItemWidth) {
        mTabItemWidth = tabItemWidth;
    }

    void setSelectedTabCenterX(float selectedTabCenterX) {
        mSelectedTabCenterX = selectedTabCenterX;
    }

    /**
     * 滚动至某个位置
     * TODO
     *
     * @param position 目标位置
     * @param duration 时间
     */
    void animtorToPosition(int position, int duration) {

    }
}

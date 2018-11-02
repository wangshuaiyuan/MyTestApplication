package com.example.navtablayout;

import android.support.design.widget.TabLayout;

import com.example.navtablayout.NavTabLayoutConstant;

/**
 * Created by wsy on 29/10/2018
 * 配置类
 */
public class NavTabConfiguration {

    /**
     * tab浮动方向
     */
    private int mTabGravity = NavTabLayoutConstant.GRAVITY_CENTER;

    /**
     * 指示器颜色
     */
    private int mSelectedIndicatorTabColor;

    /**
     * 指示器高度
     */
    private int mSelectedIndicatorHeight;

    /**
     * 指示器位置
     */
    private int mIndicatorAlign;

    /**
     * 指示器宽度
     */
    private int mIndicatorWidth;

    /**
     * 模式
     */
    private int mMode = NavTabLayoutConstant.MODE_SCROLLABLE;


    public int getmTabGravity() {
        return mTabGravity;
    }

    public void setmTabGravity(int mTabGravity) {
        this.mTabGravity = mTabGravity;
    }

    public int getmSelectedIndicatorTabColor() {
        return mSelectedIndicatorTabColor;
    }

    public void setmSelectedIndicatorTabColor(int mSelectedIndicatorTabColor) {
        this.mSelectedIndicatorTabColor = mSelectedIndicatorTabColor;
    }

    public int getmSelectedIndicatorHeight() {
        return mSelectedIndicatorHeight;
    }

    public void setmSelectedIndicatorHeight(int mSelectedIndicatorHeight) {
        this.mSelectedIndicatorHeight = mSelectedIndicatorHeight;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }

    public int getmIndicatorAlign() {
        return mIndicatorAlign;
    }

    public void setmIndicatorAlign(int mIndicatorAlign) {
        this.mIndicatorAlign = mIndicatorAlign;
    }

    public int getmIndicatorWidth() {
        return mIndicatorWidth;
    }

    public void setmIndicatorWidth(int mIndicatorWidth) {
        this.mIndicatorWidth = mIndicatorWidth;
    }
}

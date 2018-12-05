package com.example.navtablayout.core.observer;

/**
 * Created by wsy on 04/12/2018
 */
public interface ScrollChangeObserver {

    /**
     * 页面滚动事件
     *
     * @param position             滚动索引
     * @param positionOffset       滚动偏移量 范围[0，1）
     * @param positionOffsetPixels 滚动像素偏移量
     */
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    /**
     * 页面被选中事件
     *
     * @param position 选中索引
     */
    void onPageSelected(int position);
}

package com.example.navtablayout.core;

/**
 * tab选中或非选中事件
 */
public interface OnTabSelectedListener {
    /**
     * 选中tab
     *
     * @param position tab位置
     */
    void onTabSelect(int position);

    /**
     * 反选tab
     *
     * @param position tab位置
     */
    void onTabUnSelect(int position);
}

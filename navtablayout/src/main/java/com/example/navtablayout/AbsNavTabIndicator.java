package com.example.navtablayout;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by wsy on 02/11/2018
 */
public abstract class AbsNavTabIndicator {

    /**
     * 已选中index
     */
    protected float mSelectedIndex;

    /**
     * 可渲染范围
     */
    private Rect mCanvasRect = new Rect();

    /**
     * 渲染
     */
    abstract void draw(Canvas canvas);

    /**
     * 更新渲染区域
     */
    public void updateDisplayArea(int l, int t, int r, int b) {
        mCanvasRect.set(l, t, r, b);
    }

    /**
     * 更新选中索引
     */
    public void upDateSelectedIndex(float selectedIndex) {
        mSelectedIndex = selectedIndex;
    }
}

package com.example.navtablayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.navtablayout.Adapter.IAdapterDataObserver;
import com.example.navtablayout.Adapter.AbsTabAdapter;

/**
 * Created by wsy on 22/10/2018
 */
public class SlidingTabStrip extends LinearLayout {

    /**
     * 滚动动画
     */
    private ValueAnimator mIndicatorAnimator;

    /**
     * 配置类
     */
    private Option mOption;

    public SlidingTabStrip(Context context) {
        super(context);
        setWillNotDraw(false);
        setOrientation(HORIZONTAL);
    }

    /**
     * 设置配置项
     */
    public void setOption(@NonNull Option option) {
        mOption = option;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mOption.isShowIndicator()) {
            if (mOption.getIndicatorAlign() == Option.INDICATOR_ALIGN_TOP) {
                scrollTo(0, -mOption.getIndicatorRender().getHeight());
            }
            mOption.getIndicatorRender().draw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //等待下次测量
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            return;
        }

        if (mOption.getMode() == Option.MODE_FIXED && mOption.getGravity() == Option.GRAVITY_CENTER) {
            final int count = getChildCount();

            int largestTabWidth = 0;
            for (int i = 0, z = count; i < z; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    largestTabWidth = Math.max(largestTabWidth, child.getMeasuredWidth());
                }
            }

            if (largestTabWidth <= 0) {
                return;
            }

            boolean remeasure = false;
            if (largestTabWidth * count <= getMeasuredWidth()) {
                for (int i = 0; i < count; i++) {
                    final LinearLayout.LayoutParams lp =
                            (LayoutParams) getChildAt(i).getLayoutParams();
                    if (lp.width != largestTabWidth || lp.weight != 0) {
                        lp.width = largestTabWidth;
                        lp.weight = 0;
                        remeasure = true;
                    }
                }
            } else {
                mOption.setGravity(Option.GRAVITY_FILL);
                remeasure = true;
            }

            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }

        }
    }

    /**
     * child是否需要layout
     *
     * @return true，子view还未layout，需要layout
     */
    boolean isChildNeedLayout() {
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final View child = getChildAt(i);
            if (child.getWidth() <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 动画滚动到指定位置
     *
     * @param position 目标索引
     * @param duration 时间
     */
    void animateIndicatorToPosition(int position, int duration) {
        //TODO

    }
}

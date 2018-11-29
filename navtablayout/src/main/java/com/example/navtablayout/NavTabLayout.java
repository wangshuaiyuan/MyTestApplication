package com.example.navtablayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.example.navtablayout.Adapter.AbsTabAdapter;
import com.example.navtablayout.Adapter.IAdapterDataObserver;

import java.util.TreeMap;

/**
 * Created by wsy on 22/10/2018
 * 通用导航栏
 */
public class NavTabLayout extends HorizontalScrollView implements IAdapterDataObserver {
    /**
     * 动画时间
     */
    private final int ANIMATION_DURATION = 300;
    /**
     * 不合法位置
     */
    public final int INVALID_POSITION = -1;

    private SlidingTabStrip mTabStrip;

    /**
     * 默认option
     */
    private Option mOption = new Option.Builder().build();

    private ValueAnimator mScrollAnimator;
    private AbsTabAdapter mAdapter;

    /**
     * 选中位置，default 0
     */
    private int mSelectedPosition;

    /**
     * 指示器
     */
    private AbsTabIndicatorRender mIndicator = new NavTabIndicator();

    public NavTabLayout(Context context) {
        this(context, null);
    }

    public NavTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mIndicator = new NavTabIndicator();
        mTabStrip = new SlidingTabStrip(getContext());
        mTabStrip.setOption(mOption);
        super.addView(mTabStrip, 0, new HorizontalScrollView.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 计算滚动目标位置
     */
    private int calculateTabScrollX(int position, float positionOffset) {
        if (position >= mTabStrip.getChildCount()) {
            return 0;
        }
        if (mOption.getMode() == Option.MODE_SCROLLABLE) {
            final View selectedChild = mTabStrip.getChildAt(position);
            if (selectedChild == null) {
                return 0;
            }
            final View nextChild = position + 1 < mTabStrip.getChildCount()
                    ? mTabStrip.getChildAt(position + 1)
                    : null;
            final int selectedWidth = selectedChild.getWidth();
            final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;

            //选中的tab置于屏幕中间
            int scrollBase = selectedChild.getLeft() + (selectedWidth / 2) - (getWidth() / 2);
            int scrollOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);

            return (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                    ? scrollBase + scrollOffset
                    : scrollBase - scrollOffset;
        }
        return 0;
    }

    /**
     * 刷新tab内容view
     */
    private void updateTabs() {
        mTabStrip.removeAllViews();
        for (int i = 0, j = mAdapter.getItemCount(); i < j; i++) {
            View itemView = mAdapter.getItemView(mTabStrip, i, mSelectedPosition == i);
            mTabStrip.addView(itemView);
        }
    }

    /**
     * 计算指示器终点位置
     */
    private int calculateSelectedTabCenterX(int position, float positionOffset) {
        final View selectedChild = mTabStrip.getChildAt(position);
        if (selectedChild == null) {
            return 0;
        }
        final View nextChild = position + 1 < mTabStrip.getChildCount()
                ? mTabStrip.getChildAt(position + 1)
                : null;
        final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
        final int selectedWidth = selectedChild.getWidth();
        int centerXBase = selectedChild.getLeft() + selectedWidth / 2;
        int centerOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);
        return (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                ? centerXBase + centerOffset
                : centerXBase - centerOffset;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int idealHeight = NavTabUtil.dpToPx(getContext(), getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY);
                break;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 1) {
            final View child = getChildAt(0);
            boolean remeasure = false;

            switch (mOption.getMode()) {
                case Option.MODE_SCROLLABLE:
                    remeasure = child.getMeasuredWidth() < getMeasuredWidth();
                    break;
                case Option.MODE_FIXED:
                    remeasure = child.getMeasuredWidth() != getMeasuredWidth();
                    break;
            }

            if (remeasure) {
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    /**
     * 动画检查赋值
     */
    private void ensureScrollAnimator() {
        if (mScrollAnimator == null) {
            mScrollAnimator = new ValueAnimator();
            mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            mScrollAnimator.setDuration(ANIMATION_DURATION);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    scrollTo((int) animator.getAnimatedValue(), 0);
                }
            });
        }
    }

    /**
     * 动画滚动到某个位置
     *
     * @param newPosition 新位置
     */
    private void animatToPosition(int newPosition) {
        if (newPosition == INVALID_POSITION) {
            return;
        }
        if (getWindowToken() != null || mTabStrip.isChildNeedLayout()) {
            scrollToPosition(newPosition, 0, true);
        }

        ensureScrollAnimator();
        final int startScrollX = getScrollX();
        final int targetScrollX = calculateTabScrollX(newPosition, 0);

        if (startScrollX != targetScrollX) {
            ensureScrollAnimator();
            mScrollAnimator.setIntValues(startScrollX, targetScrollX);
            mScrollAnimator.start();
        }
        mTabStrip.animateIndicatorToPosition(newPosition, ANIMATION_DURATION);
    }

    /**
     * 获取默认高度
     */
    private int getDefaultHeight() {

        if (mTabStrip == null || mTabStrip.getChildCount() == 0) {
            return 0;
        }

        int maxHeight = 0;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View childView = mTabStrip.getChildAt(i);
            maxHeight = childView.getMeasuredHeight();
        }

        //展示指示器
        if (mOption.isShowIndicator()) {
            int height = mOption.getIndicatorRender().getHeight();
            maxHeight += height;
        }

        return maxHeight;
    }

    /**
     * 设置适配器
     */
    public void setAdapter(@NonNull AbsTabAdapter adapter) {
        mAdapter = adapter;
        mAdapter.registerDataObserver(this);
        updateTabs();
    }

    /**
     * 设置滚动位置
     */
    public void scrollToPosition(int position, float positionOffset, boolean updateIndicatorPosition) {
        final int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition < 0 || roundedPosition >= mTabStrip.getChildCount()) {
            return;
        }

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }
        if (updateIndicatorPosition) {
            mIndicator.setSelectedTabCenterX(calculateSelectedTabCenterX(position, positionOffset));
        }
        scrollTo(calculateTabScrollX(position, positionOffset), 0);
        ViewCompat.postInvalidateOnAnimation(mTabStrip);
    }

    /**
     * 设置配置类
     *
     * @param option 配置类对象
     */
    public void setOption(@NonNull Option option) {
        mOption = option;
        mTabStrip.setOption(option);
    }

    /**
     * 设置指示器颜色
     *
     * @param color 色值
     */
    public void setIndicatorColor(@ColorInt int color) {
        mOption.getIndicatorRender().setIndicatorColor(color);
    }

    @Override
    public void onChanged() {
        updateTabs();
    }
}

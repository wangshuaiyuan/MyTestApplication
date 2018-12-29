package com.example.navtablayout.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.example.navtablayout.core.Adapter.AbsTabAdapter;
import com.example.navtablayout.core.Adapter.IAdapterDataObserver;
import com.example.navtablayout.core.observer.ScrollChangeObserver;

/**
 * Created by wsy on 22/10/2018
 * 通用导航栏
 */
public class NavTabLayout extends HorizontalScrollView implements IAdapterDataObserver, ScrollChangeObserver {
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
        mTabStrip = new SlidingTabStrip(getContext());
        mTabStrip.setOption(mOption);
        super.addView(mTabStrip, 0, new HorizontalScrollView.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 计算滚动目标位置
     */
    private int calculateTabScrollX(int position, float positionOffset) {
        if (mOption.getMode() != Option.MODE_SCROLLABLE || position >= mTabStrip.getChildCount()) {
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
        adjustSelectedPosition();
    }

    /**
     * 调整选中位置
     */
    private void adjustSelectedPosition() {
        if (mSelectedPosition > mAdapter.getItemCount()) {
            mSelectedPosition = mAdapter.getItemCount() - 1;
        }
    }

    /**
     * 刷新选中view
     *
     * @param position 位置
     * @param itemView view
     */
    private void refreshSelectItem(int position, View itemView) {
        if (mAdapter != null) {
            mAdapter.updateSelectPosition(itemView, position);
        }
    }

    /**
     * 刷新取消选中view
     *
     * @param position 位置
     * @param itemView view
     */
    private void refreshUnSelectItem(int position, View itemView) {
        if (mAdapter != null) {
            mAdapter.updateUnSelectPosition(itemView, position);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int idealHeight = getDefaultHeight() + getPaddingTop() + getPaddingBottom();
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
    public void animatToPosition(final int newPosition) {
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
            mScrollAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onItemSelected(newPosition, mSelectedPosition);
                    mSelectedPosition = newPosition;
                }
            });
        }

        mTabStrip.animateIndicatorToPosition(newPosition, ANIMATION_DURATION);
    }

    private void onItemSelected(int newSelectPosition, int unSelectPosition) {
        refreshSelectItem(newSelectPosition, mTabStrip.getChildAt(newSelectPosition));
        refreshUnSelectItem(unSelectPosition, mTabStrip.getChildAt(unSelectPosition));
        mSelectedPosition = newSelectPosition;
    }

    /**
     * 获取默认高度
     */
    private int getDefaultHeight() {
        if (mTabStrip == null || mTabStrip.getChildCount() == 0) {
            return 0;
        }


        mTabStrip.requestLayout();
        int maxHeight = 0;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View childView = mTabStrip.getChildAt(i);
            int childeHeigt = childView.getMeasuredHeight();
            Log.e("-----", childeHeigt + "");
            maxHeight = Math.max(maxHeight, childeHeigt);
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
        scrollToPosition(mSelectedPosition, 0, true);
    }

    /**
     * 设置滚动位置
     */
    protected void scrollToPosition(int position, float positionOffset, boolean updateIndicatorPosition) {
        final int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition < 0 || roundedPosition >= mTabStrip.getChildCount()) {
            return;
        }


        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }
        if (updateIndicatorPosition) {
            mTabStrip.setSelectedPosition(position, positionOffset);
        }
        int destinationX = calculateTabScrollX(position, positionOffset);
        scrollTo(destinationX, 0);
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
        if (getWindowToken() != null) {
            requestLayout();
        }
    }

    @Override
    public void onChanged() {
        updateTabs();
    }

    /**
     * 滚动位置
     */
    public float getScrollPosition() {
        return mSelectedPosition;
    }

    /**
     * 设置选中位置
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        scrollToPosition(position, 0, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        scrollToPosition(position, positionOffset, true);
    }

    @Override
    public void onPageSelected(int position) {
        onItemSelected(position, mSelectedPosition);
    }
}

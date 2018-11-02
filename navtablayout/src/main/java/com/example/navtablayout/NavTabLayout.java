package com.example.navtablayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by wsy on 22/10/2018
 * 通用导航栏
 */
public class NavTabLayout extends HorizontalScrollView {

    private SlidingTabStrip mTabStrip;

    private NavTabConfiguration mConfiguration;

    private ObjectAnimator mScrollAnimator;

    private NavTabAdapter mAdapter;

    /**
     * 指示器
     */
    private NavTabIndicator mIndicator;


    public void setNavTabIndicator(){

    }


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

    private void init() {
        mConfiguration = new NavTabConfiguration();
        mAdapter = new NavTabAdapter();
        mTabStrip = new SlidingTabStrip(getContext());
        mScrollAnimator = new ObjectAnimator();
        mIndicator = new NavTabIndicator();
    }


    void setScrollPosition(int position, float positionOffset, boolean updateSelectedText,
                           boolean updateIndicatorPosition) {
        final int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition < 0 || roundedPosition >= mTabStrip.getChildCount()) {
            return;
        }

        if (updateIndicatorPosition) {
            mTabStrip.setIndicatorPositionFromTabPosition(position, positionOffset);
        }

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }
        scrollTo(calculateScrollXForTab(position, positionOffset), 0);

        if (updateSelectedText) {
            setSelectedTabView(roundedPosition);
        }
    }


    private void setSelectedTabView(int position) {
        final int tabCount = mTabStrip.getChildCount();
        if (position < tabCount) {
            for (int i = 0; i < tabCount; i++) {
                final View child = mTabStrip.getChildAt(i);
                child.setSelected(i == position);
            }
        }
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        if (mConfiguration.getmMode() == NavTabLayoutConstant.MODE_SCROLLABLE) {
            final View selectedChild = mTabStrip.getChildAt(position);
            final View nextChild = position + 1 < mTabStrip.getChildCount()
                    ? mTabStrip.getChildAt(position + 1)
                    : null;
            final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
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
     * 设置适配器
     */
    public void setAdapter(NavTabAdapter adapter) {
        mAdapter = adapter;
        mTabStrip.setAdapter(adapter);

//        mIndicator.updateDisplayRange(0, 0, 0, 0);
        mIndicator.setTotalCount(mAdapter.getItemCount());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //height
        final int idealHeight = NavTabUtil.dpToPx(getContext(), getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)),
                        MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY);
                break;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 1) {
            final View child = getChildAt(0);
            boolean remeasure = false;

            switch (mConfiguration.getmMode()) {
                case NavTabLayoutConstant.MODE_SCROLLABLE:
                    remeasure = child.getMeasuredWidth() < getMeasuredWidth();
                    break;
                case NavTabLayoutConstant.MODE_FIXED:
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

    private int getDefaultHeight() {
        if (mTabStrip == null || mTabStrip.getChildCount() == 0) {
            return 0;
        }
        int maxHeight = 0, maxWidth = 0;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View childView = mTabStrip.getChildAt(i);
            maxHeight = childView.getMeasuredHeight();
            maxWidth = childView.getMeasuredWidth();
        }
        return maxHeight;
    }

    /**
     * 指示器宽高
     *
     * @param width  宽
     * @param height 高
     */
    public void setIndicatorSize(int width, int height) {
        mIndicator.setIndicatorHeight(height);
        mIndicator.setIndicatorWidth(width);
    }

}

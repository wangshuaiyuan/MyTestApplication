package com.example.navtablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wsy on 22/10/2018
 */
public class SlidingTabStrip extends LinearLayout implements ITabStrip {
    /**
     * dps
     */
    static final int FIXED_WRAP_GUTTER_MIN = 16;

    static final int MOTION_NON_ADJACENT_OFFSET = 24;

    private int mTabGravity;

    private NavTabConfiguration mConfiguration;

    private NavTabIndicator mIndicator;

    private int mIndicatorLeft = -1;

    private int mIndicatorRight = -1;

    private float mSelectionOffset;

    private NavTabAdapter mAdapter;
    /**
     * 滚动动画
     */
    private ValueAnimator mIndicatorAnimator;

    /**
     * 当前选中位置
     */
    private int mSelectedPosition = 0;

    public SlidingTabStrip(Context context) {
        super(context);
        mIndicator = new NavTabIndicator();
        mConfiguration = new NavTabConfiguration();
    }

    /**
     * 设置adapter
     * @param adapter 适配器
     */
    public void setAdapter(NavTabAdapter adapter) {
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                refreshView();
            }
        });
        mAdapter = adapter;
        refreshView();
    }

    //TODO 选中view样式
    private void refreshView() {
        //清除所有view
        removeAllViews();
        for (int i = 0, j = mAdapter.getItemCount(); i < j; i++) {
            RecyclerView.ViewHolder viewHolder = mAdapter.createViewHolder(this, mAdapter.getItemViewType(i));
            addView(viewHolder.itemView);
            mAdapter.bindViewHolder(viewHolder, i);
        }
    }

    void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
        if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
            mIndicatorAnimator.cancel();
        }

        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        refreshIndicator();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mIndicator.draw(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //等待下次测量
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            return;
        }

        if (mConfiguration.getmMode() == NavTabLayoutConstant.MODE_FIXED && mTabGravity == NavTabLayoutConstant.GRAVITY_CENTER) {
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

            final int gutter = NavTabUtil.dpToPx(getContext(), FIXED_WRAP_GUTTER_MIN);
            boolean remeasure = false;

            if (largestTabWidth * count <= getMeasuredWidth() - gutter * 2) {
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
                mTabGravity = NavTabLayoutConstant.GRAVITY_FILL;
                remeasure = true;
            }

            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
            mIndicatorAnimator.cancel();
            final long duration = mIndicatorAnimator.getDuration();
            animateIndicatorToPosition(mSelectedPosition, Math.round((1f - mIndicatorAnimator.getAnimatedFraction()) * duration));
        } else {
            refreshIndicator();
        }
    }

    void refreshIndicator() {
        ViewCompat.postInvalidateOnAnimation(this);
    }

    void setIndicatorPosition(int left, int right) {
        if (left != mIndicatorLeft || right != mIndicatorRight) {
            mIndicatorLeft = left;
            mIndicatorRight = right;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void animateIndicatorToPosition(final int position, int duration) {
        if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
            mIndicatorAnimator.cancel();
        }

        final View targetView = getChildAt(position);
        if (targetView == null) {
            refreshIndicator();
            return;
        }
        final boolean isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
        final int targetLeft = targetView.getLeft();
        final int targetRight = targetView.getRight();
        final int startLeft;
        final int startRight;

        if (Math.abs(position - mSelectedPosition) <= 1) {
            startLeft = mIndicatorLeft;
            startRight = mIndicatorRight;
        } else {
            final int offset = NavTabUtil.dpToPx(getContext(), MOTION_NON_ADJACENT_OFFSET);
            if (position < mSelectedPosition) {
                if (isRtl) {
                    startLeft = startRight = targetLeft - offset;
                } else {
                    startLeft = startRight = targetRight + offset;
                }
            } else {
                if (isRtl) {
                    startLeft = startRight = targetRight + offset;
                } else {
                    startLeft = startRight = targetLeft - offset;
                }
            }
        }

        if (startLeft != targetLeft || startRight != targetRight) {
            ValueAnimator animator = mIndicatorAnimator = new ValueAnimator();
            animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(duration);
            animator.setFloatValues(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    final float fraction = animator.getAnimatedFraction();
                    setIndicatorPosition(
                            AnimationUtils.lerp(startLeft, targetLeft, fraction),
                            AnimationUtils.lerp(startRight, targetRight, fraction));
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    mSelectedPosition = position;
                    mSelectionOffset = 0f;
                }
            });
            animator.start();
        }
    }


    @Override
    public void setMode(int mode) {
        mConfiguration.setmMode(mode);
    }

    @Override
    public void setNavGravity(int gravity) {
        mTabGravity = gravity;
    }

}

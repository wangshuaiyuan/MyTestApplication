package com.example.navtablayout.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wsy on 22/10/2018
 */
public class SlidingTabStrip extends LinearLayout {

    /**
     * 滚动动画
     */
    private ValueAnimator mIndicatorAnimator;

    /**
     * 指示器在画布中x轴的位置
     */
    private int mIndicatorLeft = -1, mIndicatorRight = -1;


    private int mSelectedPosition;

    private float mSelectionOffset;

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
     * 刷新指示器边界
     *
     * @param left  左侧
     * @param right 右侧
     */
    private void refreshIndicatorBorder(int left, int right) {
        if (mIndicatorLeft != left || mIndicatorRight != right) {
            mIndicatorLeft = left;
            mIndicatorRight = right;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void setSelectedPosition(int position, float offset) {
        mSelectedPosition = position;
        mSelectionOffset = offset;
        updateSelectPosition();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateSelectPosition();
    }

    /**
     * 刷新选择状态
     */
    private void updateSelectPosition() {
        final View selectedTitle = getChildAt(mSelectedPosition);
        int left, right;

        if (selectedTitle != null && selectedTitle.getWidth() > 0) {
            left = selectedTitle.getLeft();
            right = selectedTitle.getRight();

            if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }
        } else {
            left = right = -1;
        }
        refreshIndicatorBorder(left, right);
    }

    /**
     * 设置配置项
     */
    public void setOption(@NonNull Option option) {
        mOption = option;
        applyModeAndGravity();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mOption.isShowIndicator() && mOption.getIndicatorRender().getHeight() > 0 && mIndicatorLeft >= 0 && mIndicatorRight >= 0) {
            if (mOption.getIndicatorAlign() == Option.INDICATOR_ALIGN_TOP) {
                scrollTo(0, -mOption.getIndicatorRender().getHeight());
                canvas.save();
                canvas.translate(0, -mOption.getIndicatorRender().getHeight());
            }
            mOption.getIndicatorRender().draw(canvas, buildIndicatorDrawArea());
            if (mOption.getIndicatorAlign() == Option.INDICATOR_ALIGN_TOP) {
                canvas.restore();
            }
        }
    }

    /**
     * 构造指示器展示区域
     *
     * @return 展示区域
     */
    private Rect buildIndicatorDrawArea() {
        Rect rect = new Rect();
        rect.left = mIndicatorLeft;
        rect.right = mIndicatorRight;
        if (mOption.getIndicatorAlign() == Option.INDICATOR_ALIGN_TOP) {
            rect.top = 0;
            rect.bottom = mOption.getIndicatorRender().getHeight();
        } else {

            rect.bottom = getBottom();
            rect.top = rect.bottom - mOption.getIndicatorRender().getHeight();
        }
        return rect;
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
            for (int i = 0; i < count; i++) {
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


    private void applyModeAndGravity() {
        if (mOption.getMode() == Option.MODE_FIXED) {
            setGravity(Gravity.CENTER_HORIZONTAL);
        } else if (mOption.getMode() == Option.MODE_SCROLLABLE) {
            setGravity(GravityCompat.START);
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
    void animateIndicatorToPosition(final int position, int duration) {
        if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
            mIndicatorAnimator.cancel();
        }

        final View targetView = getChildAt(position);
        if (targetView == null) {
            updateSelectPosition();
            return;
        }

        final int targetLeft = targetView.getLeft();
        final int targetRight = targetView.getRight();
        final int startLeft;
        final int startRight;

        if (Math.abs(position - mSelectedPosition) <= 1) {
            startLeft = mIndicatorLeft;
            startRight = mIndicatorRight;
        } else {
            if (position < mSelectedPosition) {
                startLeft = startRight = targetRight;
            } else {
                startLeft = startRight = targetLeft;
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
                    refreshIndicatorBorder(AnimationUtils.lerp(startLeft, targetLeft, fraction),
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
}

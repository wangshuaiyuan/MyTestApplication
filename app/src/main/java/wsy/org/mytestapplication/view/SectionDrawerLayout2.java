package wsy.org.mytestapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by wsy on 02/07/2018
 */

public class SectionDrawerLayout2 extends FrameLayout implements NestedScrollingParent2 {


    private List<Integer> mUsableAnchorsPosition = new ArrayList<Integer>() {{
        add(0);
        add(700);
    }};

    /**
     * 未操作处理前getTop的值，{@see #getTop()}  {@see #getTranslationY()}
     */
    private float mInitialTop = -1;

    private int mTouchSlop;

    private NestedScrollingParentHelper mParentHelper;

    private boolean isHandleAnchor;
    private OverScroller mScroller = new OverScroller(getContext());

    public SectionDrawerLayout2(@NonNull Context context) {
        this(context, null);
    }

    public SectionDrawerLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionDrawerLayout2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    private List<Anchor> mAnchors = new ArrayList<>();

    /**
     * 设置锚点
     *
     * @param anchors 锚点集合
     */
    public void setAnchor(List<Anchor> anchors) {
        isHandleAnchor = true;
        mAnchors.clear();
        mAnchors.addAll(anchors);
        requestLayout();
    }

    private void handleAnchor() {
        mUsableAnchorsPosition.clear();
        int defaultAnchor = 0;
        //锚点计算和排序
        int measuredHeight = getMeasuredHeight();
        if (mAnchors.size() == 0) {
            addDefaultAnchorOffset(measuredHeight);
        } else {
            for (Anchor anchor : mAnchors) {
                int anchorOffset = anchor.calculateAnchorPosition(measuredHeight);
                if (anchorOffset > 0 && anchorOffset < measuredHeight && !mUsableAnchorsPosition.contains(anchorOffset)) {
                    mUsableAnchorsPosition.add(anchorOffset);
                }
                if (defaultAnchor == 0 && anchor.isDefaultAnchor) {
                    defaultAnchor = anchorOffset;
                }
            }
            Collections.sort(mUsableAnchorsPosition, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            addDefaultAnchorOffset(measuredHeight);


        }
        setTranslationY(defaultAnchor);
        isHandleAnchor = false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mInitialTop == -1) {
            mInitialTop = getTranslationY();
        }
        if (isHandleAnchor) {
            handleAnchor();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void addDefaultAnchorOffset(int measuredHeight) {
        mUsableAnchorsPosition.add(0, 0);
        mUsableAnchorsPosition.add(mUsableAnchorsPosition.size() - 1, measuredHeight);
    }


    /**
     * 是否正在拖拽
     */
    private boolean isBeingDragged;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!inChild((int) ev.getX(), (int) y)) {
                    isBeingDragged = false;
                    break;
                }
                mLastMotionY = y;
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastMotionY;
                boolean inChild = inChild((int) ev.getX(), (int) ev.getY());
                if (inChild && dy > mTouchSlop && (getNestedScrollAxes() & ViewCompat.SCROLL_AXIS_VERTICAL) == 0) {
                    isBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isBeingDragged = false;
                break;
        }
        return isBeingDragged;
    }

    /**
     * 记录touch事件上次位置
     */
    private float mLastMotionY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastMotionY;

                boolean inChild = inChild((int) ev.getX(), (int) ev.getY());
                if (inChild && !isBeingDragged && Math.abs(dy) > mTouchSlop) {
                    isBeingDragged = true;
                }
                if (isBeingDragged) {
                    scrollToInBounds(getScrollY(), (int) -dy);
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                isBeingDragged = false;
                customFling();
                return true;
            case MotionEvent.ACTION_CANCEL:
                isBeingDragged = false;
                customFling();
                break;
        }
        return true;
    }

    private int mLastScrollerY;

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int deltaY = mScroller.getCurrY() - mLastScrollerY;
            scrollToInBounds(mLastScrollerY, deltaY);
            ViewCompat.postInvalidateOnAnimation(this);
            mLastScrollerY = mScroller.getCurrY();
        } else {
            mLastScrollerY = 0;
        }
    }

    /**
     * 在边界内scroll
     */
    private void scrollToInBounds(int startY, int deltaY) {
        int desY;
        int range = getDrawerRange();
        if (startY + deltaY > 0) {
            desY = 0;
        } else if (startY + deltaY < -range) {
            desY = -getDrawerRange();
        } else {
            desY = startY + deltaY;
        }
        scrollTo(getScrollX(), desY);

    }

    /**
     * 自动滚动
     */
    private void customFling() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        int curScrollY = getScrollY();
        int nearestAnchor = -getNearestAnchorByY(-curScrollY);
        mScroller.startScroll(0, getScrollY(), 0, nearestAnchor - curScrollY, 500);
        mLastScrollerY = curScrollY;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 是否在子view所在范围
     */
    private boolean inChild(int x, int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            final int scrollY = getScrollY();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                if (!(y < child.getTop() - scrollY
                        || y >= child.getBottom() - scrollY
                        || x < child.getLeft()
                        || x >= child.getRight())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return 获取拖拽范围
     */
    private int getDrawerRange() {
        return mUsableAnchorsPosition.get(mUsableAnchorsPosition.size() - 1);
    }

    /**
     * 获取当前滚动位置的锚点
     *
     * @param scrollY 滚动距离
     */
    protected int getNearestAnchorByY(float scrollY) {
        TreeMap<Float, Integer> deltaDistance = new TreeMap<>();
        //查找最近锚点
        for (int i = 0, size = mUsableAnchorsPosition.size(); i < size; i++) {
            int anchor = mUsableAnchorsPosition.get(i);
            deltaDistance.put(Math.abs(anchor - scrollY), i);
        }

        Iterator iterator = deltaDistance.keySet().iterator();
        if (iterator.hasNext()) {
            return mUsableAnchorsPosition.get(deltaDistance.get(iterator.next()));
        }
        return 0;
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mParentHelper.onStopNestedScroll(target);
        customFling();
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            scrollToInBounds(getScrollY(), dyUnconsumed);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    /**
     * 插值器
     */
    public class EaseQuadOutInterpolator implements Interpolator {

        public float getInterpolation(float input) {
            return -input * (input - 2);
        }


    }


    /**
     * 锚点
     */
    public static abstract class Anchor {

        public Anchor(int offset, boolean isDefaultAnchor) {
            this.offset = offset;
            this.isDefaultAnchor = isDefaultAnchor;
        }

        /**
         * 偏移量
         */
        public int offset;

        public boolean isDefaultAnchor;

        /**
         * l
         * 计算锚点位置
         */
        abstract int calculateAnchorPosition(int measuredHeight);
    }


    /**
     * 从顶部为基准的锚点
     */
    public static class TopAnchor extends Anchor {

        public TopAnchor(int offset, boolean isDefaultAnchor) {
            super(offset, isDefaultAnchor);
        }

        @Override
        int calculateAnchorPosition(int measuredHeight) {
            return offset;
        }
    }

    /**
     * 已底部为基准计算的锚点
     */
    public static class BottomAnchor extends Anchor {

        public BottomAnchor(int offset, boolean isDefultAnchor) {
            super(offset, isDefultAnchor);
        }

        @Override
        int calculateAnchorPosition(int measuredHeight) {
            return measuredHeight - offset;
        }
    }
}

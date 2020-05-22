package wsy.org.mytestapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wsy on 13/06/2018
 * 具有分段自动吸入的layout
 */
public class SectionDrawerLayout extends FrameLayout implements NestedScrollingParent2 {


    private VelocityTracker mVelocityTracker;

    private List<Integer> mUsableAnchorsPosition = new ArrayList<Integer>() {{
        add(0);
        add(700);
    }};

    /**
     * 动画是否在执行中
     */
    private boolean isAnimating = false;

    /**
     * 未操作处理前getTop的值，{@see #getTop()}  {@see #getTranslationY()}
     */
    private float mInitialTop = -1;

    private int mTouchSlop;

    private NestedScrollingParentHelper mParentHelper;

    /**
     * TODO default false
     */
    private boolean isHandleAnchor = true;
    private EdgeEffect mEdgeGlowTop;
    private EdgeEffect mEdgeGlowBottom;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mLastScrollerY;

    public SectionDrawerLayout(@NonNull Context context) {
        this(context, null);
    }

    public SectionDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
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
            //TODO
//            handleAnchor();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void addDefaultAnchorOffset(int measuredHeight) {
        mUsableAnchorsPosition.add(0, 0);
        mUsableAnchorsPosition.add(mUsableAnchorsPosition.size() - 1, measuredHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //忽略touch事件
        if (isAnimating) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int y = mScroller.getCurrY();
            int dy = y - mLastScrollerY;
//            Log.e("----scroll dy", dy + "");
//
//            if (dy != 0) {
//                final int range = getScrollRange();
//                final int oldScrollY = getScrollY();
//                Log.e("----diaoyong","2222");
//                boolean isEdge = overScrollByCompat(0, dy, getScrollX(), oldScrollY, 0, range, 0, 0, false);
//                if (isEdge) {
//                    endDrag();
//                }
//            }

            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

//            mLastScrollerY = y;
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            mLastScrollerY = 0;
        }
    }

    /**
     * 是否正在拖拽
     */
    private boolean isDragging = false;
//    private int mNestedYOffset = 0;


    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean inChild(int x, int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            final int scrollY = getScrollY();
            final View child = getChildAt(0);
            for (int i = 0; i < childCount; i++) {
                return !(y < child.getTop() - scrollY
                        || y >= child.getBottom() - scrollY
                        || x < child.getLeft()
                        || x >= child.getRight());
            }
        }
        return false;
    }

    /**
     * 记录touch事件上次位置
     */
    private float mLastMotionY;

    private OverScroller mScroller = new OverScroller(getContext());


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int actionMasked = ev.getActionMasked();
        final int y = (int) ev.getY();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if (!inChild((int) ev.getX(), y)) {
                    isDragging = false;
                    recycleVelocityTracker();
                    break;
                }
                mLastMotionY = y;
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                mScroller.computeScrollOffset();
                isDragging = !mScroller.isFinished();
                break;
            case MotionEvent.ACTION_MOVE:
                int diffY = (int) Math.abs(mLastMotionY - y);
                if (diffY > mTouchSlop && (getNestedScrollAxes() & ViewCompat.SCROLL_AXIS_VERTICAL) == 0) {
                    isDragging = true;
                    mLastMotionY = y;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    return true;
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                recycleVelocityTracker();
                if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;

        }
        return isDragging;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        final int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();

                if (isDragging = !mScroller.isFinished()) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastMotionY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) event.getY();
                int deltaY = (int) (mLastMotionY - y);
                Log.e("-----", deltaY + "");

                if (!isDragging && Math.abs(deltaY) > mTouchSlop) {
                    isDragging = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (isDragging) {
                    mLastMotionY = y;
                    final int oldY = getScrollY();
                    final int range = getScrollRange();
                    final int overScrollMode = getOverScrollMode();
//                    boolean canOverScroll = overScrollMode == View.OVER_SCROLL_ALWAYS
//                            || (overScrollMode == View.OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                    Log.e("----diaoyong","111");
                    if (overScrollByCompat(0, deltaY, 0, getScrollY(), 0, range, 0,
                            0, true)) {
                        mVelocityTracker.clear();
                    }

//                    if (canOverScroll) {
//                        ensureGlows();
//                        final int pulledToY = oldY + deltaY;
//                        if (pulledToY < 0) {
//                            EdgeEffectCompat.onPull(mEdgeGlowTop, (float) deltaY / getHeight(), event.getX() / getWidth());
//                            if (!mEdgeGlowBottom.isFinished()) {
//                                mEdgeGlowBottom.onRelease();
//                            }
//                        } else if (pulledToY > range) {
//                            EdgeEffectCompat.onPull(mEdgeGlowBottom, (float) deltaY / getHeight(), 1.f - event.getX() / getWidth());
//                            if (!mEdgeGlowTop.isFinished()) {
//                                mEdgeGlowTop.onRelease();
//                            }
//                        }
//                        if (mEdgeGlowTop != null
//                                && (!mEdgeGlowTop.isFinished() || !mEdgeGlowBottom.isFinished())) {
//                            ViewCompat.postInvalidateOnAnimation(this);
//                        }
//                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    mScroller.fling(getScrollX(), getScrollY(),
                            0, -initialVelocity,
                            0, 0,
                            Integer.MIN_VALUE, Integer.MAX_VALUE,
                            0, 0);
                    mLastScrollerY = getScrollY();
                    ViewCompat.postInvalidateOnAnimation(this);
                } else if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0,
                        getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isDragging && getChildCount() > 0) {
                    if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0,
                            getScrollRange())) {
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                endDrag();
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(event);
        }

        return true;
    }

    private void endDrag() {
        isDragging = false;
        recycleVelocityTracker();
        if (mEdgeGlowTop != null) {
            mEdgeGlowTop.onRelease();
            mEdgeGlowBottom.onRelease();
        }
    }


    private void ensureGlows() {
        if (getOverScrollMode() != View.OVER_SCROLL_NEVER) {
            if (mEdgeGlowTop == null) {
                Context context = getContext();
                mEdgeGlowTop = new EdgeEffect(context);
                mEdgeGlowBottom = new EdgeEffect(context);
            }
        } else {
            mEdgeGlowTop = null;
            mEdgeGlowBottom = null;
        }
    }

    /**
     * @return 获取拖拽范围
     */
    private int getDrawerRange() {
        return mUsableAnchorsPosition.get(mUsableAnchorsPosition.size() - 1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //重新过滤锚点
    }


    boolean overScrollByCompat(int deltaX, int deltaY,
                               int scrollX, int scrollY,
                               int scrollRangeX, int scrollRangeY,
                               int maxOverScrollX, int maxOverScrollY,
                               boolean isTouchEvent) {

        int newScrollY = scrollY + deltaY;
        boolean flag = false;
        if (newScrollY < -scrollRangeY) {
            newScrollY = -scrollRangeY;
            flag = true;
        } else if (newScrollY > 0) {
            newScrollY = 0;
            flag = true;
        }

        Log.e("----over", deltaY + "");
//        if (flag) {
//            mScroller.springBack(0, newScrollY, 0, 0, 0, getScrollRange());
//        }
        onOverScrolled(0, newScrollY, false, false);
        return flag;
    }

    private int getScrollRange() {
        if (mUsableAnchorsPosition != null && mUsableAnchorsPosition.size() > 0) {
            return mUsableAnchorsPosition.get(mUsableAnchorsPosition.size() - 1);
        }
        return 0;
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
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
        //TODO
//        float topOffset = getTopOffset();
//        float moveDistance = getNearestAnchorByY(0, topOffset) - topOffset;
//        if (moveDistance != 0) {
//            autoApproachAnchor(moveDistance);
//        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //TODO 边界判断
//        scrollBy(0, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
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




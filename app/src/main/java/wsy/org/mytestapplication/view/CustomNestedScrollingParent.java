package wsy.org.mytestapplication.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by shiguotao on 2017/7/31.
 * 抖动问题
 */
public class CustomNestedScrollingParent extends LinearLayout implements NestedScrollingParent {

    View scrollingChild;
    NestedScrollingParentHelper mNestedScrollingParentHelper;
    NestedScrollView nestedScrollView;

    int initHeaderViewHeight;
    int minHeaderViewHeight;
    ViewDragHelper helper;


    public CustomNestedScrollingParent(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomNestedScrollingParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomNestedScrollingParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        scrollingChild = this.getChildAt(0);
        nestedScrollView = (NestedScrollView) this.getChildAt(1);
        scrollingChild.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (initHeaderViewHeight <= 0) {
                    initHeaderViewHeight = scrollingChild.getMeasuredHeight();
                    minHeaderViewHeight = initHeaderViewHeight - 400;
                }
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return target instanceof NestedScrollView && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e("dy", dy + "");
        if (dy > 0) {// 手势向上，页面上滑，加载下面的东西
            //负数表示检测上滑，正数表示下滑
            boolean b = ViewCompat.canScrollVertically(nestedScrollView, -1);
            if (!b) {
                LayoutParams params = (LayoutParams) scrollingChild.getLayoutParams();
                if (params.height <= initHeaderViewHeight && params.height >= minHeaderViewHeight) {
                    int tempDy;
                    if (params.height - dy < minHeaderViewHeight) {
                        tempDy = params.height - minHeaderViewHeight;
                        params.height = minHeaderViewHeight;
                    } else {
                        params.height = params.height - dy;
                        tempDy = dy;
                    }
                    scrollingChild.setLayoutParams(params);
                    consumed[1] = tempDy;
                }
            }

        } else if (dy < 0) {// 手势下滑，页面下滑，加载上面的东西
            boolean b = ViewCompat.canScrollVertically(nestedScrollView, -1);

            if (!b) {
                LayoutParams params = (LayoutParams) scrollingChild.getLayoutParams();
                if (params.height <= initHeaderViewHeight && params.height >= minHeaderViewHeight) {
                    int tempDy;
                    if (params.height - dy < initHeaderViewHeight) {
                        params.height = params.height - dy;
                        tempDy = dy;
                    } else {
                        tempDy = -(initHeaderViewHeight - params.height);
                        params.height = initHeaderViewHeight;
                    }
                    scrollingChild.setLayoutParams(params);
                    consumed[1] = tempDy;

                }
            }

        }
        int[] locationInWindow = new int[2];
        nestedScrollView.getLocationInWindow(locationInWindow);
        Log.e("dy consumed.y", consumed[1] + "");

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);

    }
}

package wsy.org.mytestapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wsy on 06/09/2018
 */
public class WrapLayout extends ViewGroup {

    public WrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View childView = getChildAt(0);
        childView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void addView(View child, int index) {
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null && child != null) {
            parentView.addView(child, index);
        }
    }

    @Override
    public void addView(View child, LayoutParams params) {
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null && child != null) {
            parentView.addView(child, params);
        }
    }

    @Override
    public void addView(View child) {
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null && child != null) {
            parentView.addView(child);
        }
    }

    @Override
    public void addView(View child, int width, int height) {

        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null && child != null) {
            parentView.addView(child, width, height);
        }
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null && child != null) {
            parentView.addView(child, index, params);
        }
    }
}

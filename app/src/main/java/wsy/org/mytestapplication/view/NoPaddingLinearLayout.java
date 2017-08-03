package wsy.org.mytestapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by wsy on 2017/4/21.
 */

public class NoPaddingLinearLayout extends RelativeLayout {
    public NoPaddingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoPaddingLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount < 2) {
            super.onLayout(changed, l, t, r, b);
            return;
        }

        View firstView = getChildAt(0);
        View lastView = getChildAt(childCount - 1);
        l = l + getPaddingLeft();
        r = r - getPaddingRight();

        firstView.layout(l, t, l + firstView.getMeasuredWidth(), t + firstView.getMeasuredHeight());
        lastView.layout(r - lastView.getMeasuredWidth(), t, r, t + lastView.getMeasuredHeight());

        if (childCount == 2) {
            return;
        }

        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int totalChildWidth = 0;
        for (int i = 0; i < childCount; i++) {
            totalChildWidth += getChildAt(i).getMeasuredWidth();
        }

        int totalGap = width - totalChildWidth;


        int gap = totalGap / (childCount - 1);
        int left = gap + firstView.getMeasuredWidth() + getPaddingLeft();

        for (int i = 1; i < childCount - 1; i++) {
            //Gravity
            View childView = getChildAt(i);
            childView.layout(left, t, left + childView.getMeasuredWidth(), t + childView.getMeasuredHeight());
            left = left + childView.getMeasuredWidth() + gap;
        }
    }
}

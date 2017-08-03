package wsy.org.mytestapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by wsy on 2016/9/29.
 * <p/>
 * desc:宽度确定，高度为宽度为某个比例的view
 */
public class WidthHeightWeightLayout extends RelativeLayout {


    //宽高比,默认0.75,如果高大于宽，该值小于1；如果宽大于高，该值大于1
    private float mhWRate = 3.0f / 4.0f;
    private int mHeight;
    private int mWidth;

    public WidthHeightWeightLayout(Context context) {
        super(context);
    }

    public WidthHeightWeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (attrs != null) {
//            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.WidthHeightWeightLayout);
//            mhWRate = array.getFloat(R.styleable.WidthHeightWeightLayout_HWRate, 0.75F);
//            array.recycle();
//        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            int childHeight = childView.getMeasuredHeight();
            if (childHeight < mHeight) {
                b = t + childHeight;
            }
            childView.layout(l + getPaddingLeft(), t + getPaddingTop(), r - getPaddingRight(), b - getPaddingBottom());
        }
//        super.onLayout(changed,l,t,r,b);
    }

    private boolean mRelayout = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = 0;
        int height = 0;
        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            try {
                LayoutParams params = (LayoutParams) childView.getLayoutParams();

                if (widthMode == MeasureSpec.EXACTLY) {
                    width = widthSize;
                }
                height = (int) (width * mhWRate);
                if (maxWidth < width) {
                    maxWidth = width;
                    maxHeight = height;
                }
                int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                Log.e("---h",params.height+"");
                Log.e("---w",params.width+"");
                childView.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width),
                        getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height));

            } catch (Exception e) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
        }

        mHeight = maxHeight;
        mWidth = maxWidth;
        setMeasuredDimension(mWidth, mHeight);
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
}

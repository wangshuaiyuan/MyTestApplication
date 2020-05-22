package wsy.org.mytestapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class IndicatorView extends View implements ViewPager.OnPageChangeListener {

    private Drawable mIndicator;
    private int mIndicatorSize;
    private int mWidth;
    private int mContextWidth;
    private int mCount;
    private int mMargin;
    private int mSelectItem;
    private float mOffset;
    private boolean mSmooth;
    private ViewPager.OnPageChangeListener mPageChangeListener;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.IndicatorView);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IndicatorView_indicator_icon:
                    mIndicator = typedArray.getDrawable(attr);
                    break;
                case R.styleable.IndicatorView_indicator_margin:
                    float defaultMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                    mMargin = (int) typedArray.getDimension(attr, defaultMargin);
                    break;
                case R.styleable.IndicatorView_indicator_smooth:
                    mSmooth = typedArray.getBoolean(attr, false);
                    break;
            }
        }
        typedArray.recycle();
        initIndicator();
    }

    private void initIndicator() {
        mIndicatorSize = Math.max(mIndicator.getIntrinsicWidth(), mIndicator.getIntrinsicHeight());
        mIndicator.setBounds(0, 0, mIndicator.getIntrinsicWidth(), mIndicator.getIntrinsicWidth());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width;
        int desired = getPaddingLeft() + getPaddingRight() + mIndicatorSize * mCount + mMargin * (mCount - 1);
        mContextWidth = desired;
        if (mode == MeasureSpec.EXACTLY) {
            width = Math.max(desired, size);
        } else {
            if (mode == MeasureSpec.AT_MOST) {
                width = Math.min(desired, size);
            } else {
                width = desired;
            }
        }
        mWidth = width;
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            int desired = getPaddingTop() + getPaddingBottom() + mIndicatorSize;
            if (mode == MeasureSpec.AT_MOST) {
                height = Math.min(desired, size);
            } else {
                height = desired;
            }
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        int left = mWidth / 2 - mContextWidth / 2 + getPaddingLeft();
        canvas.translate(left, getPaddingTop());
        for (int i = 0; i < mCount; i++) {
            mIndicator.setState(EMPTY_STATE_SET);
            mIndicator.draw(canvas);
            canvas.translate(mIndicatorSize + mMargin, 0);
        }
        canvas.restore();
        float leftDraw = (mIndicatorSize + mMargin) * (mSelectItem + mOffset);
        canvas.translate(left, getPaddingTop());
        canvas.translate(leftDraw, 0);
        mIndicator.setState(SELECTED_STATE_SET);
        mIndicator.draw(canvas);

    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new RuntimeException("请看使用说明");
        }
        mCount = pagerAdapter.getCount();
        viewPager.setOnPageChangeListener(this);
        mSelectItem = viewPager.getCurrentItem();
        invalidate();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener mPageChangeListener) {
        this.mPageChangeListener = mPageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mSmooth) {
            mSelectItem = position;
            mOffset = positionOffset;
            invalidate();
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mSelectItem = position;
        invalidate();
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}

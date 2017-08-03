package wsy.org.mytestapplication.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2016/10/18.
 */

public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    /* The default number of lines */
    private static final int MAX_COLLAPSED_LINES = 3;

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    protected ViewGroup mLayout;//type为1时才会有
    private ViewGroup mLayoutChild1;
    private View mLayoutChild2;

    protected ImageView mButton; // Button to expand/collapse

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private int mMaxCollapsedLines;

    private int mAnimationDuration;

    private boolean mAnimating;

    private OnClickListener mOnClickListener;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;

    private int mLayoutCollapsedHeight = 0;
    private int mLayoutExpandHeight = 0;

    private TextView mTvCollapsedContent;
    private TextView mTvTitle1;
    private TextView mTvTitle2;
    private TextView mTvContent1;
    private TextView mTvContent2;

    private boolean mExpandable = false;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    boolean mCustomMeasure = false;

    @Override
    public void onClick(View view) {

        if (mButton.getVisibility() != View.VISIBLE) {
            return;
        }

        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }

        if (!mExpandable) {
            return;
        }


        if (mCollapsed) {
            mCustomMeasure = true;
            mLayoutChild1.setVisibility(VISIBLE);
            mLayoutChild2.setVisibility(GONE);
        }

        // mark that the animation is in progress
        mAnimating = true;

        Animation animation;
        Animation buttonAnimation;

        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(mLayoutChild1, mLayoutCollapsedHeight, mLayoutExpandHeight);
            buttonAnimation = new RotateAnimation(0, 180, mButton.getMeasuredWidth() / 2, mButton.getMeasuredHeight() / 2);
            buttonAnimation.setDuration(mAnimationDuration);
            buttonAnimation.setInterpolator(new EaseQuadOutInterpolator());
            buttonAnimation.setFillAfter(true);
        } else {
            animation = new ExpandCollapseAnimation(mLayoutChild1, mLayoutExpandHeight, mLayoutCollapsedHeight);
            buttonAnimation = new RotateAnimation(180, 0, mButton.getMeasuredWidth() / 2, mButton.getMeasuredHeight() / 2);
            buttonAnimation.setDuration(mAnimationDuration);
            buttonAnimation.setInterpolator(new EaseQuadOutInterpolator());
            buttonAnimation.setFillAfter(true);
        }

        mCollapsed = !mCollapsed;

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation();
                // clear the animation flag
                mAnimating = false;

                if (mCollapsed) {
                    mLayoutChild1.setVisibility(GONE);
                    mLayoutChild2.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
        mButton.startAnimation(buttonAnimation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mAnimating;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (mLayoutExpandHeight == 0 || mLayoutCollapsedHeight == 0 || mRelayout) {
            mRelayout = false;
            int visibility = mLayoutChild1.getVisibility();
            if (visibility != VISIBLE) {
                mLayoutChild1.setVisibility(VISIBLE);
            }

            int visibility2 = mLayoutChild2.getVisibility();
            if (visibility2 != VISIBLE) {
                mLayoutChild2.setVisibility(VISIBLE);
            }
            mTvCollapsedContent.setMaxLines(Integer.MAX_VALUE);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (mLayoutChild1 != null) {
                mLayoutExpandHeight = mLayoutChild1.getMeasuredHeight();
            }
            if (mLayoutChild2 != null) {
                mLayoutCollapsedHeight = mLayoutChild2.getMeasuredHeight();
            }

            mLayoutChild1.setVisibility(visibility);
            mLayoutChild2.setVisibility(visibility2);
            int lineCount = mTvCollapsedContent.getLineCount();
            if (lineCount <= mMaxCollapsedLines) {
                mExpandable = false;
            } else {
                mExpandable = true;
            }
            mTvCollapsedContent.setMaxLines(mMaxCollapsedLines);
        }

        if (!mExpandable) {
            mButton.setVisibility(GONE);
        } else {
            mButton.setVisibility(VISIBLE);
        }

        if (mCustomMeasure) {
            mCustomMeasure = false;
            mLayoutChild1.getLayoutParams().height = mLayoutCollapsedHeight;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * @param collapseContent 合并时展示的内容
     * @param tile1           标题1
     * @param title2          标题2
     * @param content1        第一段内容
     * @param content2        第二段内容
     */
    public void setContent(CharSequence collapseContent, CharSequence tile1, CharSequence title2, CharSequence content1, CharSequence content2) {
        mRelayout = true;
        mTvTitle1.setText(tile1);
        mTvContent1.setText(content1);
        mTvTitle2.setText(title2);
        mTvContent2.setText(content2);
        mTvCollapsedContent.setText(collapseContent);
        mLayoutChild1.getLayoutParams().width = ViewPager.LayoutParams.MATCH_PARENT;
        mLayoutChild1.getLayoutParams().height = ViewPager.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        typedArray.recycle();
        // enforces vertical orientation
        setOrientation(LinearLayout.VERTICAL);
    }

    private void findViews() {
        View v = getChildAt(0);
        mLayout = (ViewGroup) v;
        v.setOnClickListener(this);
        mLayoutChild1 = (ViewGroup) mLayout.getChildAt(0);
        mLayoutChild2 = mLayout.getChildAt(1);

        mTvCollapsedContent = (TextView) mLayoutChild2;
        mTvTitle1 = (TextView) mLayoutChild1.getChildAt(0);
        mTvContent1 = (TextView) mLayoutChild1.getChildAt(1);
        mTvTitle2 = (TextView) mLayoutChild1.getChildAt(2);
        mTvContent2 = (TextView) mLayoutChild1.getChildAt(3);
        mButton = (ImageView) getChildAt(1);
        mButton.setOnClickListener(this);
    }


    private static boolean isPostLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
            setInterpolator(new EaseQuadOutInterpolator());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }
}
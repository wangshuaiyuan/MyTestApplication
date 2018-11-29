package tablayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wsy on 06/11/2018
 * <p>
 * 目前可能存在的问题测量
 * 移除某一个tab
 */
public class TabLayout extends HorizontalScrollView {

    /**
     * 固定模式
     */
    public static final int MODE_FIXED = 1001;
    /**
     * 滚动模式
     */
    public static final int MODE_SCROLLABLE = 1002;


    /**
     * 居中布局，该布局方式配合fixed模式使用{@link #MODE_FIXED}.
     */
    public static final int GRAVITY_CENTER = 2001;
    /**
     * 填充布局
     */
    public static final int GRAVITY_FILL = 2002;


    /**
     * 指示器位于顶部
     */
    public static final int INDICATOR_ALIGN_TOP = 3001;
    /**
     * 指示器位于底部
     */
    public static final int INDICATOR_ALIGN_BOTTOM = 3002;


    private int mMode = MODE_SCROLLABLE;

    private int mGravity = GRAVITY_FILL;
    /**
     * 是否显示indicator
     */
    private boolean isShowIndicator = true;

    private int mIndicatorAlign = INDICATOR_ALIGN_BOTTOM;

    private Indicator mIndicator;

    private SlidingTab mSlidingTab;

    private ContentWrap mContentWrap;

    private Paint mSelectedIndicatorPaint = new Paint();


    private TabAdapter mAdapter;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        buildUp();
        test();
    }

    private void init() {
        mIndicator = new Indicator(getContext());
        mSlidingTab = new SlidingTab(getContext());
        mContentWrap = new ContentWrap(getContext());
    }


    private void test() {
        mSelectedIndicatorPaint.setColor(Color.parseColor("#ff4081"));
        TabAdapter adapter = new TabAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                TextView tv = new TextView(getContext());
                tv.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
                return tv;
            }

            @Override
            public void refreshView(View itemView, int position, int selectedPosition, float offset) {
                TextView tv = (TextView) itemView;
                tv.setText("哈哈哈" + position);
            }
        };

    }

    private void buildUp() {
        addView(mContentWrap);
        if (isShowIndicator) {
            mContentWrap.addView(mIndicator);
        }
        mContentWrap.addView(mSlidingTab);
    }

    public void setScrollPosition(int position, float offset, boolean updateIndicatorPosition) {
        final int roundedPosition = Math.round(position + offset);
        if (roundedPosition < 0 || roundedPosition >= mSlidingTab.getChildCount()) {
            return;
        }

        if (updateIndicatorPosition) {
            mIndicator.updateIndicatorPosition(position, offset);
        }
        scrollTo(calculateScrollXForTab(position, offset), 0);
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        if (mMode == MODE_SCROLLABLE) {
            final View selectedChild = mSlidingTab.getChildAt(position);
            final View nextChild = position + 1 < mSlidingTab.getChildCount()
                    ? mSlidingTab.getChildAt(position + 1)
                    : null;
            final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
            final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;

            int scrollBase = selectedChild.getLeft() + (selectedWidth / 2) - (getWidth() / 2);
            int scrollOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);

            return (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                    ? scrollBase + scrollOffset
                    : scrollBase - scrollOffset;
        }
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 1) {
            final View child = getChildAt(0);
            boolean remeasure = false;

            switch (mMode) {
                case MODE_SCROLLABLE:
                    remeasure = child.getMeasuredWidth() < getMeasuredWidth();
                    break;
                case MODE_FIXED:
                    remeasure = child.getMeasuredWidth() != getMeasuredWidth();
                    break;
            }
            if (remeasure) {
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    /**
     * 刷新指示器浮动方向
     */
    private void refreshIndicatorAlign() {
        //不展示指示器
        if (!isShowIndicator) {
            return;
        }
        mContentWrap.removeAllViews();
        if (mIndicatorAlign == INDICATOR_ALIGN_TOP) {
            mContentWrap.addView(mIndicator);
            mContentWrap.addView(mSlidingTab);
        } else {
            mContentWrap.addView(mSlidingTab);
            mContentWrap.addView(mIndicator);
        }
    }


    /**
     * 设置option
     */
    private void applyOption(Option option) {
        //是否需要刷新
        boolean needRefresh = false;

        if (option.mode != mMode && (option.mode == MODE_FIXED || option.mode == MODE_SCROLLABLE)) {
            needRefresh = true;
            mMode = option.mode;
        }

        if (option.indicatorAlign != mIndicatorAlign && (option.indicatorAlign == INDICATOR_ALIGN_TOP || option.indicatorAlign == INDICATOR_ALIGN_BOTTOM)) {
            needRefresh = true;
            mIndicatorAlign = option.indicatorAlign;
        }

        if (option.gravity != mGravity && (option.gravity == GRAVITY_CENTER || option.gravity == GRAVITY_FILL)) {
            needRefresh = true;
            mGravity = option.gravity;
        }

        if (needRefresh) {
            refreshIndicatorAlign();
            requestLayout();
        }
    }

    class ContentWrap extends LinearLayout {

        public ContentWrap(Context context) {
            super(context);
            setOrientation(VERTICAL);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            boolean remeasure = false;

            switch (mMode) {
                case MODE_SCROLLABLE:
                    remeasure = mSlidingTab.getMeasuredWidth() < getMeasuredWidth();
                    break;
                case MODE_FIXED:
                    remeasure = mSlidingTab.getMeasuredWidth() != getMeasuredWidth();
                    break;
            }

            if (remeasure) {
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), mSlidingTab.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                mSlidingTab.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }

            if (isShowIndicator) {
                mIndicator.measure(MeasureSpec.makeMeasureSpec(mSlidingTab.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mIndicator.getMeasuredHeight(), MeasureSpec.EXACTLY));
            }
        }
    }

    /**
     * 指示器
     */
    class Indicator extends View {
        private int mIndicatorLeft;
        private int mIndicatorRight;

        public Indicator(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
                canvas.drawRect(mIndicatorLeft, 0, mIndicatorRight, getHeight(), mSelectedIndicatorPaint);
            }
        }

        /**
         * 设置指示器位置
         *
         * @param left  左
         * @param right 右
         */
        private void setIndicatorPosition(int left, int right) {
            if (left != mIndicatorLeft || right != mIndicatorRight) {
                mIndicatorLeft = left;
                mIndicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        /**
         * 刷新指示器
         */
        protected void updateIndicatorPosition(int selectedPosition, float offset) {
            final View selectedTitle = getChildAt(selectedPosition);
            int left, right;

            if (selectedTitle != null && selectedTitle.getWidth() > 0) {
                left = selectedTitle.getLeft();
                right = selectedTitle.getRight();

                if (offset > 0f && selectedPosition < getChildCount() - 1) {
                    View nextTitle = mSlidingTab.getChildAt(selectedPosition + 1);
                    left = (int) (offset * (nextTitle.getLeft() - left) + left);
                    right = (int) (offset * (nextTitle.getRight() - right) + right);
                }
            } else {
                left = right = -1;
            }
            setIndicatorPosition(left, right);
        }
    }


    /**
     * 展示内容容器
     */
    class SlidingTab extends LinearLayout {

        public SlidingTab(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setBackgroundColor(Color.parseColor("#ff4081"));
        }

        /**
         * 设置选中位置
         *
         * @param position 位置
         * @param offset   便宜量
         */
        void setSelectedPosition(int position, float offset) {
            if (mAdapter != null) {
                mAdapter.refreshView(getChildAt(position), position, position, offset);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
                return;
            }

            if (mMode == MODE_FIXED && mGravity == GRAVITY_CENTER) {
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

                boolean remeasure = false;

                if (largestTabWidth * count <= getMeasuredWidth()) {
                    for (int i = 0; i < count; i++) {
                        final LinearLayout.LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                        if (lp.width != largestTabWidth || lp.weight != 0) {
                            lp.width = largestTabWidth;
                            lp.weight = 0;
                            remeasure = true;
                        }
                    }
                } else {
                    mGravity = GRAVITY_FILL;
                    updateTabViews(false);
                    remeasure = true;
                }

                if (remeasure) {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }

        void updateTabViews(final boolean requestLayout) {
            for (int i = 0; i < mSlidingTab.getChildCount(); i++) {
                View child = mSlidingTab.getChildAt(i);
                updateTabViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
                if (requestLayout) {
                    child.requestLayout();
                }
            }
        }

        private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
            if (mMode == MODE_FIXED && mGravity == GRAVITY_FILL) {
                lp.width = 0;
                lp.weight = 1;
            } else {
                lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                lp.weight = 0;
            }
        }

        /**
         * 刷新
         */
        void refresh() {
            removeAllViews();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                for (int i = 0, count = mAdapter.getCount(); i < count; i++) {
                    View itemView = mAdapter.getView(this, i);
                    mAdapter.refreshView(itemView, 0, 0, 0);
                    addView(itemView);
                }
                requestLayout();
            }
        }
    }


    /**
     * 适配器
     */
    public abstract class TabAdapter {
        /**
         * @return
         */
        public abstract int getCount();

        /**
         * @param parent
         * @param position
         * @return
         */
        public abstract View getView(ViewGroup parent, int position);

        /**
         * @param itemView
         * @param position
         * @param selectedPosition
         * @param offset
         */
        public abstract void refreshView(View itemView, int position, int selectedPosition, float offset);


        /**
         * 刷新数据
         */
        public void notifyDatasetChanged() {
            mSlidingTab.refresh();
        }

    }

    /**
     * 设置指示器高度
     */
    public void setIndicatorHeight(int height) {
        if (mIndicator.getVisibility() != VISIBLE) {
            return;
        }
        if (mIndicator.getLayoutParams().height == height) {
            return;
        }
        mIndicator.getLayoutParams().height = height;
        requestLayout();
    }

    /**
     * 设置adapter
     */
    public void setAdapter(TabAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 配置项
     */
    public class Option {
        private int mode;
        private int gravity;
        private int indicatorAlign;

        private Option() {

        }

        public void apply() {
            applyOption(this);
        }


        public class Builder {

            private int mode;
            private int gravity;
            private int indicatorAlign;

            public Builder mode(int mode) {
                this.mode = mode;
                return this;
            }

            public Builder gravity(int gravity) {
                this.gravity = gravity;
                return this;
            }

            public Builder indicatorAlign(int align) {
                this.indicatorAlign = align;
                return this;
            }

            public Option build() {
                Option op = new Option();
                op.gravity = gravity;
                op.mode = mode;
                op.indicatorAlign = indicatorAlign;
                return op;
            }
        }
    }
}

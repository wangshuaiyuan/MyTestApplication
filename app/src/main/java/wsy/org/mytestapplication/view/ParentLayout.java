//package wsy.org.mytestapplication.view;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v4.view.NestedScrollingParent;
//import android.support.v4.view.NestedScrollingParentHelper;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.widget.ViewDragHelper;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.yu.zz.xzpulllayout.Shi.Layout.TopLayout;
//import com.yu.zz.xzpulllayout.tools.Utils;
//
///**
// * Created by zz on 2017/7/30.
// */
//
//public class ParentLayout extends ViewGroup implements NestedScrollingParent {
//
//    View mTop;
//
//    RecyclerView rcl;
//    ViewDragHelper viewDragHelper;
//
//
//
//    NestedScrollingParentHelper mNestedScrollingParentHelper;
//    /*当前View移动了多少*/
//    int mMove = 0;
//
//    private int y=0 ;
//
//    int mTopLayoutHeigh;
//    private int down_y;
//
//    public ParentLayout(Context context) {
//        this(context, null);
//    }
//
//    public ParentLayout(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public ParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measureChild(mTop, widthMeasureSpec, heightMeasureSpec);
//        measureChild(rcl, widthMeasureSpec, heightMeasureSpec);
//    }
//
//    private void init() {
//        mTopLayoutHeigh = Utils.dp2px(getContext(), 120);
//        adaptTop();
//        adaptRcl();
//        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
//
//    }
//
//    private void adaptRcl() {
//        rcl = new RecyclerView(getContext());
//        rcl.setBackgroundColor(Color.GREEN);
//        rcl.setLayoutManager(new LinearLayoutManager(getContext()));
//        rcl.setAdapter(new RecyclerView.Adapter() {
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                TextView tv = new TextView(getContext());
//                int padding = 30;
//                tv.setPadding(padding, padding, padding, padding);
//
//                return new RecyclerView.ViewHolder(tv) {
//                };
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                TextView tv = (TextView) holder.itemView;
//                tv.setText("item" + position);
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 100;
//            }
//        });
//        addView(rcl);
//    }
//
//    private void adaptTop() {
//        mTop = new TopLayout(getContext());
//        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, mTopLayoutHeigh);
//        mTop.setLayoutParams(lp);
//        mTop.setBackgroundColor(Color.BLUE);
//        addView(mTop);
//    }
//
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        mTop.layout(0, 0, getMeasuredWidth(), mTop.getMeasuredHeight());
//        rcl.layout(0, mTop.getMeasuredHeight()+y, getMeasuredWidth(), getMeasuredHeight());
//    }
//
//    @Override
//    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        Log.e("zz", "onStartNestedScroll" + nestedScrollAxes);
//        return true;
//    }
//
//
//    @Override
//    public void onNestedScrollAccepted(View child, View target, int axes) {
//        Log.e("zz", "onNestedScrollAccepted" + axes);
//        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
//    }
//
//    @Override
//    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Log.e("zz", "onNestedPreScroll：" + " dx:"  + " dy:" + dyConsumed );
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//    }
//
//    @Override
//    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        Log.e("zz", "before：" + " dx:" + dx + " dy:" + dy + consumed.toString());
//        if (dy > 0) {
//            if (!ViewCompat.canScrollVertically(rcl, -1)) {
//                y = y-dy;
//                requestLayout();
//                consumed[1]=dy;
//            }
//        } else {
//            if (Math.abs(rcl.getTranslationY()) < mTopLayoutHeigh) {
//                y =y -dy;
//                requestLayout();
//                consumed[1]=dy;
//            }
//
//        }
//
//        Log.e("zz", "onNestedPreScroll：" + " dx:" + dx + " dy:" + dy + consumed.toString());
//    }
//
//
//}

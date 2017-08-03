package wsy.org.mytestapplication.view.pull2Refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.tool.Util;

public class Pull2RefreshListView extends ListView implements OnScrollListener {

    private OnScrollListener mScrollListener;
    private OnRefreshListener mListViewListener;
    private Pull2RefreshListViewHeader mHeaderView;
    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight;
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;
    // 下拉状态枚举值
    private final static int RELEASE_TO_REFRESH = 0;
    private final static int PULL_TO_REFRESH = 1;
    private final static int REFRESHING = 2;

    // 下拉刷新结果枚举值
    public static final int REFRESH_SUCCEED = 0;
    public static final int REFRESH_FAIL = 1;

    // 下拉结果显示时间，单位ms
    private static final int REFRESH_RESULT_TIME = 1000;

    private MyTimerTask mTask;
    private boolean mHasRefresh = false;// 是否已经刷新
    private float downY, lastY;
    public float moveDeltaY = 0;
    private boolean canPull = true;
    private TextView mTvRefreshResult;
    private float radio = 2;
    private boolean isTouchInRefreshing = false;
    private Timer mTimer;
    public float MOVE_SPEED = 8;
    private float refreshDist = Util.dip2px(getContext(), 80);
    private int mHeightTvResult = Util.dip2px(getContext(), 40);
    private int state = PULL_TO_REFRESH;

    private float xDistance, yDistance, xLast, yLast;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xDistance = yDistance = 0f;
                    xLast = ev.getX();
                    yLast = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float curX = ev.getX();
                    final float curY = ev.getY();

                    xDistance += Math.abs(curX - xLast);
                    yDistance += Math.abs(curY - yLast);
                    xLast = curX;
                    yLast = curY;

                    if (xDistance > yDistance) {
                        return false; // 表示向下传递事件
                    }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public Pull2RefreshListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public Pull2RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public Pull2RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        super.setOnScrollListener(this);
        mTimer = new Timer();
        mHeaderView = new Pull2RefreshListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.refresh_red_ball_content);
        addHeaderView(mHeaderView);
        ImageView ivRedBall = (ImageView) mHeaderView.findViewById(R.id.refresh_red_ball_animation);
        ((AnimationDrawable) ivRedBall.getDrawable()).start();
        mTvRefreshResult = (TextView) mHeaderView.findViewById(R.id.tv_refresh_result);
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    // //////////////////////////////////////new
    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mHasRefresh = false;
                    downY = ev.getY();
                    lastY = downY;
                    if (mTask != null) {
                        mTask.cancel();
                    }
                    if (ev.getY() < moveDeltaY)
                        return true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (canPull) {
                        if (moveDeltaY == 0 && ev.getY() - lastY < 0) {
                            lastY = ev.getY();
                            return super.onTouchEvent(ev);
                        }
                        if (ev.getY() - lastY > 0) {
                            mTvRefreshResult.setVisibility(View.INVISIBLE);
                        }
                        moveDeltaY = moveDeltaY + (ev.getY() - lastY) / radio;
                        if (moveDeltaY < 0) {
                            moveDeltaY = 0;
                        }
                        if (moveDeltaY > getMeasuredHeight())
                            moveDeltaY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            isTouchInRefreshing = true;
                        }
                        lastY = ev.getY();
                        radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * moveDeltaY));
                        updateHeaderHeight(moveDeltaY);
                        if (moveDeltaY <= refreshDist && state == RELEASE_TO_REFRESH) {
                            changeState(PULL_TO_REFRESH);
                        }
                        if (moveDeltaY >= refreshDist && state == PULL_TO_REFRESH) {
                            changeState(RELEASE_TO_REFRESH);
                        }

                        if (moveDeltaY <= 0) {
                            MotionEvent event2 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, ev.getX(), ev.getY(), 0);
                            super.onTouchEvent(event2);
                        }
                        return true;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (canPull && moveDeltaY > 0) {
                        if (moveDeltaY > refreshDist)
                            isTouchInRefreshing = false;
                        if (state == RELEASE_TO_REFRESH) {
                            changeState(REFRESHING);
                            OnPull2Refresh();
                        }
                        hideHead();
                        sendCancelEvent(ev);
                        return true;
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void sendCancelEvent(MotionEvent event) {
        if (event == null) {
            return;
        }
        MotionEvent last = event;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case PULL_TO_REFRESH:
                break;
            case RELEASE_TO_REFRESH:
                break;
            case REFRESHING:
                mHasRefresh = true;
                break;
            default:
                break;
        }
    }

    private void hideHead() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        mTimer.schedule(mTask, 0, 20);
    }

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * moveDeltaY));
            if (state == REFRESHING && moveDeltaY <= refreshDist && !isTouchInRefreshing) {
                moveDeltaY = refreshDist;
                mTask.cancel();
            }
            moveDeltaY -= MOVE_SPEED;
            if (moveDeltaY <= mHeightTvResult && mHasRefresh) {
                moveDeltaY = mHeightTvResult;
                mTvRefreshResult.setVisibility(View.VISIBLE);
                mTask.cancel();
                mTask = new MyTimerTask(updateHandler);
                mTimer.schedule(mTask, REFRESH_RESULT_TIME, 20);
                mHasRefresh = false;
            }

            if (moveDeltaY <= 0) {
                moveDeltaY = 0;
                if (state != REFRESHING)
                    changeState(PULL_TO_REFRESH);
                mTask.cancel();
                setEnabled(true);
            }
            updateHeaderHeight(moveDeltaY);
        }
    };

    Handler autoRefreshHandler = new Handler() {
        public void handleMessage(Message msg) {
            MOVE_SPEED = 8;
            if (state == REFRESHING) {
                return;
            }
            moveDeltaY += MOVE_SPEED;
            if (moveDeltaY > refreshDist) {
                moveDeltaY = refreshDist;
                isTouchInRefreshing = false;
                changeState(REFRESHING);
                mTask.cancel();
                OnPull2Refresh();
            }
            updateHeaderHeight(moveDeltaY);
        }
    };

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (state == REFRESHING) {
            return;
        }
        if (mTvRefreshResult != null) {
            mTvRefreshResult.setVisibility(INVISIBLE);
        }
        changeState(PULL_TO_REFRESH);
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(autoRefreshHandler);
        mTimer.schedule(mTask, 0, 20);
    }

    /**
     * 结束刷新
     *
     * @param refreshResult
     */
    public void finishRefresh(int refreshResult, String message) {
        mTvRefreshResult.setText(message);
        switch (refreshResult) {
            case REFRESH_SUCCEED:
                break;
            case REFRESH_FAIL:
                break;
            default:
                break;
        }
        state = PULL_TO_REFRESH;
        hideHead();
    }

    // ////////////////////////////////////end new

    private void OnPull2Refresh() {
        if (mListViewListener != null) {
            mListViewListener.onRefresh();
        }
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnMyScrollListener) {
            OnMyScrollListener l = (OnMyScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta);
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
                mHeaderView.setState(Pull2RefreshListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(Pull2RefreshListViewHeader.STATE_NORMAL);
            }
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view != null && view.getChildAt(0) != null) {
            if (view.getFirstVisiblePosition() == 0 && view.getChildAt(0).getTop() >= 0) {
                canPull = true;
            } else {
                canPull = false;
            }
        }

        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        mListViewListener = l;
    }

    public interface OnMyScrollListener extends OnScrollListener {
        void onXScrolling(View view);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public int getHeadVisibleHeight() {
        return mHeaderView.getVisibleHeight();
    }
}

package wsy.org.mytestapplication.view.pull2Refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wsy.org.mytestapplication.R;


public class Pull2RefreshListViewHeader extends LinearLayout {
    private LinearLayout mContainer;
    private TextView mTvResult;
    private int mState = STATE_NORMAL;
    private int mHeight;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public Pull2RefreshListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public Pull2RefreshListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        // MarginLayoutParams parms = new MarginLayoutParams(source);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.refresh_red_ball_header_view, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mTvResult = (TextView) findViewById(R.id.tv_refresh_result);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) { // 显示进度
        } else { // 显示箭头图片
        }
        switch (state) {
            case STATE_NORMAL:
                break;
            case STATE_READY:
                break;
            case STATE_REFRESHING:
                break;
            default:
        }
        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        return mContainer.getLayoutParams().height;
    }

}

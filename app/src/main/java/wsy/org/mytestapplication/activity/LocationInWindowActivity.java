package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2017/8/4.
 */

public class LocationInWindowActivity extends Activity implements View.OnClickListener {

    public static final int SCROLL_TYPE_RESIZE = 101;
    public static final int SCROLL_TYPE_SCROLL_BY = 102;


    private View mLlRoot;
    private View topView;
    private View newView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_in_window);
        initView();
    }

    private void initView() {
        topView = findViewById(R.id.location_in_window_v);
        newView = findViewById(R.id.location_in_window_v2);
        mLlRoot = findViewById(R.id.location_in_window_root_ll);


        findViewById(R.id.location_in_window_scroll_by_btn).setOnClickListener(this);
        findViewById(R.id.location_in_window_resize_btn).setOnClickListener(this);
    }

    private void moveByScroll() {
        mLlRoot.scrollBy(0, (int) getResources().getDimension(R.dimen.location_in_window_scroll_velocity));
    }

    private void moveByResize(View v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height -= (int) getResources().getDimension(R.dimen.location_in_window_scroll_velocity);
        v.setLayoutParams(layoutParams);
    }

    private void customAScroll(int type) {
        int[] location = new int[2];
        newView.getLocationInWindow(location);

        if (type == SCROLL_TYPE_SCROLL_BY) {
            moveByScroll();
        } else if (type == SCROLL_TYPE_RESIZE) {
            moveByResize(topView);
        }

        int[] newLocation = new int[2];
        newView.getLocationInWindow(newLocation);
        Log.e("location1", location[1] + "");
        Log.e("location2", newLocation[1] + "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] postLocation = new int[2];
                newView.getLocationInWindow(postLocation);
                Log.e("location1", postLocation[1] + "");
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.location_in_window_scroll_by_btn:
                customAScroll(SCROLL_TYPE_SCROLL_BY);
                break;
            case R.id.location_in_window_resize_btn:
                customAScroll(SCROLL_TYPE_RESIZE);
                break;
        }
    }
}

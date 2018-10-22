package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.OverScroller;
import android.widget.TextView;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 29/06/2018
 */
public class ScrollerTestActivity extends Activity implements View.OnClickListener {

//    OverScroller mOverScroller = new OverScroller(this);

    private TextView mTvScrollTo;
    private TextView mTvScrollBy;
    private TextView mTvScrollSpingBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller_test);
        mTvScrollTo = findViewById(R.id.tv_scroller_scroll_to);
        mTvScrollBy = findViewById(R.id.tv_scroller_scroll_by);
        mTvScrollSpingBack = findViewById(R.id.tv_scroller_scroll_spring_back);

        mTvScrollTo.setOnClickListener(this);
        mTvScrollBy.setOnClickListener(this);
        mTvScrollSpingBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
    }
}

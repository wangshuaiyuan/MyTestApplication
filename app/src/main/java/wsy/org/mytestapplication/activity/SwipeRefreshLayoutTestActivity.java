package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.adapter.SwipeRefreshLayoutTestAdapter;

/**
 * Created by wsy on 2017/1/5.
 */
public class SwipeRefreshLayoutTestActivity extends Activity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh_layout_test);
        findView();
        initData();
    }

    private void findView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mData.add("我是测试数据" + i);
        }
        final SwipeRefreshLayoutTestAdapter adapter = new SwipeRefreshLayoutTestAdapter(mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mData.add(0, "新加载数据");
                adapter.notifyDataSetChanged();
            }
        });
    }
}

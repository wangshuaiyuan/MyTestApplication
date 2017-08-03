package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.adapter.PullListAdapter;
import wsy.org.mytestapplication.view.pull2Refresh.Pull2RefreshListView;

/**
 * Created by wsy on 2016/12/22.
 */
public class Pull2RefreshListActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_list);
        final Pull2RefreshListView lv = (Pull2RefreshListView) findViewById(R.id.searchlist_itemlv);

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("哈哈哈" + i);
        }
        PullListAdapter adapter = new PullListAdapter(this, data);
        lv.setAdapter(adapter);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                lv.finishRefresh(100, "哈哈哈");
            }
        };
        lv.setOnRefreshListener(new Pull2RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendMessageDelayed(Message.obtain(), 2000);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("----", lv.getHeadVisibleHeight() + "");
            }
        });


    }
}


package wsy.org.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import im.sdk.debug.RegisterAndLoginActivity;
import wsy.org.mytestapplication.activity.AppbarLayoutTestActivity;
import wsy.org.mytestapplication.activity.ChangeRecyclerViewItemHeightActivity;
import wsy.org.mytestapplication.activity.CircleImageViewActivity;
import wsy.org.mytestapplication.activity.EventBusActivity;
import wsy.org.mytestapplication.activity.ExpandTextviewActivity;
import wsy.org.mytestapplication.activity.HtmlStringTestActivity;
import wsy.org.mytestapplication.activity.NestedTestActivity;
import wsy.org.mytestapplication.activity.NoPaddingTestActivity;
import wsy.org.mytestapplication.activity.PermissionTestActivity;
import wsy.org.mytestapplication.activity.Pull2RefreshListActivity;
import wsy.org.mytestapplication.activity.ReversalAnimationActivity;
import wsy.org.mytestapplication.activity.ScreenCaptureActivity;
import wsy.org.mytestapplication.activity.SwipeRefreshLayoutTestActivity;
import wsy.org.mytestapplication.activity.TaskTestActivity;
import wsy.org.mytestapplication.activity.WidthHeightWeightLayoutActivity;
import wsy.org.mytestapplication.adapter.DemoListAdapter;
import wsy.org.mytestapplication.bean.DemoItemBean;

/**
 * Created by wsy on 2016/10/10.
 */
public class ListActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvivity_list);
        mListView = (ListView) findViewById(R.id.lv_my_demos);
        final DemoListAdapter adapter = new DemoListAdapter(this, buildList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DemoItemBean itemBean = (DemoItemBean) adapter.getItem(position);
                Intent intent = new Intent(ListActivity.this, itemBean.demoActivityClass);
                startActivity(intent);
            }
        });
    }


    private ArrayList<DemoItemBean> buildList() {
        ArrayList<DemoItemBean> list = new ArrayList<>();
        DemoItemBean itemBean = new DemoItemBean();
        itemBean.demoActivityClass = MainActivity.class;
        itemBean.demoName = "长宽比layout测试,ViewPager指示器";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = HtmlStringTestActivity.class;
        itemBean.demoName = "TextView展示String html";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = ExpandTextviewActivity.class;
        itemBean.demoName = "带动画可展开TextView";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = ScreenCaptureActivity.class;
        itemBean.demoName = "屏幕截图宽高设置";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = WidthHeightWeightLayoutActivity.class;
        itemBean.demoName = "固定宽高比view";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = CircleImageViewActivity.class;
        itemBean.demoName = "圆形ImageVIew";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = ChangeRecyclerViewItemHeightActivity.class;
        itemBean.demoName = "item动态改变高度";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = Pull2RefreshListActivity.class;
        itemBean.demoName = "pull2refresh";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = SwipeRefreshLayoutTestActivity.class;
        itemBean.demoName = "SwipeRefreshLayoutTestActivity";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = ReversalAnimationActivity.class;
        itemBean.demoName = "ReversalAnimationActivity";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = PermissionTestActivity.class;
        itemBean.demoName = "运行时权限";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = TaskTestActivity.class;
        itemBean.demoName = "任务测试";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = AppbarLayoutTestActivity.class;
        itemBean.demoName = "appbar测试";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = NoPaddingTestActivity.class;
        itemBean.demoName = "均分";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = EventBusActivity.class;
        itemBean.demoName = "EventBus初体验";
        list.add(itemBean);

        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = RegisterAndLoginActivity.class;
        itemBean.demoName = "野狗sdk测试";
        list.add(itemBean);


        itemBean = new DemoItemBean();
        itemBean.demoActivityClass = NestedTestActivity.class;
        itemBean.demoName = "nested测试";
        list.add(itemBean);

        return list;
    }
}

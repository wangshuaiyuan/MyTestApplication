package wsy.org.mytestapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Integer> mIdRes;
    //    private IndicatorView mIndicatorView;
    private ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String a = "aaaa";
        Log.e("---", a.substring(0, 3));

        mIdRes = new ArrayList<>();
        mIdRes.add(R.mipmap.t_01);
        mIdRes.add(R.mipmap.t_02);
        mIdRes.add(R.mipmap.t_03);
        mIdRes.add(R.mipmap.t_04);
//        mViewPager = (ViewPager) findViewById(R.id.id_view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this, mIdRes);
//        mViewPager.setAdapter(mViewPagerAdapter);
//        mIndicatorView = (IndicatorView) findViewById(R.id.id_indicator);
//        mIndicatorView.setViewPager(mViewPager);
        mLv = (ListView) findViewById(R.id.lv_width_height);
        WidthHeightApdater adapter = new WidthHeightApdater();
        mLv.setAdapter(adapter);
    }

    public class WidthHeightApdater extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_width_height, parent, false);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, mIdRes);
            ViewPager dp = (ViewPager) convertView.findViewById(R.id.id_view_pager);
            dp.setAdapter(viewPagerAdapter);
            return convertView;
        }
    }

}

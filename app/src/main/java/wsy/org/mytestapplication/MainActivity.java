package wsy.org.mytestapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navtablayout.Adapter.AbsTabAdapter;
import com.example.navtablayout.NavTabAdapter;
import com.example.navtablayout.NavTabLayout;

import java.util.ArrayList;
import java.util.List;

import wsy.org.mytestapplication.activity.HtmlStringTestActivity;
import wsy.org.mytestapplication.view.ScrollTestView;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
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
        mViewPager = findViewById(R.id.id_view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this, mIdRes);
        mViewPager.setAdapter(mViewPagerAdapter);

//        mIndicatorView = (IndicatorView) findViewById(R.id.id_indicator);
//        mIndicatorView.setViewPager(mViewPager);
//        mLv = (ListView) findViewById(R.id.lv_width_height);
//        WidthHeightApdater adapter = new WidthHeightApdater();
//        mLv.setAdapter(adapter);

        testNavTabLayout();
        testScroll();

    }

    private void testScroll() {
        ScrollTestView view = findViewById(R.id.scroll_test_main_ac);
        view.scrollToBottom();
        final View tv = findViewById(R.id.tv_scroll_test_main_ac);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("---x", tv.getX() + "");
                Log.e("---Y", tv.getY() + "");
            }
        });
    }


    private void testNavTabLayout() {
        final NavTabLayout navTabLayout = findViewById(R.id.nav_tab_test);
        AbsTabAdapter adapter = new AbsTabAdapter() {
            @Override
            public View getItemView(ViewGroup parent, int position, boolean isSelect) {
                TextView textView = new TextView(MainActivity.this);
                textView.setTextSize(14);
                String color = isSelect ? "#ff4081" : "#2c3e50";
                textView.setText(String.format("item%d", position));
                textView.setTextColor(Color.parseColor(color));
                return textView;
            }

            @Override
            public void updateSelectPosition(View itemView, int newSelectPosition) {
                if (itemView instanceof TextView) {
                    TextView tv = (TextView) itemView;
                    tv.setTextColor(Color.parseColor("#ff4081"));
                }
            }

            @Override
            public void updateUnSelectPosition(View itemView, int unSelectPosition) {
                if (itemView instanceof TextView) {
                    TextView tv = (TextView) itemView;
                    tv.setTextColor(Color.parseColor("#2c3e50"));
                }
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        };
        navTabLayout.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                navTabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            ViewPager dp = convertView.findViewById(R.id.id_view_pager);
            dp.setAdapter(viewPagerAdapter);
            return convertView;
        }
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        Intent intent = new Intent(this, HtmlStringTestActivity.class);
        startActivity(intent);

    }
}

package wsy.org.mytestapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navtablayout.core.Adapter.AbsTabAdapter;
import com.example.navtablayout.core.NavTabLayout;
import com.example.navtablayout.core.Option;

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
    private NavTabLayout mNavTabLayout;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdRes = new ArrayList<>();
        mIdRes.add(R.mipmap.t_01);
        mIdRes.add(R.mipmap.t_02);
        mIdRes.add(R.mipmap.t_03);
        mIdRes.add(R.mipmap.t_04);
        mIdRes.add(R.mipmap.t_01);
        mIdRes.add(R.mipmap.t_02);
        mIdRes.add(R.mipmap.t_03);
        mIdRes.add(R.mipmap.t_04);
        mIdRes.add(R.mipmap.t_01);
        mIdRes.add(R.mipmap.t_02);
        mViewPager = findViewById(R.id.id_view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this, mIdRes);
        mViewPager.setAdapter(mViewPagerAdapter);

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

    public void onClick(View view) {
        mNavTabLayout.scrollBy(100, 0);
        Log.e("---", mTabLayout.getScrollX() + "");
    }


    private void testNavTabLayout() {
        mNavTabLayout = findViewById(R.id.nav_tab_test);
        mTabLayout = findViewById(R.id.tablayout_test_main_ac);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(mTabLayout.GRAVITY_FILL);

        for (int i = 0; i < 10; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }

        for (int i = 0; i < 10; i++) {
            mTabLayout.getTabAt(i).setText("太薄" + i);
        }

        Option option = new Option.Builder().isShowIndicator(true).indicatorAlign(Option.INDICATOR_ALIGN_TOP).gravity(Option.GRAVITY_FILL).mode(Option.MODE_SCROLLABLE).build();
        mNavTabLayout.setOption(option);
        mTabLayout.setupWithViewPager(mViewPager, false);

        AbsTabAdapter adapter = new AbsTabAdapter() {
            @Override
            public View getItemView(ViewGroup parent, int position, boolean isSelect) {
                TextView textView = new TextView(MainActivity.this);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(150, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(20, 0, 20, 0);
                textView.setLayoutParams(params);
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
                    Log.e("---SelectPosition--",tv.getText()+"");
                    tv.setTextColor(Color.parseColor("#ff4081"));
                }
            }

            @Override
            public void updateUnSelectPosition(View itemView, int unSelectPosition) {
                if (itemView instanceof TextView) {
                    TextView tv = (TextView) itemView;
                    Log.e("---unSelectPosition--",tv.getText()+"");
                    tv.setTextColor(Color.parseColor("#2c3e50"));
                }
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        };
        mNavTabLayout.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mNavTabLayout.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mNavTabLayout.onPageSelected(position);
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

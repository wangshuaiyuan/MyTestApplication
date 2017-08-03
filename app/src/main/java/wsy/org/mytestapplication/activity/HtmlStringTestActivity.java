package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.common.Utils;

/**
 * Created by wsy on 2016/10/10.
 */
public class HtmlStringTestActivity extends Activity {

    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_string);
        mTv = (TextView) findViewById(R.id.tv_html_string);
        final ViewGroup contentView = (ViewGroup) findViewById(R.id.html_string_content_ll);
        try {
            testException();
        } catch (NumberFormatException e) {
            Log.e("hahhahha", "我收到的异常");
            e.printStackTrace();
        }

        new Thread() {
            @Override
            public void run() {
                //可以在子线程添加view
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Button b = new Button(HtmlStringTestActivity.this);
                getScreenHeight();
                findViewById(R.id.html_string_btn4).requestFocus();
//                b.setText("测试文字");
//                b.setTextColor(Color.parseColor("#ff4081"));
            }
        }.start();
//        Spanned demo1 = Html.fromHtml(getResources().getString(R.string.demo3, 2012,"好好干事"));

        String html =
                "<html>" +
//                    "<head><title>TextView使用HTML</title></head>" +
                        "<body>" +
                        "<img src=\\\"file:///android_asset/a.png\\\"/>" +
                        "<strong>个性描述</strong><br/>" +

//                        "<p><em>斜体</em></p>"+
//
//                        "<p><a href=\"http://www.dreamdu.com/xhtml/\">超链接HTML入门</a>学习HTML!</p>" +
//
//                        "<p><font color=\"#aabb00\">颜色1</p>" +
//
//                        "<p><font color=\"#00bbaa\" >颜色2</p>" +

//                        "<h1>标题1dafasdfasdfasdfasdfasfasfasasfasfasfasfasdfasdfasfasfasfas</h1>" +
                        "我很有个性哈哈哈哈个性描述内部豪华好话好哈哈哈哈哈哈哈哈哈哈爱的发生的发生地方啊发生的发生啊发撒的发生地方啊沙发上地方撒的啊沙发上地方撒的撒发生的发生地方啊撒发生的发生地方啊撒发生的发生地方啊沙发沙发上" +

                        "<strong>内部情况</strong><br/>" +

                        "内部豪华好话好哈哈哈哈哈哈哈哈哈哈爱的发生的发生地方啊发生的发生啊发撒的发生地方啊沙发上地方撒的啊沙发上地方撒的撒发生的发生地方啊撒发生的发生地方啊撒发生的发生地方啊沙发沙发上" +
//                        "<h3>标题2</h3>" +

//                        "<h6>标题3</h6>" +

//                        "<p>大于>小于<</p>" +
//
//                        "<p>下面是网络图片</p>" +
//
                        "<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/>" +

                        "</body>" +
                        "</html>";
//        Spanned demo1 = Html.fromHtml(getResources().getString(R.string.demo4));
        mTv.setText(Html.fromHtml(html));
    }

    public int getScreenHeight() {
        int screenWHPixels[] = null;
        if (screenWHPixels == null) {
            WindowManager _manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics _displayMetrics = new DisplayMetrics();
            _manager.getDefaultDisplay().getMetrics(_displayMetrics);

            screenWHPixels = new int[2];
            screenWHPixels[0] = _displayMetrics.widthPixels;
            screenWHPixels[1] = _displayMetrics.heightPixels;
        }
        return screenWHPixels[1];
    }

    private void testException() throws NumberFormatException {
        int a = Integer.parseInt("aaa");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("btn4top", findViewById(R.id.html_string_btn4).getTop() + "");
        findViewById(R.id.html_string_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.html_string_btn3).setVisibility(View.GONE);
                Log.e("btn4top", findViewById(R.id.html_string_btn4).getTop() + "");
            }
        });

        findViewById(R.id.html_string_btn2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("btn4top", findViewById(R.id.html_string_btn4).getTop() + "");
                String totalMemoty = Utils.getTotalMemory(getBaseContext());
                Log.e("totalMemoty", totalMemoty);
            }
        });

        findViewById(R.id.html_string_btn1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                findViewById(R.id.html_string_btn3).setVisibility(View.VISIBLE);
            }
        });
    }


}

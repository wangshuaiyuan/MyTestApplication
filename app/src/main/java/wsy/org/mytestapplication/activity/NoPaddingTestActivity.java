package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.tool.CalendarUtil;

/**
 * Created by wsy on 2017/4/24.
 */

public class NoPaddingTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_padding_linear);

        //测试日期计算相关的方法
        String date = CalendarUtil.getFormatDate(1498723436000L, "yyyy-MM-dd");
        long millSecond = 0;
        long millSecond2 = 0;
        try {
            millSecond = CalendarUtil.getMillonsecond("2017-06-29", "yyyy-MM-dd");
            millSecond2 = CalendarUtil.getMillonsecond(date, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int days = CalendarUtil.getBetweenDays(millSecond, millSecond2);
        Log.e("days", days + "");
    }
}

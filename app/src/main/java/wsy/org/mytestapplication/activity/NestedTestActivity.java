package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2017/8/3.
 */

public class NestedTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_test_layout);
    }
}

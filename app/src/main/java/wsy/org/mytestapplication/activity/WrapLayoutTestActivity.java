package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 06/09/2018
 */
public class WrapLayoutTestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap_layout_test);
    }
}

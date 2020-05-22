package wsy.org.mytestapplication;

import android.app.Application;


/**
 * Created by wsy on 2017/8/3.
 */

public class YashonApplication extends Application {

    private static YashonApplication sInstance;

    public static YashonApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}

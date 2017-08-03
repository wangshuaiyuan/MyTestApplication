package wsy.org.mytestapplication;

import android.app.Application;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by wsy on 2017/8/3.
 */

public class YashonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initWildDog();
    }

    private void initWildDog() {
        Log.i("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
    }
}

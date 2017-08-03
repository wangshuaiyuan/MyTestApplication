package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2016/10/24.
 */
public class ScreenCaptureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_cature);


        final TextView tv = (TextView) findViewById(R.id.screen_capture_tv);
        final TextView tvResult = (TextView) findViewById(R.id.screen_capture_result_tv);
        final ImageView iv = (ImageView) findViewById(R.id.screen_capture_test_iv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result;
                View v = findViewById(R.id.screen_capture_whole_wrap_rl);
                v.setDrawingCacheEnabled(true);
                v.buildDrawingCache(true);
                Bitmap bitmap = v.getDrawingCache();
                result = "bitmap width:" + bitmap.getWidth() + ",bitmap height:" + bitmap.getHeight() + "\n";
                v.setDrawingCacheEnabled(false);
                //屏幕范围
                Display display = getWindowManager().getDefaultDisplay();
                Rect displayRect = new Rect();
                display.getRectSize(displayRect);
                Rect outRect = new Rect();
                //应用所占屏幕区域
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                result += "屏幕width:" + displayRect.width() + ",屏幕height" + displayRect.height() + "\n";
                result += "app width:" + outRect.width() + ",app height" + outRect.height() + "\n";
                int statusBarHeight = displayRect.height() - outRect.height();
                result += "statusBarHeight" + statusBarHeight;
                tv.setText(result);

                int[] location = new int[2];
                int[] location2 = new int[2];
                iv.getLocationOnScreen(location2);
                iv.getLocationInWindow(location);
                String result2 = "imageView InWindow Y:" + location[1] + "\n";
                result2 += "imageView OnScreen Y:" + location2[1] + "\n";
                int top = iv.getTop();
                result2 += "imageView top:" + top + "\n";

                result2 += "工具类测试\n";
                result2 += "StatusBarHeight:" + getStatusBarHeight(ScreenCaptureActivity.this) + "\n";
                result2 += "ContentHeight:" + getAndroidContentHeight(ScreenCaptureActivity.this) + "\n";
                result2 += "ScreenHeight:" + getScreenHeight(ScreenCaptureActivity.this) + "\n";
                tvResult.setText(result2);
            }
        });


    }

    /**
     * 取得状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }


    public static int getAndroidContentHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
    }


    private static int[] screenWHPixels = null;

    public static int getScreenHeight(Activity activity) {
        if (screenWHPixels == null) {
            WindowManager _manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics _displayMetrics = new DisplayMetrics();
            _manager.getDefaultDisplay().getMetrics(_displayMetrics);
            screenWHPixels = new int[2];
            screenWHPixels[0] = _displayMetrics.widthPixels;
            screenWHPixels[1] = _displayMetrics.heightPixels;
        }
        return screenWHPixels[1];
    }
}

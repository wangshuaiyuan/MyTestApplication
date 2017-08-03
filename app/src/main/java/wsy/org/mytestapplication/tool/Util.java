package wsy.org.mytestapplication.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.List;

public class Util {
    private static int[] screenWHPixels = null;
    private static int minXYScreenPPI = 0;
    private static int sStatusBarHeight = 0;
    private static int sContentViewHeight = 0;



    public static int getScreenWidth(Context context) {
        if (screenWHPixels == null) {
            WindowManager _manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics _displayMetrics = new DisplayMetrics();
            _manager.getDefaultDisplay().getMetrics(_displayMetrics);

            screenWHPixels = new int[2];
            screenWHPixels[0] = _displayMetrics.widthPixels;
            screenWHPixels[1] = _displayMetrics.heightPixels;
        }

        return screenWHPixels[0];
    }

    public static int getScreenHeight(Context context) {
        if (screenWHPixels == null) {
            WindowManager _manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics _displayMetrics = new DisplayMetrics();
            _manager.getDefaultDisplay().getMetrics(_displayMetrics);

            screenWHPixels = new int[2];
            screenWHPixels[0] = _displayMetrics.widthPixels;
            screenWHPixels[1] = _displayMetrics.heightPixels;
        }
        return screenWHPixels[1];
    }

    public static Bitmap compressImage(Bitmap image, int imageSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > imageSize) { //循环判断如果压缩后图片是否大于imageSize kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 测量View的宽高
     */
    public static int[] measureViewWidthHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);

        int[] wh = new int[2];
        wh[0] = v.getMeasuredWidth();
        wh[1] = v.getMeasuredHeight();

        return wh;
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View view = listAdapter.getView(i, listView.getChildAt(i), listView);
            view.measure(0, 0);
            totalHeight += view.getMeasuredHeight();
        }
        totalHeight = totalHeight + listView.getDividerHeight();
        return totalHeight;
    }


    public static String getBitmapThrumbUrl(String longitude, String latitude) {
        return String.format("http://api.map.baidu.com/staticimage?width=640&height=426&zoom=15&markers=%s,%s&markerStyles=-2,http://m.xiaozhu.com/app/appResources/map-dibiao2.png,-1", longitude, latitude);
    }

    public static String getExceptionMsg(Exception e) {
        String message = e.getMessage();
        StackTraceElement[] stack = e.getStackTrace();

        for (int i = 0, j = stack.length; i < j; i++) {
            message += "\n" + stack[i].toString() + ", class: " + stack[i].getClassName() + ", method: " + stack[i].getMethodName() + ", file: " + stack[i].getFileName() + ", line: " + stack[i].getLineNumber();
        }
        message += "\n";

        stack = null;
        return message;
    }

    public static String getTopActivityName(Activity context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.toString();
        else
            return null;
    }

    public static String fixNewLine(String oldString) {
        if (oldString == null)
            return "";
        return oldString.replace("\r", "\n").replace("\n\n", "\n");
    }


    /**
     * 获取是否存在NavigationBar：
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }

        return hasNavigationBar;

    }

    /**
     * 获取NavigationBar的高度：
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
     *
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar2(Context activity) {

        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }
}

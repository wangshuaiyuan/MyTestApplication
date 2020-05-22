package wsy.org.mytestapplication.tool;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by wsy on 2019-12-03
 */
public class PermissionUtil {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void requestPermission(final Activity ac, final CallBack callBack) {
        int hasWriteContactsPermission;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            hasWriteContactsPermission = ContextCompat.checkSelfPermission(ac, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(ac, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(ac, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(ac, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
            }


            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if ((ContextCompat.checkSelfPermission(ac,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
                        handler.post(this);
                    } else {
                        callBack.onSuccess();
                    }
                }
            });
        }else{
            callBack.onSuccess();
        }
    }


    public interface CallBack {
        void onSuccess();
    }
}

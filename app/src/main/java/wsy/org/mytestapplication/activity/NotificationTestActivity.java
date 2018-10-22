package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 25/06/2018
 */
public class NotificationTestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        findViewById(R.id.tv_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                String id = "test1";
                Notification notice = new NotificationCompat.Builder(
                        NotificationTestActivity.this,id)
                        .setLargeIcon(bitmap)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("213123")
                        .setContentTitle("wqewe")
                        .setContentText("大事发生大幅").setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .build();
                notice(notice, 1222,id);
            }
        });
    }

    /**
     * 状态栏的逻辑收敛
     *
     * @param notification 需要提示的一些信息
     */
    public void notice(@NonNull Notification notification, int noticeId,String id ) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            AudioAttributes AUDIO_ATTRIBUTES_DEFAULT = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            //兼容8.0的处理
            NotificationChannel xzChannel = new NotificationChannel(id, "ceshi", NotificationManager.IMPORTANCE_HIGH);
            xzChannel.setSound(Uri.parse("android.resource://"
                    + getPackageName() + "/"
                    + R.raw.song), AUDIO_ATTRIBUTES_DEFAULT);
            notificationManager.createNotificationChannel(xzChannel);
        }
        notificationManager.cancel(noticeId);
        notificationManager.notify(noticeId, notification);
    }
}

package wsy.org.mytestapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.noui.SynchronousQueueTest;

/**
 * Created by wsy on 2017/7/5.
 */

public class EventBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        EventBus.getDefault().register(this);
        initView();
        initEmojiTv();
    }

    private void initView() {
        final SynchronousQueueTest synchronousQueueTest = new SynchronousQueueTest();
        Button btnSendMain = (Button) findViewById(R.id.btn_event_bus_send_main);

        btnSendMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post("hello EventBus");
                synchronousQueueTest.product();
            }
        });

        findViewById(R.id.btn_event_bus_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronousQueueTest.custom();
            }
        });

    }

    private void initEmojiTv() {
        TextView tv = (TextView) findViewById(R.id.tv_emoj_test);
        setEmojiToTextView(tv);
    }

    private void setEmojiToTextView(TextView tv) {
        int unicodeJoy = 0x1F631;
        String emojiString = getEmojiStringByUnicode(unicodeJoy);
        tv.setText(emojiString);
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void receiveMsg(String msg) {
        Log.e("自定义事件receiveMsg", Thread.currentThread().getId() + msg);
    }


    @Subscribe(threadMode = ThreadMode.BackgroundThread)
    public void receiveMsgBgThread(String msg) {
        Log.e("自定义事件receiveMsgBgThread", Thread.currentThread().getId() + msg);
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void receiveMsgPostThread(String msg) {
        Log.e("自定义事件receivePostThread", Thread.currentThread().getId() + msg);
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void receiveMsgAsync(String msg) {
        Log.e("自定义事件receiveMsgAsync", Thread.currentThread().getId() + msg);
    }
}

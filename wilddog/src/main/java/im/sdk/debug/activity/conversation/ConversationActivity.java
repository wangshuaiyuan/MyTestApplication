package im.sdk.debug.activity.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import im.sdk.debug.R;

/**
 * Created by ${chenyn} on 16/3/30.
 *
 * @desc :会话相关主界面
 */
public class ConversationActivity extends Activity implements View.OnClickListener {

    private TextView mTv_showConvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_conversation);
        Button bt_getInfo = (Button) findViewById(R.id.bt_get_conversation_info);
        Button bt_setInfo = (Button) findViewById(R.id.bt_get_info);
        Button bt_enterConversation = (Button) findViewById(R.id.bt_enter_conversation);
        Button bt_deleteConversation = (Button) findViewById(R.id.bt_delete_conversation);
        Button bt_localGetConvList = (Button) findViewById(R.id.bt_localGetConvList);
        Button bt_getConvList = (Button) findViewById(R.id.bt_getConvList);

        mTv_showConvList = (TextView) findViewById(R.id.tv_showConvList);

        bt_getInfo.setOnClickListener(this);
        bt_setInfo.setOnClickListener(this);
        bt_enterConversation.setOnClickListener(this);
        bt_deleteConversation.setOnClickListener(this);
        bt_localGetConvList.setOnClickListener(this);
        bt_getConvList.setOnClickListener(this);
        findViewById(R.id.bt_retract).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int i = v.getId();
        if (i == R.id.bt_get_conversation_info) {
            intent = new Intent(getApplicationContext(), GetConversationInfoActivity.class);
            startActivity(intent);

        } else if (i == R.id.bt_get_info) {
            intent = new Intent(getApplicationContext(), OrderMessageActivity.class);
            startActivity(intent);

        } else if (i == R.id.bt_enter_conversation) {
            intent = new Intent(getApplicationContext(), IsShowNotifySigActivity.class);
            startActivity(intent);

        } else if (i == R.id.bt_delete_conversation) {
            intent = new Intent(getApplicationContext(), DeleteConversationActivity.class);
            startActivity(intent);

        } else if (i == R.id.bt_retract) {
            intent = new Intent(getApplicationContext(), RetractMessageActivity.class);
            startActivity(intent);

        } else if (i == R.id.bt_localGetConvList) {
            mTv_showConvList.setText("降序 : \n");
            List<Conversation> conversationList = JMessageClient.getConversationList();
            if (conversationList != null) {
                for (Conversation convList : conversationList) {
                    if (convList.getType().toString().equals("single")) {
                        UserInfo userInfo = (UserInfo) convList.getTargetInfo();
                        mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  用户名 : " + userInfo.getUserName() + "\n");
                    }
                    if (convList.getType().toString().equals("group")) {
                        GroupInfo groupInfo = (GroupInfo) convList.getTargetInfo();
                        mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  群组id : " + groupInfo.getGroupID() + "\n");
                    }
                }
            } else {
                Toast.makeText(ConversationActivity.this, "该用户没有会话", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.bt_getConvList) {
            mTv_showConvList.setText("不排序 : \n");
            List<Conversation> getConvList = JMessageClient.getConversationListByDefault();
            if (getConvList != null) {
                for (Conversation convList : getConvList) {
                    if (convList.getType().toString().equals("single")) {
                        UserInfo userInfo = (UserInfo) convList.getTargetInfo();
                        mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  用户名 : " + userInfo.getUserName() + "\n");
                    }
                    if (convList.getType().toString().equals("group")) {
                        GroupInfo groupInfo = (GroupInfo) convList.getTargetInfo();
                        mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  群组id : " + groupInfo.getGroupID() + "\n");
                    }
                }
            } else {
                Toast.makeText(ConversationActivity.this, "该用户没有会话", Toast.LENGTH_SHORT).show();
            }

        } else {
        }
    }
}


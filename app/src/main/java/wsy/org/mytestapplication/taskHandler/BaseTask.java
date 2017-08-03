package wsy.org.mytestapplication.taskHandler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.UUID;

import wsy.org.mytestapplication.taskHandler.entity.MessageBean;
import wsy.org.mytestapplication.taskHandler.task.TaskGroup;

/**
 * Created by wsy on 2017/2/24.
 */
public abstract class BaseTask<T> implements ITask {
    public static final int TASK_START = 1001;
    public static final int TASK_PROGRESS = 1002;
    public static final int TASK_SUCCESS = 1003;
    public static final int TASK_FAIL = 1004;

    //所属的任务组 default value is null
    private TaskGroup mTaskGroup = null;

    private String mTaskId = UUID.randomUUID().toString();
    private ArrayList<ITaskCallBack> mCallBacks = new ArrayList<>();

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int msgWhat = msg.what;
            Object obj = msg.obj;
            MessageBean<T> bean = null;
            if (obj != null && obj instanceof MessageBean) {
                bean = (MessageBean<T>) obj;
            }
            if (msgWhat == TASK_START) {//start
                for (ITaskCallBack callBack : mCallBacks) {
                    callBack.onStart(BaseTask.this);
                }
            } else if (msgWhat == TASK_PROGRESS) {//progress
                for (ITaskCallBack callBack : mCallBacks) {
                    if (bean != null) {
                        callBack.onProgress(BaseTask.this, bean.total, bean.present);
                    }
                }
            } else if (msgWhat == TASK_SUCCESS) {//success
                for (ITaskCallBack callBack : mCallBacks) {
                    if (bean != null) {
                        callBack.onSuccess(BaseTask.this, bean.messageInfo, bean.messageBody);
                    }
                }
                //通知任务组当前只任务执行成功
                if (mTaskGroup != null) {
                    mTaskGroup.onTaskSuccess(BaseTask.this, bean);
                }
            } else {//fail
                for (ITaskCallBack callBack : mCallBacks) {
                    if (bean != null)
                        callBack.onFail(BaseTask.this, bean.messageInfo);
                }
                //通知任务组当前只任务执行失败
                if (mTaskGroup != null) {
                    mTaskGroup.onTaskFail(BaseTask.this);
                }
            }
        }
    };

    @Override
    public void setTaskGroup(TaskGroup taskGroup) {
        mTaskGroup = taskGroup;
    }

    @Override
    public void run() {
        execute();
    }


    public void onProgressUpdate(int taskTotal, int taskProgress) {
        Message progressMsg = new Message();
        progressMsg.what = TASK_PROGRESS;
        MessageBean bean = new MessageBean();
        bean.total = taskTotal;
        bean.present = taskProgress;
        progressMsg.obj = bean;
        sendMessage(progressMsg);
    }

    public void onSuccess(T body) {
        Message successMsg = new Message();
        successMsg.what = TASK_SUCCESS;
        MessageBean<T> bean = new MessageBean();
        bean.messageCode = 200;
        bean.messageInfo = "success";
        bean.messageBody = body;
        successMsg.obj = bean;
        sendMessage(successMsg);
    }

    public void onStart() {
        Message msg = new Message();
        msg.what = TASK_START;
        sendMessage(msg);
    }

    public void onFail(String errorMsg) {
        Message msg = new Message();
        msg.what = TASK_FAIL;
        MessageBean bean = new MessageBean();
        bean.messageInfo = errorMsg;
        msg.obj = bean;
        sendMessage(msg);
    }

    @Override
    public String getTaskId() {
        return mTaskId;
    }


    @Override
    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    /**
     * 添加监听器
     *
     * @param callBack
     */
    public void addCallBack(ITaskCallBack callBack) {
        mCallBacks.add(callBack);
    }

}

package wsy.org.mytestapplication.taskHandler;

import android.os.Message;

import wsy.org.mytestapplication.taskHandler.task.TaskGroup;

/**
 * Created by wsy on 2017/2/24.
 */
public interface ITask extends Runnable {

    String getTaskId();

    void execute();

    void sendMessage(Message msg);

    void setTaskGroup(TaskGroup taskGroup);
}

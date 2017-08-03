package wsy.org.mytestapplication.taskHandler.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import wsy.org.mytestapplication.taskHandler.ITask;
import wsy.org.mytestapplication.taskHandler.TaskDispatcher;
import wsy.org.mytestapplication.taskHandler.entity.MessageBean;

/**
 * Created by wsy on 2017/2/28.
 * 任务组
 */
public class TaskGroup<T> {
    /**
     * 失败重试的次数
     */
    private int mRetryTime = 3;

    private List<ITask> mTasks = new ArrayList<>();

    /**
     * 执行失败的task
     */
    private HashMap<String, ITask> mFailedTasks = new HashMap<>();
    /**
     * 失败计数
     */
    private HashMap<String, Integer> mFailedCount = new HashMap<>();

    private String mGroupId = UUID.randomUUID().toString();
    /**
     * 当前任务组所含task的个数
     */
    private int mTaskNum;

    private TaskGroupListener mListener;
    /**
     * 倒计数为mCountDownEndPoint时，调用回调。默认所有任务都成功时调用回调
     */
    private int mCountDownEndPoint = 0;
    private HashMap<String, T> mResult = new HashMap<>();

    public TaskGroup(List<ITask> tasks) {
        if (tasks != null) {
            mTasks.addAll(tasks);
            for (ITask task : tasks) {
                task.setTaskGroup(this);
            }
            mTaskNum = mTasks.size();
        }
    }

    /**
     * 开始执行任务
     */
    public void startTasks() {
        TaskDispatcher.getInstance().startTask(mTasks);
    }

    public String getGroupId() {
        return mGroupId;
    }

    public synchronized void onTaskSuccess(ITask task, MessageBean<T> taskResult) {
        mResult.put(task.getTaskId(), taskResult.messageBody);
        countDown();
    }

    public synchronized void onTaskFail(ITask task) {

        //继续执行task，尝试mRetryTime次
        if (mFailedCount.containsKey(task.getTaskId())) {
            int count = mFailedCount.get(task.getTaskId());
            if (count < mRetryTime) {
                count++;
                //继续上传
                TaskDispatcher.getInstance().startTask(task);
            } else {
                //重试累计次数超过限制，直接略过
                countDown();
                mFailedTasks.put(task.getTaskId(), task);
            }
        } else {
            int count = 1;
            mFailedCount.put(task.getTaskId(), count);
            // 继续上传
            TaskDispatcher.getInstance().startTask(task);
        }
    }

    /**
     * 任务计数器，标志着任务组的执行进度
     */
    private void countDown() {
        mTaskNum--;
        if (mTaskNum == mCountDownEndPoint) {
            if (mListener != null) {
                if (mResult.size() > 0) {
                    mListener.onSuccess(sortResult());
                } else {
                    mListener.onFail();
                }
            }
        }
    }

    private List sortResult() {
        ArrayList<T> sortedResult = new ArrayList<>();
        for (ITask task : mTasks) {
            if (mResult.containsKey(task.getTaskId())) {
                sortedResult.add(mResult.get(task.getTaskId()));
            }
        }
        return sortedResult;
    }

    public void setTaskGroupListener(TaskGroupListener listener) {
        mListener = listener;
    }

    public interface TaskGroupListener {
        void onSuccess(List result);

        void onFail();
    }
}

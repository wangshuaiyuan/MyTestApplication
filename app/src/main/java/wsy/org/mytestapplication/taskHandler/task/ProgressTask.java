package wsy.org.mytestapplication.taskHandler.task;

import android.util.Log;

import wsy.org.mytestapplication.taskHandler.BaseTask;
import wsy.org.mytestapplication.taskHandler.ITask;
import wsy.org.mytestapplication.taskHandler.ITaskCallBack;

/**
 * Created by wsy on 2017/2/27.
 * <p>
 * 带有进度的task
 */
public class ProgressTask extends BaseTask {

    public ProgressTask() {
        this.addCallBack(new ITaskCallBack() {
            @Override
            public void onStart(ITask task) {
                Log.e("onStart", "onStart");
            }

            @Override
            public void onSuccess(ITask task, String successMsg, Object result) {
                Log.e("onSuccess", task.getTaskId() + "---successMsg");
            }

            @Override
            public void onProgress(ITask task, int total, int present) {
                float percent = (float) present / (float) total;
                percent *= 100;
//                Log.e("onProgress", task.getTaskId() + "---" + (int) percent + "");
            }

            @Override
            public void onFail(ITask task, String errorMsg) {
                Log.e("onFail", task.getTaskId() + "---onFail");
            }
        });
    }

    @Override
    public void execute() {

        onStart();
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onProgressUpdate(30, i + 1);
        }
        onSuccess("哈哈哈");
    }
}

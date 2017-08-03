package wsy.org.mytestapplication.taskHandler;

/**
 * Created by wsy on 2017/2/27.
 */
public interface ITaskCallBack {

    void onStart(ITask task);

    void onSuccess(ITask task, String successMsg, Object result);

    void onProgress(ITask task, int total, int present);

    void onFail(ITask task, String errorMsg);


}

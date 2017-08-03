package wsy.org.mytestapplication.taskHandler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by wsy on 2017/2/24.
 * 任务调度者
 */
public class TaskDispatcher {

    private final int MULTI_TASK_SIZE = 3;

    private ExecutorService mTaskExecutorPool;

    private LinkedList<Runnable> mTaskQueue;
    private Handler mPoolThreadHandler;

    private static TaskDispatcher sInstance;
    private Semaphore mTaskSemaphore;

    public static TaskDispatcher getInstance() {
        if (sInstance == null) {
            synchronized (TaskDispatcher.class) {
                sInstance = new TaskDispatcher();
            }
        }
        return sInstance;
    }

    private TaskDispatcher() {
        mTaskQueue = new LinkedList<>();
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        mTaskExecutorPool = Executors.newFixedThreadPool(MULTI_TASK_SIZE);
        mTaskSemaphore = new Semaphore(MULTI_TASK_SIZE);
        initBackThread();
    }

    /**
     * 添加任务
     *
     * @param task
     */
    public void startTask(final ITask task) {
        Runnable taskWrap = new Runnable() {
            @Override
            public void run() {
                task.run();
                //释放信号量
                mTaskSemaphore.release();
            }
        };
        mTaskQueue.add(taskWrap);
        mPoolThreadHandler.sendEmptyMessage(0);
    }

    /**
     * 添加一组任务
     *
     * @param tasks
     */
    public void startTask(List<ITask> tasks) {
        for (final ITask task : tasks) {
            Runnable taskWrap = new Runnable() {
                @Override
                public void run() {
                    task.run();
                    //释放信号量
                    mTaskSemaphore.release();
                }
            };
            mTaskQueue.add(taskWrap);
        }
        for (int i = 0, length = tasks.size(); i < length; i++) {
            mPoolThreadHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 初始化后台轮询线程
     */
    private void initBackThread() {
        // 后台轮询线程
        Thread backThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池去取出一个任务进行执行
                        mTaskExecutorPool.execute(getTask());
                        try {
                            //获取一个信号量
                            mTaskSemaphore.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                Looper.loop();
            }
        };
        backThread.start();
    }

    /**
     * 获取任务
     *
     * @return
     */
    private Runnable getTask() {
        return mTaskQueue.removeFirst();
    }
}

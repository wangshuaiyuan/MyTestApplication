package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import wsy.org.javacase.thread.HelloRxjava;
import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.taskHandler.ITask;
import wsy.org.mytestapplication.taskHandler.TaskDispatcher;
import wsy.org.mytestapplication.taskHandler.task.ProgressTask;
import wsy.org.mytestapplication.taskHandler.task.TaskGroup;

/**
 * Created by wsy on 2017/2/28.
 */
public class TaskTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_test);
        TaskDispatcher.getInstance();
        Button btnStart = (Button) findViewById(R.id.task_test_start_btn);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TaskDispatcher dispatcher = TaskDispatcher.getInstance();
                ProgressTask task = new ProgressTask();
                ProgressTask task2 = new ProgressTask();
                ProgressTask task3 = new ProgressTask();
                ArrayList<ITask> tasks = new ArrayList<>();
                tasks.add(task);
                tasks.add(task2);
                tasks.add(task3);
//                dispatcher.startTask(tasks);

                TaskGroup<String> taskGroup = new TaskGroup<>(tasks);
                taskGroup.setTaskGroupListener(new TaskGroup.TaskGroupListener() {
                    @Override
                    public void onSuccess(List result) {
                        Log.e("result.size", result.size() + "");
                    }

                    @Override
                    public void onFail() {

                    }
                });
                taskGroup.startTasks();
            }
        });

        HelloRxjava rxJavaTest = new HelloRxjava();
        rxJavaTest.zipTest();
    }
}

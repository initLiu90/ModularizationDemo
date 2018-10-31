package com.lzp.modularizationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lzp.api.module1.IMode1;
import com.lzp.api.module2.IMode2;
import com.lzp.core.AppRuntime;
import com.lzp.core.base.BaseActivity;
import com.lzp.core.manager.ApiServiceManager;
import com.lzp.modularizationdemo.task.MTask;
import com.lzp.modularizationdemo.task.Task;
import com.lzp.modularizationdemo.task.TaskManager;
import com.lzp.modularizationdemo.task.TaskManagerImpl;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    @Override
    protected boolean immersiveStatusBarEnabled() {
        return true;
    }

    private void test() {
//        IMode1 api1 = ((ApiServiceManager) MyApplication.getApplication().getAppRuntime().getManager(AppRuntime.API)).getApiService(IMode1.class);
//        Log.e("Test", api1.getMode1Name());
//
//        IMode2 api2 = ((ApiServiceManager) MyApplication.getApplication().getAppRuntime().getManager(AppRuntime.API)).getApiService(IMode2.class);
//        Log.e("Test", api2.getSchool());
        Task task1 = new MTask() {
            @Override
            public String name() {
                return "task1";
            }
        };

        Task task2 = new MTask() {
            @Override
            public String name() {
                return "task2";
            }
        };

        Task task3 = new MTask() {
            @Override
            public String name() {
                return "task3";
            }
        };
        Task task4 = new MTask() {
            @Override
            public String name() {
                return "task4";
            }
        };
        Task task5 = new MTask() {
            @Override
            public String name() {
                return "task5";
            }
        };
        TaskManager taskManager = new TaskManagerImpl();
        task1.dependOn(task2, task3);
        task2.dependOn(task3, task4);
        task3.dependOn(task5);
        taskManager.collectTasks(task1, task2, task3, task4, task5);
        taskManager.flatTasks();
        for (Task task : taskManager.getTasks()) {
            Log.e("Test", task.toString());
        }
    }
}

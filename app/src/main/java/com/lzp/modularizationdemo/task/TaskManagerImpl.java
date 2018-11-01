package com.lzp.modularizationdemo.task;

import android.support.v4.util.ArraySet;

import java.util.Arrays;

public class TaskManagerImpl implements TaskManager {
    private ArraySet<Task> mTasks;

    @Override
    public void collectTasks(Task... tasks) {
        if (mTasks == null) {
            mTasks = new ArraySet<>();
        }
        mTasks.addAll(Arrays.asList(tasks));
    }

    @Override
    public void flatTasks() {
        for (Task task : mTasks) {
            flatTask(task);
        }
    }

    @Override
    public void flatTask(Task task) {
        _flatTasks(task, task.getDepends());
    }

    private void _flatTasks(Task task, Task[] depends) {
        if (depends == null || depends.length == 0) {
            return;
        }
        for (Task t : depends) {
            //查找子节点的依赖集在父节点依赖集中存在的task
            Task[] repeatedTasks = task.filterDepends(t.getDepends());
            if (repeatedTasks != null && repeatedTasks.length > 0) {
                task.remove(repeatedTasks);
            }
            _flatTasks(t, t.getDepends());
        }
    }

    @Override
    public Task[] getTasks() {
        if (mTasks == null || mTasks.size() == 0) {
            return null;
        }
        Task[] rest = new Task[mTasks.size()];
        return mTasks.toArray(rest);
    }

    @Override
    public void exec(Task task) {
        if (task.hasDepend()) {
            //exec depend tasks
            Task[] depends = task.getDepends();
            for (Task d : depends) {
                if (!d.complete()) {
                    exec(d);
                }
            }
            //exec the dest task
            task.exec();
        } else {
            task.exec();
        }
    }

    @Override
    public void exec() {
        if (mTasks == null || mTasks.size() == 0) return;
        for (Task task : mTasks) {
            exec(task);
        }
    }
}

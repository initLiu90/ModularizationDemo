package com.lzp.core;

import android.support.v4.util.ArrayMap;

import com.lzp.core.manager.MTaskManager;
import com.lzp.core.mtask.MTask;
import com.lzp.library.util.MLog;

class MTaskManagerImpl implements MTaskManager {
    private ArrayMap<String, MTask> mTasks;

    @Override
    public void collectTasks(MTask... tasks) {
        if (mTasks == null) {
            mTasks = new ArrayMap<>();
        }
        if (tasks == null || tasks.length == 0) return;
        for (MTask task : tasks) {
            mTasks.put(task.name(), task);
        }
    }

    @Override
    public void configTasks() {
        if (mTasks == null || mTasks.size() == 0) return;
        for (MTask task : mTasks.values()) {
            task.config();
        }
    }

    @Override
    public void flatTasks() {
        if (mTasks == null || mTasks.size() == 0) return;
        for (MTask task : mTasks.values()) {
            flatTask(task);
        }
    }

    @Override
    public void flatTask(MTask task) {
        _flatTasks(task, task.getDepends());
    }

    private void _flatTasks(MTask task, String[] depends) {
        if (depends == null || depends.length == 0) return;
        for (String name : depends) {
            MTask tmp = getMTask(name);
            if (tmp == null) {
                MLog.e("Test", "MTaskManagerImpl", "_flatTasks could not find mtask:" + name + " from MTaskManager");
            } else {
                //查找子节点的依赖集在父节点依赖集中存在的task
                String[] repeatedTasks = task.filterDepends(tmp.getDepends());
                if (repeatedTasks != null && repeatedTasks.length > 0) {
                    task.remove(repeatedTasks);
                }
                _flatTasks(tmp, tmp.getDepends());
            }
        }
    }

    @Override
    public MTask getMTask(String taskName) {
        return mTasks.get(taskName);
    }

    @Override
    public MTask[] getTasks() {
        if (mTasks == null || mTasks.size() == 0) {
            return null;
        }
        MTask[] rest = new MTask[mTasks.size()];
        return mTasks.values().toArray(rest);
    }

    @Override
    public void exec(MTask task) {
        if (task.hasDepends()) {
            //exec depend tasks
            String[] depends = task.getDepends();
            for (String d : depends) {
                MTask t = getMTask(d);
                if (t == null) {
                    MLog.e("Test", "MTaskManagerImpl", "exec could not find mtask:" + d + " from MTaskManager");
                } else {
                    if (!t.complete()) {
                        exec(t);
                    }
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
        for (MTask task : mTasks.values()) {
            exec(task);
        }
    }

    @Override
    public void destry() {

    }
}

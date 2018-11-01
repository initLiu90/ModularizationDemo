package com.lzp.modularizationdemo.task;

import android.support.annotation.CallSuper;
import android.support.v4.util.ArraySet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MTask implements Task {
    protected boolean mCompleted = false;
    private ArraySet<Task> mDepends = new ArraySet<>();

    @Override
    public void dependOn(Task... tasks) {
        mDepends.addAll(Arrays.asList(tasks));
    }

    @Override
    public boolean remove(Task... tasks) {
        return mDepends.removeAll(Arrays.asList(tasks));
    }

    @Override
    public Task[] getDepends() {
        Task[] tasks = null;
        if (mDepends.size() == 0) {
            return null;
        } else {
            tasks = new Task[mDepends.size()];
            mDepends.toArray(tasks);
            return tasks;
        }
    }

    @Override
    public Task[] filterDepends(Task... tasks) {
        if (tasks == null || tasks.length == 0) {
            return null;
        }
        ArrayList<Task> list = new ArrayList<>();
        for (Task task : tasks) {
            if (mDepends.contains(task)) {
                list.add(task);
            }
        }
        Task[] result = new Task[list.size()];
        return list.toArray(result);
    }

    @Override
    public boolean hasDepend() {
        return !(mDepends == null || mDepends.size() == 0);
    }

    @Override
    public boolean complete() {
        return mCompleted;
    }

    @Override
    public void exec() {
        if (!mCompleted) {
            Log.e("Test", name() + " exec");
            mCompleted = true;
        }
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            Task task = (Task) obj;
            return this.name().equals(task.name());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name() + "->[");
        if (mDepends.size() <= 0) {
            return sb.append("]").toString();
        }
        for (Task task : mDepends) {
            sb.append(task.name()).append(",");
        }
        return sb.replace(sb.lastIndexOf(","), sb.length(), "]").toString();
    }
}

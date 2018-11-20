package com.lzp.core.mtask;

import android.support.v4.util.ArraySet;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Scheduler;

public abstract class AbsMTask implements MTask {
    /**
     * Indicate the task is executed
     */
    protected volatile boolean mCompleted = false;
    private ArraySet<String> mDepends = new ArraySet<>();
    private Scheduler mScheduler;

    @Override
    public final void setScheduler(Scheduler scheduler) {
        mScheduler = scheduler;
    }

    @Override
    public final Scheduler getScheduler() {
        return mScheduler;
    }

    @Override
    public final void dependsOn(String... tasks) {
        if (tasks == null || tasks.length == 0) return;
        mDepends.addAll(Arrays.asList(tasks));
    }

    @Override
    public final boolean remove(String... tasks) {
        return mDepends.removeAll(Arrays.asList(tasks));
    }

    @Override
    public final String[] getDepends() {
        String[] mTasks = new String[mDepends.size()];
        mDepends.toArray(mTasks);
        return mTasks;
    }

    @Override
    public final String[] filterDepends(String... tasks) {
        if (tasks == null || tasks.length == 0) {
            return null;
        }
        ArrayList<String> list = new ArrayList<>();
        for (String task : tasks) {
            if (mDepends.contains(task)) {
                list.add(task);
            }
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    @Override
    public final boolean hasDepends() {
        return !(mDepends == null || mDepends.size() == 0);
    }

    @Override
    public boolean complete() {
        return mCompleted;
    }

    /**
     * Task can add depends by override this method
     */
    @Override
    public void config() {
        //Do noting
    }

    /**
     * If this Task is not completed execute this Task and set completed to true
     */
    @Override
    final public void exec() {
        if (!mCompleted) {
            run();
            mCompleted = true;
        }
    }

    @Override
    public final int hashCode() {
        return name().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof MTask) {
            MTask task = (MTask) obj;
            return this.name().equals(task.name());
        }
        return false;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder(name() + "->[");
        if (mDepends.size() <= 0) {
            return sb.append("]").toString();
        }

        for (String task : mDepends) {
            sb.append(task).append(",");
        }
        return sb.replace(sb.lastIndexOf(","), sb.length(), "]").toString();
    }
}

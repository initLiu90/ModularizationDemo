package com.lzp.core.mtask;

import android.support.v4.util.ArraySet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbsMTask implements MTask {
    protected boolean mCompleted = false;
    private ArraySet<String> mDepends = new ArraySet<>();

    @Override
    public void dependsOn(String... tasks) {
        if (tasks == null || tasks.length == 0) return;
        mDepends.addAll(Arrays.asList(tasks));
    }

    @Override
    public boolean remove(String... tasks) {
        return mDepends.removeAll(Arrays.asList(tasks));
    }

    @Override
    public String[] getDepends() {
        String[] mTasks = new String[mDepends.size()];
        mDepends.toArray(mTasks);
        return mTasks;
    }

    @Override
    public String[] filterDepends(String... tasks) {
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
    public boolean hasDepends() {
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
        if (obj instanceof MTask) {
            MTask task = (MTask) obj;
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

        for (String task : mDepends) {
            sb.append(task).append(",");
        }
        return sb.replace(sb.lastIndexOf(","), sb.length(), "]").toString();
    }
}

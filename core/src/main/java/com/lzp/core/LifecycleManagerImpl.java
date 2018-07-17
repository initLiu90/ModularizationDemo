package com.lzp.core;

import android.support.v4.util.ArrayMap;

import com.lzp.core.manager.LifecycleManager;
import com.lzp.core.module.IModuleLifecycle;

class LifecycleManagerImpl implements LifecycleManager {
    private ArrayMap<String, IModuleLifecycle> moduleLifecycles = new ArrayMap<>();

    @Override
    public boolean registerModule(String clzName) {
        try {
            IModuleLifecycle moduleLifecycle = (IModuleLifecycle) Class.forName(clzName).newInstance();
            moduleLifecycles.put(clzName, moduleLifecycle);
            moduleLifecycle.onCreate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unregisterModule(String clzName) {
        IModuleLifecycle moduleLifecycle = moduleLifecycles.remove(clzName);
        if (moduleLifecycle != null) {
            moduleLifecycle.onStop();
            return true;
        }
        return false;
    }

    @Override
    public void destry() {
        moduleLifecycles.clear();
        moduleLifecycles = null;
    }
}

package com.lzp.core;

import android.support.v4.util.ArrayMap;

import com.lzp.core.manager.Manager;

public class AppRuntime {
    public static final int LIFECYCLE = 0;
    public static final int API = LIFECYCLE + 1;

    private final ArrayMap<Integer, Manager> managers = new ArrayMap<>();

    public Manager getManager(int name) {
        Manager manager = managers.get(name);
        if (manager != null) {
            return manager;
        }
        switch (name) {
            case LIFECYCLE:
                manager = new LifecycleManagerImpl();
                break;
            case API:
                manager = new ApiServiceManagerImpl();
                break;
        }
        if (manager != null) {
            addManager(name, manager);
        }
        return manager;
    }

    protected void addManager(int name, Manager manager) {
        managers.put(name, manager);
    }
}

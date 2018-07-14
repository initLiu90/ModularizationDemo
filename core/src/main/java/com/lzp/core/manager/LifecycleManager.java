package com.lzp.core.manager;


public interface LifecycleManager extends Manager {

    boolean registerModule(String clzName);

    boolean unregisterModule(String clzName);
}

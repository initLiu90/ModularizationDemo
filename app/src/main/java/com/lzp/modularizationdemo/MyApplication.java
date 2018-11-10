package com.lzp.modularizationdemo;

import com.lzp.core.AppRuntime;
import com.lzp.core.CrashHandler;
import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.LifecycleManager;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ((LifecycleManager) getAppRuntime().getManager(AppRuntime.LIFECYCLE)).registerModule("com.lzp.module1.Module1Lifecycle");
        ((LifecycleManager) getAppRuntime().getManager(AppRuntime.LIFECYCLE)).registerModule("com.lzp.module2.Module2Lifecycle");
    }

    @Override
    public boolean monitoNetwork() {
        return true;
    }

    @Override
    public CrashHandler getCrashHandler() {
        return new CrashHandler();
    }

    @Override
    public AppRuntime createAppRuntime() {
        return new AppRuntime();
    }
}

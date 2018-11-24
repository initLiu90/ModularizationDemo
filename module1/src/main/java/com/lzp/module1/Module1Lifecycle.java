package com.lzp.module1;

import com.lzp.api.module1.IMode1;
import com.lzp.core.AppRuntime;
import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.ApiServiceManager;
import com.lzp.core.manager.MTaskManager;
import com.lzp.core.module.IModuleLifecycle;
import com.lzp.core.mtask.MTask;

public class Module1Lifecycle implements IModuleLifecycle {
    @Override
    public void onCreate() {
        ((ApiServiceManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.API)).addApiService(IMode1.class, Mode1ServiceImpl.class);
        ((MTaskManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.MTASK)).collectTasks(new MT_module1(), new MT_module2(), new MT_module3());
    }

    @Override
    public void onStop() {
        ((ApiServiceManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.API)).removeApiService(IMode1.class);
    }
}

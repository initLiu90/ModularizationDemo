package com.lzp.module2;

import com.lzp.api.module2.IMode2;
import com.lzp.core.AppRuntime;
import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.ApiServiceManager;
import com.lzp.core.manager.MTaskManager;
import com.lzp.core.module.IModuleLifecycle;

public class Module2Lifecycle implements IModuleLifecycle {
    @Override
    public void onCreate() {
        ((ApiServiceManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.API)).addApiService(IMode2.class, Mode2ServiceImpl.class);
        ((MTaskManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.MTASK)).collectTasks(new MT_module0(), new MT_module4(), new MT_module5());
    }

    @Override
    public void onStop() {
        ((ApiServiceManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.API)).removeApiService(IMode2.class);
    }
}

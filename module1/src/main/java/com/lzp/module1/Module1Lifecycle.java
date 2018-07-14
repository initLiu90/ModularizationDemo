package com.lzp.module1;

import com.lzp.api.module1.IMode1;
import com.lzp.core.AppRuntime;
import com.lzp.core.BaseApplication;
import com.lzp.core.manager.ApiServiceManager;
import com.lzp.core.IModuleLifecycle;

public class Module1Lifecycle implements IModuleLifecycle {
    @Override
    public void onCreate() {
        ((ApiServiceManager) BaseApplication.sApplication.getAppRuntime().getManager(AppRuntime.API)).addApiService(IMode1.class, Mode1ServiceImpl.class);
    }

    @Override
    public void onStop() {
        ((ApiServiceManager) BaseApplication.sApplication.getAppRuntime().getManager(AppRuntime.API)).removeApiService(IMode1.class);
    }
}

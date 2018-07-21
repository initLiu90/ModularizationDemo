package com.lzp.login;

import com.lzp.api.login.ILogin;
import com.lzp.core.AppRuntime;
import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.ApiServiceManager;
import com.lzp.core.module.IModuleLifecycle;

public class LoginModuleLifecycle implements IModuleLifecycle {
    @Override
    public void onCreate() {
        ((ApiServiceManager) BaseApplication.getApplication()
                .getAppRuntime()
                .getManager(AppRuntime.API)).addApiService(ILogin.class, LoginApiImpl.class);
    }

    @Override
    public void onStop() {
        ((ApiServiceManager) BaseApplication.getApplication()
                .getAppRuntime()
                .getManager(AppRuntime.API)).removeApiService(ILogin.class);
    }
}

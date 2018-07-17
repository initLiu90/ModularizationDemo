package com.lzp.core.base;

import android.app.Application;

import com.lzp.core.AppRuntime;

public abstract class BaseApplication extends Application {
    private AppRuntime mAppRuntime;
    private static BaseApplication sApplication;

    public static BaseApplication getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mAppRuntime = createAppRuntime();
    }

    public abstract AppRuntime createAppRuntime();

    public AppRuntime getAppRuntime() {
        return mAppRuntime;
    }
}

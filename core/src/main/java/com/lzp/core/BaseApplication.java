package com.lzp.core;

import android.app.Application;

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

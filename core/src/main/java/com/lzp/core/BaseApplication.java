package com.lzp.core;

import android.app.Application;

public abstract class BaseApplication extends Application {
    protected AppRuntime mAppRuntime;
    public static BaseApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mAppRuntime = new AppRuntime();
    }

    public AppRuntime getAppRuntime() {
        return mAppRuntime;
    }
}

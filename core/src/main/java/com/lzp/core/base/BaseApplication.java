package com.lzp.core.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.lzp.core.AppRuntime;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseApplication extends Application {
    private AppRuntime mAppRuntime;
    private static BaseApplication sApplication;
    private List<WeakReference<BaseActivity>> mBackgroundActivitys = new ArrayList<>();
    private Handler mHander = new Handler(Looper.getMainLooper());

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

    void addBaseActivity(BaseActivity activity) {
        mBackgroundActivitys.add(0, new WeakReference<BaseActivity>(activity));
    }

    void removeBaseActivity(BaseActivity activity) {
        mBackgroundActivitys.remove(new WeakReference<BaseActivity>(activity));
    }

    // TODO: 2018/7/17 add finish all activity method

    // TODO: 2018/7/17 add exit method
}

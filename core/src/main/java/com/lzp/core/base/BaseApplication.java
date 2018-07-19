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
    private List<WeakReference<BaseActivity>> mActivitys = new ArrayList<>();
    private Handler mHander = new Handler(Looper.getMainLooper());

    public static BaseApplication getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        setCrashHandler();
        mAppRuntime = createAppRuntime();
    }

    /**
     * 设置AppRuntime，
     * 默认实现为{@link AppRuntime}
     * @return
     */
    public abstract AppRuntime createAppRuntime();

    public AppRuntime getAppRuntime() {
        return mAppRuntime;
    }

    /**
     * 设置UncaughtExceptionHandler，
     * 默认实现为{@link com.lzp.core.CrashHandler}
     */
    public abstract void setCrashHandler();

    void addBaseActivity(BaseActivity activity) {
        mActivitys.add(0, new WeakReference<BaseActivity>(activity));
    }

    void removeBaseActivity(BaseActivity activity) {
        mActivitys.remove(new WeakReference<BaseActivity>(activity));
    }

    public List<WeakReference<BaseActivity>> getActivitys() {
        return mActivitys;
    }

    public void closeAllActivity() {
        int len = mActivitys.size();
        for (int i = len; i >= 0; i--) {
            WeakReference<BaseActivity> ref = mActivitys.get(i);
            BaseActivity activity = ref == null ? null : ref.get();
            if (activity == null) {
                mActivitys.remove(i);
            } else if (!activity.isFinishing()) {
                activity.finish();
            } else {
                mActivitys.remove(i);
            }
        }
    }
}

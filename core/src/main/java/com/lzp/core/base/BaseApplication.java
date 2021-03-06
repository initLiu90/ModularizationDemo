package com.lzp.core.base;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.lzp.core.AppRuntime;
import com.lzp.core.CrashHandler;
import com.lzp.core.NetStateReceiver;
import com.lzp.core.manager.NetworkManager;
import com.lzp.library.util.MLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
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
        Thread.setDefaultUncaughtExceptionHandler(getCrashHandler());
        mAppRuntime = createAppRuntime();
        if (monitoNetwork()) {
            ((NetworkManager) getAppRuntime().getManager(AppRuntime.NETWORK)).registerNetMonitor();
        }
    }

    /**
     * 设置AppRuntime，
     * 默认实现为{@link AppRuntime}
     *
     * @return
     */
    protected abstract AppRuntime createAppRuntime();

    public AppRuntime getAppRuntime() {
        return mAppRuntime;
    }

    /**
     * 是否需要监听网络变化
     *
     * @return
     */
    public abstract boolean monitoNetwork();

    /**
     * 设置UncaughtExceptionHandler，
     * 默认实现为{@link com.lzp.core.CrashHandler}
     */
    public abstract CrashHandler getCrashHandler();

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
        MLog.w("Test", "BaseApplication", "closeAllActivity len=" + len);
        for (int i = len - 1; i >= 0; i--) {
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

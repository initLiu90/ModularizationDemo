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
import com.lzp.core.NetStateReceiver;

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
        regisetNetworkMonitorer();
    }

    /**
     * 设置AppRuntime，
     * 默认实现为{@link AppRuntime}
     *
     * @return
     */
    public abstract AppRuntime createAppRuntime();

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

    private void regisetNetworkMonitorer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (monitoNetwork()) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                manager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                    }
                });
            }
        }
    }
}

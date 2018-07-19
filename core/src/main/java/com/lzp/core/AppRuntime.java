package com.lzp.core;

import android.support.v4.util.ArrayMap;

import com.lzp.core.base.BaseActivity;
import com.lzp.core.base.BaseApplication;
import com.lzp.core.observer.BusinessObserver;
import com.lzp.core.manager.Manager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AppRuntime {
    public static final int LIFECYCLE = 0;
    public static final int API = LIFECYCLE + 1;

    private final ArrayMap<Integer, Manager> managers = new ArrayMap<>();
    private List<BusinessObserver> mObservers = new ArrayList<>();
    private BaseApplication mApp;

    public AppRuntime() {
        mApp = BaseApplication.getApplication();
    }

    public Manager getManager(int name) {
        Manager manager = managers.get(name);
        if (manager != null) {
            return manager;
        }
        switch (name) {
            case LIFECYCLE:
                manager = new LifecycleManagerImpl();
                break;
            case API:
                manager = new ApiServiceManagerImpl();
                break;
        }
        if (manager != null) {
            addManager(name, manager);
        }
        return manager;
    }

    protected void addManager(int name, Manager manager) {
        managers.put(name, manager);
    }

    public void addObserver(BusinessObserver observer) {
        if (observer == null) throw new RuntimeException("invalid parameter observer is null");
        mObservers.add(observer);
    }

    public void removeObserver(BusinessObserver observer) {
        if (observer == null) throw new RuntimeException("invalid parameter observer is null");
        mObservers.remove(observer);
    }

    /**
     * 通知类型为observerClz以及observerClz子类型 的observer
     *
     * @param observerClz
     * @param isSuccess
     * @param value
     */
    public void notifyObserver(Class observerClz, boolean isSuccess, Object value) {
        for (BusinessObserver observer : mObservers) {
            if (observerClz.isAssignableFrom(observer.getClass())) {
                observer.onUpdate(isSuccess, value);
            }
        }
    }

    /**
     * 通知当前栈顶Activity，已登录
     */
    public void onLogin() {
        WeakReference<BaseActivity> ref = mApp.getActivitys().get(0);
        BaseActivity activity = ref == null ? null : ref.get();
        if (activity != null && !activity.isFinishing()) {
            activity.onLogin();
        }
    }

    /**
     * 通知当前栈顶Activity，已退出登录
     */
    public void onLogout() {
        WeakReference<BaseActivity> ref = mApp.getActivitys().get(0);
        BaseActivity activity = ref == null ? null : ref.get();
        if (activity != null && !activity.isFinishing()) {
            activity.onLogout();
        }
    }
}

package com.lzp.core;

import com.lzp.core.base.BaseApplication;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private boolean isCrashing = false;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        synchronized (this) {
            if (isCrashing)
                return;
            isCrashing = true;
        }
        BaseApplication.getApplication().closeAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

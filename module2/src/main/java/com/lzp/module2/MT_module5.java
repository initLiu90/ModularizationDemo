package com.lzp.module2;

import android.util.Log;

import com.lzp.core.mtask.AbsMTask;

public class MT_module5 extends AbsMTask {
    @Override
    public String name() {
        return MT_module5.class.getName();
    }

    @Override
    public void config() {
        dependsOn("com.lzp.module1.MT_module1");
        dependsOn("com.lzp.module1.MT_module2");
    }

    @Override
    public void run() {
        Log.e("Test", name() + " exec");
    }
}

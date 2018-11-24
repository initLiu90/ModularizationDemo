package com.lzp.module2;

import android.util.Log;

import com.lzp.core.mtask.AbsMTask;

public class MT_module0 extends AbsMTask {
    @Override
    public String name() {
        return MT_module0.class.getName();
    }
    @Override
    public void run() {
        Log.e("Test", name() + " exec on thread:" + Thread.currentThread().getName());
        Log.e("Test", name() + " exec end");
    }
}

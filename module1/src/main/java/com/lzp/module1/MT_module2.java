package com.lzp.module1;

import com.lzp.core.mtask.AbsMTask;

import io.reactivex.schedulers.Schedulers;

public class MT_module2 extends AbsMTask {
    @Override
    public String name() {
        return MT_module2.class.getName();
    }

    @Override
    public void config() {
        setScheduler(Schedulers.computation());
        dependsOn("com.lzp.module2.MT_module0");
    }
}

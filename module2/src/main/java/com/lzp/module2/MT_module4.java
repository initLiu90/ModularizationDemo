package com.lzp.module2;

import com.lzp.core.mtask.AbsMTask;

public class MT_module4 extends AbsMTask {
    @Override
    public String name() {
        return MT_module4.class.getName();
    }

    @Override
    public void config() {
        dependsOn("com.lzp.module1.MT_module1");
    }
}

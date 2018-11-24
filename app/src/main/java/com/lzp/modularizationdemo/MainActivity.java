package com.lzp.modularizationdemo;

import android.os.Bundle;
import android.view.View;

import com.lzp.core.base.BaseActivity;
import com.lzp.core.mtask.MTaskList;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    @Override
    protected boolean immersiveStatusBarEnabled() {
        return true;
    }

    private void test() {
        MTaskList taskList = null;

    }
}

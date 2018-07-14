package com.lzp.modularizationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lzp.api.module1.IMode1;
import com.lzp.api.module2.IMode2;
import com.lzp.core.AppRuntime;
import com.lzp.core.manager.ApiServiceManager;

public class MainActivity extends AppCompatActivity {

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

    private void test() {
        IMode1 api1 = ((ApiServiceManager) MyApplication.sApplication.getAppRuntime().getManager(AppRuntime.API)).getApiService(IMode1.class);
        Log.e("Test", api1.getMode1Name());

        IMode2 api2 = ((ApiServiceManager) MyApplication.sApplication.getAppRuntime().getManager(AppRuntime.API)).getApiService(IMode2.class);
        Log.e("Test", api2.getSchool());
    }
}

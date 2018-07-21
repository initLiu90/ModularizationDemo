package com.lzp.modularizationdemo;

import android.os.Bundle;
import android.view.View;

import com.lzp.api.login.ILogin;
import com.lzp.core.AppRuntime;
import com.lzp.core.base.BaseActivity;
import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.ApiServiceManager;
import com.lzp.library.util.MLog;

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

    private void test() {
        ILogin iLogin = ((ApiServiceManager) BaseApplication.getApplication()
                .getAppRuntime()
                .getManager(AppRuntime.API)).getApiService(ILogin.class);
        if (!iLogin.isLogin()) {
            iLogin.login(this);
        } else {
            MLog.e("Test", "MainActivity", "已登录 " + iLogin.getAccount());
        }
    }
}

package com.lzp.core.base;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lzp.core.AppRuntime;

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseApplication mApp;
    protected AppRuntime mRuntime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = BaseApplication.getApplication();
        mRuntime = mApp.getAppRuntime();
        mApp.addBaseActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApp.removeBaseActivity(this);
    }

    /**
     * use {@link BaseActivity#getSupportFragmentManager BaseActivity.getSupportFragmentManager()} instead
     * @return FragmentManager
     */
    @Deprecated
    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }
}

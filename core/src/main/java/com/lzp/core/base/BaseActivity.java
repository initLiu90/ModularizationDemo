package com.lzp.core.base;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.lzp.core.AppRuntime;

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseApplication mApp;
    protected AppRuntime mRuntime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (enableImmersiveStatusBar()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        mApp = BaseApplication.getApplication();
        mRuntime = mApp.getAppRuntime();
        mApp.addBaseActivity(this);
    }

    protected boolean enableImmersiveStatusBar() {
        return false;
    }

    /**
     * 隐藏底部导航栏
     */
    protected void hideBottomNavigation() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    /**
     * use {@link BaseActivity#getSupportFragmentManager BaseActivity.getSupportFragmentManager()} instead
     *
     * @return FragmentManager
     */
    @Deprecated
    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }

    /**
     * 登录成功回调
     */
    public void onLogin() {

    }

    /**
     * 退出登录回调
     */
    public void onLogout() {

    }

    /**
     * 无网络时回调
     */
    public void onNetworkClosed() {

    }

    /**
     * 网络已连接
     */
    public void onNetworkConnected() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApp.removeBaseActivity(this);
    }
}

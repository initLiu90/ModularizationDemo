package com.lzp.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.NetworkManager;
import com.lzp.library.util.MLog;

/**
 * api<24时适用此广播
 * 必须在AndroidManifest文件中声明 android.permission.ACCESS_NETWORK_STATE
 * api>=24时在自定义的Application中重写方法
 *  @Override
 *  public boolean monitoNetwork() {
 *      return true;
 *  }
 */
public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean connect;
            if (networkInfo != null && networkInfo.isConnected()) {
                connect = true;
            } else {
                connect = false;
            }

            boolean changed = ((NetworkManager) BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.NETWORK)).updateConnectState(connect);
            MLog.i("Test", TAG, "receive CONNECTIVITY_ACTION connect:" + connect + ",changed:" + changed);
            if (changed) {
                if (connect) {
                    BaseApplication.getApplication().getAppRuntime().onNetworkConnected();
                } else {
                    BaseApplication.getApplication().getAppRuntime().onNetworkClosed();
                }
            }
        }
    }
}

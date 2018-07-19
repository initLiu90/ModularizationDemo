package com.lzp.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.NetworkManager;
import com.lzp.library.util.MLog;

public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            MLog.i(TAG, "receive CONNECTIVITY_ACTION");
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean connect;
            if (networkInfo != null && networkInfo.isConnected()) {
                connect = true;
            }else{
                connect = false;
            }

            boolean changed = ((NetworkManager)BaseApplication.getApplication().getAppRuntime().getManager(AppRuntime.NETWORK)).updateConnectState(connect);
            if (networkInfo != null) {
                if (!networkInfo.isConnected()) {
                    BaseApplication.getApplication().getAppRuntime().onNetworkClosed();
                }
            }
        }
    }
}

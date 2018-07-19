package com.lzp.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.NetworkManager;
import com.lzp.library.util.MLog;

public class NetworkManagerImpl implements NetworkManager {
    private boolean mConnect;
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    public NetworkManagerImpl() {
        mConnect = isConnect();
    }

    @Override
    public boolean isConnect() {
        ConnectivityManager manager = (ConnectivityManager) BaseApplication.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateConnectState(boolean connect) {
        boolean changed = false;
        if (mConnect != connect) {
            changed = true;
        }
        mConnect = connect;
        return changed;
    }

    @Override
    public void registerNetMonitor() {
        MLog.i("Test", "NetworkManagerImpl", "regisetNetworkMonitorer sdk_int=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && mNetworkCallback == null) {
            ConnectivityManager manager = (ConnectivityManager) BaseApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            mNetworkCallback = new MyNetworkCallback();
            manager.registerNetworkCallback(builder.build(), mNetworkCallback);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private class MyNetworkCallback extends ConnectivityManager.NetworkCallback {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            boolean changed = updateConnectState(true);
            MLog.i("Test", "NetworkManagerImpl", "onAvailable changed:" + changed);
            if (changed) {
                BaseApplication.getApplication().getAppRuntime().onNetworkConnected();
            }
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            boolean changed = updateConnectState(false);
            MLog.i("Test", "NetworkManagerImpl", "onLost changed:" + changed);
            if (changed) {
                BaseApplication.getApplication().getAppRuntime().onNetworkClosed();
            }
        }
    }

    @Override
    public void destry() {
        if (mNetworkCallback != null) {
            ConnectivityManager manager = (ConnectivityManager) BaseApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            manager.unregisterNetworkCallback(mNetworkCallback);
        }
    }
}


package com.lzp.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lzp.core.base.BaseApplication;
import com.lzp.core.manager.NetworkManager;

public class NetworkManagerImpl implements NetworkManager {
    private boolean mConnect;

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
    public void destry() {

    }
}


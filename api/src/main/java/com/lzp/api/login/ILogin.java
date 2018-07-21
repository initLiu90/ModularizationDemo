package com.lzp.api.login;

import android.app.Activity;

public interface ILogin {
    boolean isLogin();

    String getAccount();

    void login(Activity context);
}

package com.lzp.login;

import android.app.Activity;
import android.content.Intent;

import com.lzp.api.login.ILogin;
import com.lzp.login.activity.LoginActivity;
import com.lzp.login.utils.UserUtil;

public class LoginApiImpl implements ILogin {
    @Override
    public boolean isLogin() {
        return UserUtil.getWJLoginHelper().hasLogin();
    }

    @Override
    public String getAccount() {
        return UserUtil.getWJLoginHelper().getUserAccount();
    }

    @Override
    public void login(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}

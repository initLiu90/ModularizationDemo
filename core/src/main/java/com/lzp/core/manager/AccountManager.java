package com.lzp.core.manager;

public abstract class AccountManager implements Manager {
    public abstract String getAccount();

    public abstract String getPin();

    public abstract boolean checkLoginState();

    public abstract boolean refreshLoginState();
}

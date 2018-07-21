package com.lzp.login.processor;

import jd.wjlogin_sdk.common.listener.OnLoginCallback;
import jd.wjlogin_sdk.model.ErrorResult;

/**
 * 用户名密码登录以及微信登录
 */
public class CommonLoginCallback extends OnLoginCallback {
    public CommonLoginCallback(CommonLoginFailProcessor failProcessor) {
        super(failProcessor);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(ErrorResult errorResult) {

    }
}

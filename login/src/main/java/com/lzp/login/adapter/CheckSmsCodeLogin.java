package com.lzp.login.adapter;

import com.lzp.login.utils.UserUtil;

import jd.wjlogin_sdk.common.listener.OnCommonCallback;
import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.FailResult;

/**
 * 验证短信验证码并登录
 */
public class CheckSmsCodeLogin extends Adapter {
    private Callback mCallback;

    @Override
    public void process(Argument arg, Callback callback) {
        mCallback = callback;
        final String phone = arg.arg1;
        final String smscode = arg.arg2;

        UserUtil.getWJLoginHelper()
                .checkMsgCodeForPhoneNumLogin(phone, smscode, new OnCommonCallback() {
                    @Override
                    public void onSuccess() {
                        if (mCallback != null) {
                            mCallback.onSuccess(null);
                        }
                    }

                    @Override
                    public void onError(ErrorResult errorResult) {
                        if (mCallback != null) {
                            mCallback.onError(errorResult.getErrorMsg());
                        }
                    }

                    @Override
                    public void onFail(FailResult failResult) {
                        if (mCallback != null) {
                            mCallback.onError(failResult.getMessage());
                        }
                    }
                });
    }

    @Override
    public void finish() {
        mCallback = null;
    }
}

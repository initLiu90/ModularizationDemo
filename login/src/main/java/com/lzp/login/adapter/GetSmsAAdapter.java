package com.lzp.login.adapter;

import com.lzp.library.util.MLog;
import com.lzp.login.utils.Constansts;
import com.lzp.login.utils.UserUtil;

import jd.wjlogin_sdk.common.listener.OnDataCallback;
import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.FailResult;
import jd.wjlogin_sdk.model.SuccessResult;
import jd.wjlogin_sdk.util.ReplyCode;

/**
 * 获取短信验证码
 */
public class GetSmsAAdapter extends Adapter {
    private static final String TAG = "GetSmsAAdapter";

    private Callback mCallback;

    @Override
    public void process(Argument arg, Callback callback) {
        mCallback = callback;
        final String phone = arg.arg1;

        UserUtil.getWJLoginHelper()
                .sendMsgCodeForPhoneNumLogin(phone, new OnDataCallback<SuccessResult>() {
                    @Override
                    public void onSuccess(SuccessResult successResult) {
                        if (mCallback != null) {
                            mCallback.onSuccess(successResult.getIntVal());
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
                        if (failResult.getReplyCode() == ReplyCode.reply0x17) {
                            // 提交过于频繁。是否需要禁止用户操作，由客户端决定
                            //刷新显示到计时failResult.getDwLimitTimet
                            int time = failResult.getIntVal();
                            MLog.e("Test", TAG, "提交过于频繁=" + time);
                            if (mCallback != null) {
                                mCallback.onFail(Constansts.FAIL_TYPE_SMS_CODE2, String.valueOf(time), null);
                            }
                        } else if (failResult.getReplyCode() == ReplyCode.reply0x1f) {
                            //短信已发送，请勿重复提交。 是否需要禁止用户操作，由客户端决定
                            //刷新显示到计时failResult.getDwLimitTimet
                            int time = failResult.getIntVal();
                            MLog.e("Test", TAG, "短信已发送=" + time);
                            if (mCallback != null) {
                                mCallback.onFail(Constansts.FAIL_TYPE_SMS_CODE3, String.valueOf(time), null);
                            }
                        } else {
                            if (mCallback != null) {
                                mCallback.onFail(Constansts.FAIL_TYPE_SMS_CODE1, failResult.getMessage(), null);
                            }
                        }
                    }
                });
    }

    @Override
    public void finish() {
        mCallback = null;
    }
}

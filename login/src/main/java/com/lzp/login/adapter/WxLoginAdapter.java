package com.lzp.login.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.lzp.library.util.MLog;
import com.lzp.login.activity.WebActivity;
import com.lzp.login.processor.CommonLoginCallback;
import com.lzp.login.processor.CommonLoginFailProcessor;
import com.lzp.login.utils.Constansts;
import com.lzp.login.utils.UserUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.WXTokenInfo;

public class WxLoginAdapter extends Adapter {
    private static final String TAG = "WxLoginAdapter";

    private Callback mCallback;
    private IWXAPI api;
    private WxLoginAdapter.WXLoginResponseReceiver mWXLoginResponseReceiver;
    private Context mContext;
    private long thirdLoginLastclickTime = -1;//防止连击微信、qq登录重复调起app的时候使用。

    public WxLoginAdapter(Context context) {
        mContext = context;
        api = WXAPIFactory.createWXAPI(context.getApplicationContext(), Constansts.WXAPP_ID, false);
        api.registerApp(Constansts.WXAPP_ID);
        registeWxLoginReceiver();
    }

    @Override
    public void process(Argument arg, Callback callback) {
        mCallback = callback;
        wxAuthLogin();

    }

    /**
     * 微信授权登陆
     */
    private void wxAuthLogin() {
        long cur = System.currentTimeMillis();
        if (thirdLoginLastclickTime < 0 || cur - thirdLoginLastclickTime > 1000) {
            //拉起微信app,获取code
            wxLogin();
            thirdLoginLastclickTime = cur;
        }
    }

    //拉起微信登录
    private void wxLogin() {
        if (!checkWX()) {
            return;
        }
        final SendAuth.Req pReq = new SendAuth.Req();
        pReq.scope = "snsapi_userinfo";
        pReq.state = "jdlogin";
        api.sendReq(pReq);
    }

    /**
     * 检查是否安装微信或者微信版本过低
     *
     * @return
     */
    private boolean checkWX() {
        if (api.getWXAppSupportAPI() <= 0) {
            return false;
        }
        return true;
    }

    private class WXLoginResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nErrCode = intent.getIntExtra("errCode",
                    ConstantsAPI.COMMAND_UNKNOWN);
            int nType = intent.getIntExtra("type", -1);
            String strCode = intent.getStringExtra("code");
            String strState = intent.getStringExtra("state");

            MLog.e("Test", TAG, "WXLoginResponseReceiver nErrCode=" + nErrCode + ",nType=" + nType + ",strCode=" + strCode + ",strState=" + strState);
            if (nType == ConstantsAPI.COMMAND_SENDAUTH) {
                if (nErrCode == BaseResp.ErrCode.ERR_OK) {
                    onWXLoginResponse(strCode, strState);
                }
            }
        }
    }

    public void onWXLoginResponse(String code, String state) {
        //拿微信返回的code去后台检验
        try {
            WXTokenInfo wxTokenInfo = new WXTokenInfo();
            wxTokenInfo.setCode(code);
            UserUtil.getWJLoginHelper()
                    .wxLogin(wxTokenInfo, new CommonLoginCallback(new CommonLoginFailProcessor(CommonLoginFailProcessor.TYPE_UNION) {
                        @Override
                        public void onFail(int failType, byte originalCode, String message) {
                            if (mCallback != null) {
                                mCallback.onFail(Constansts.FAIL_TYPE_DEFAULE, message, null);
                            }
                        }

                        @Override
                        public void onAccountLocked(byte originalCode, String message) {
                            if (mCallback != null) {
                                mCallback.onFail(Constansts.FAIL_TYPE_ACCOUNT_LOCK, message, null);
                            }
                        }

                        @Override
                        public void toMPage(String url, String token, String message) {
                            if (mCallback != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WebActivity.KEY_URL, url);
                                bundle.putString(WebActivity.KEY_TAG, token);
                                mCallback.onTranslate(Constansts.TRANSLATE_TYPE_WEB, bundle);
                            }
                        }

                        @Override
                        public void sendSms(String url, String token) {
                            if (mCallback != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WebActivity.KEY_URL, url);
                                bundle.putString(WebActivity.KEY_TAG, token);
                                mCallback.onTranslate(Constansts.TRANSLATE_TYPE_WEB, bundle);
                            }
                        }

                        @Override
                        public void onBindJDAccount(String url, String token, String message) {
                            if (mCallback != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WebActivity.KEY_URL, url);
                                bundle.putString(WebActivity.KEY_TAG, token);
                                mCallback.onTranslate(Constansts.TRANSLATE_TYPE_WEB, bundle);
                            }
                        }
                    }) {
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
                    });
        } catch (Exception e) {
            if (mCallback != null) {
                mCallback.onError(e.getMessage());
            }
        }
    }


    public void registeWxLoginReceiver() {
        if (mWXLoginResponseReceiver == null) {
            mWXLoginResponseReceiver = new WXLoginResponseReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constansts.BROADCAST_FROM_WXLOGIN);
            mContext.registerReceiver(mWXLoginResponseReceiver, filter,
                    Constansts.SLEF_BROADCAST_PERMISSION, null);
        }
    }

    public void unRegisteWxLoginReceiver() {
        if (mWXLoginResponseReceiver != null) {
            mContext.unregisterReceiver(mWXLoginResponseReceiver);
            mWXLoginResponseReceiver = null;
        }
    }

    @Override
    public void finish() {
        unRegisteWxLoginReceiver();
        mContext = null;
        mCallback = null;
    }
}

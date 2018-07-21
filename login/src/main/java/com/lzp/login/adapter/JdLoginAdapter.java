package com.lzp.login.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.lzp.library.util.MLog;
import com.lzp.login.utils.UserUtil;

import java.lang.ref.WeakReference;

import jd.wjlogin_sdk.common.listener.OnCommonCallback;
import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.FailResult;

public class JdLoginAdapter extends Adapter {
    private static final String TAG = "JdLoginAdapter";
    private static final String RETURN_URL = "jdLogin.openApp.jdMobile://virtual?action=thirdPartyLogin";
    private static final String BROADCAST_FROM_JINGDONGLOGIN = "com.jd.wjloginclient.jdloginreceiver";

    private Callback mCallback;
    private Context mContext;

    private long curretRegistjdToken;
    private static long registjdToken;
    private String thirdToken; //第三方app调用京东商城登录token

    private JdReceiver mJdResponseReceiver;

    public JdLoginAdapter(Context context) {
        mContext = context;
        if (isJDAppSupport()) {
            registeJdLoginReceiver();
        }
    }

    @Override
    public void process(Argument arg, Callback callback) {
        mCallback = callback;
        if (!isJDAppSupport()) {
            callback.onError("请安装最新版JdApp");
        } else {
            //请求网络，验证签名
            UserUtil.getWJLoginHelper()
                    .openJDApp(mContext.getApplicationContext(), RETURN_URL, new OnCommonCallback() {
                        @Override
                        public void onSuccess() {
                            // sdk 签名验证成功后，拉起京东商城app。客户端啥都不用做。sdk里面已经拉起
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
    }

    /**
     * 检查京东app是否支持第三方登录
     *
     * @return
     */
    private boolean isJDAppSupport() {
        return UserUtil.getWJLoginHelper().isJDAppSupportAPI();
    }

    private void registeJdLoginReceiver() {
        if (mJdResponseReceiver == null) {
            mJdResponseReceiver = new JdReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BROADCAST_FROM_JINGDONGLOGIN);
            curretRegistjdToken = registjdToken = System.currentTimeMillis();
            mContext.registerReceiver(mJdResponseReceiver, filter);
        }
    }

    public void unRgeisteJdLoginReceiver() {
        if (mJdResponseReceiver != null) {
            mContext.unregisterReceiver(mJdResponseReceiver);
            mJdResponseReceiver = null;
        }
    }

    private class JdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (curretRegistjdToken != registjdToken) {
                return;
            }
            thirdToken = intent.getStringExtra("thirdToken");
            if (!TextUtils.isEmpty(thirdToken)) {
                toLoginByToken();
            } else {
                if (mCallback != null) {
                    mCallback.onError("授权登录失败");
                }
            }
        }
    }

    private void toLoginByToken() {
        //拿到token，去请求网络，获取a2 pin
        UserUtil.getWJLoginHelper().loginWithToken(thirdToken, new OnCommonCallback() {
            @Override
            public void onSuccess() {
                MLog.e("Test", TAG, "授权登录成功");
                thirdToken = null;
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
        unRgeisteJdLoginReceiver();
        mCallback = null;
        mContext = null;
    }
}

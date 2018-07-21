package com.lzp.login.adapter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.lzp.core.base.BaseApplication;
import com.lzp.library.util.MLog;
import com.lzp.login.activity.WebActivity;
import com.lzp.login.processor.CommonLoginCallback;
import com.lzp.login.processor.CommonLoginFailProcessor;
import com.lzp.login.utils.Constansts;
import com.lzp.login.utils.UserUtil;

import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.PicDataInfo;

public class PwdLoginAdapter extends Adapter {
    private static final String TAG = "PwdLoginAdapter";

    private Callback mCallback;

    @Override
    public void process(Argument arg, Callback callback) {
        mCallback = callback;
        final String account = arg.arg1;
        String pwd = arg.arg2;
        PicDataInfo picDataInfo = (PicDataInfo) arg.arg3;

        UserUtil.getWJLoginHelper()
                .JDLoginWithPassword(account, pwd, picDataInfo
                        , new CommonLoginCallback(new CommonLoginFailProcessor(CommonLoginFailProcessor.TYPE_PWD) {
                            @Override
                            public void onFail(int failType, byte originalCode, String message) {
                                MLog.e("Test", TAG, "onFail failType=" + failType + ",originalCode=" + originalCode + ",message=" + message);
                                if (mCallback != null) {
                                    mCallback.onFail(Constansts.FAIL_TYPE_DEFAULE, message, null);
                                }
                            }

                            @Override
                            public void onAccountLocked(byte originalCode, String message) {
                                MLog.e("Test", TAG, "onAccountLocked=" + message);
                                if (mCallback != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(WebActivity.KEY_URL, getFengkongUri(account));
                                    bundle.putBoolean(WebActivity.KEY_REGIST, true);
                                    mCallback.onTranslate(Constansts.TRANSLATE_TYPE_WEB, bundle);
                                }
                            }

                            @Override
                            public void toMPage(String url, String token, String message) {
                                MLog.e("Test", TAG, "toMPage url=" + url + ",message=" + message);
                                if (mCallback != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(WebActivity.KEY_URL, url);
                                    mCallback.onTranslate(Constansts.TRANSLATE_TYPE_WEB, bundle);
                                }
                            }

                            @Override
                            public void sendSms(String url, String token) {
                                MLog.e("Test", TAG, "sendSms url=" + url + ",token=" + token);
                                if (mCallback != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(WebActivity.KEY_URL, url);
                                    bundle.putString(WebActivity.KEY_TAG, "sms");
                                    mCallback.onTranslate(Constansts.TRANSLATE_TYPE_WEB, bundle);
                                }
                            }

                            @Override
                            public void onHidePicData() {
                                MLog.e("Test", TAG, "onHidePicData");
                                if (mCallback != null) {
                                    mCallback.onFail(Constansts.FAIL_TYPE_HIDE_PICDATA, null, null);
                                }
                            }

                            @Override
                            public void onShowPicData(PicDataInfo picDataInfo) {
                                MLog.e("Test", TAG, "onShowPicData");
                                if (mCallback != null) {
                                    mCallback.onFail(Constansts.FAIL_TYPE_SHOW_PICDATA, null, null);
                                }
                            }
                        }) {
                            @Override
                            public void onSuccess() {
                                MLog.e("Test", TAG, "onSuccess");
                                if (mCallback != null) {
                                    mCallback.onSuccess(null);
                                }
                            }

                            @Override
                            public void onError(ErrorResult errorResult) {
                                MLog.e("Test", TAG, "onError");
                                if (mCallback != null) {
                                    mCallback.onError(errorResult.getErrorMsg());
                                }
                            }
                        });
    }

    private String getFengkongUri(String account) {
        String findPwdUrl = "https://plogin.m.jd.com/cgi-bin/m/mfindpwd";
        String versionName = null;

        try {
            PackageInfo packageInfo = BaseApplication.getApplication().getPackageManager()
                    .getPackageInfo(BaseApplication.getApplication().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String formatUrl = String.format("%1$s?appid=%2$s&show_title=%3$s&client_type=%4$s&os_version=%5$s&app_client_ver=%6$s&uuid=%7$s&account=%8$s&returnurl=%9$s",
                findPwdUrl, UserUtil.APPID, "0", "android", Build.VERSION.RELEASE, versionName,
                "", account, Constansts.FROMREGIST);
        return formatUrl;
    }

    @Override
    public void finish() {
        mCallback = null;
    }
}


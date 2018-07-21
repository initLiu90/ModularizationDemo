package com.lzp.login.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.lzp.core.base.BaseApplication;
import com.lzp.library.util.MLog;

import jd.wjlogin_sdk.common.WJLoginHelper;
import jd.wjlogin_sdk.common.listener.OnCommonCallback;
import jd.wjlogin_sdk.model.ClientInfo;
import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.FailResult;

/**
 * 构造单例helper,开发中建议使用单例模式。
 *
 * @author Administrator
 */

public class UserUtil {
    private static WJLoginHelper helper;
    public static final short APPID = 100;

    public static ClientInfo getClientInfo() {
        ClientInfo clientInfo = new ClientInfo();
        //下面的值必须填上，不能为空。
        clientInfo.setDwAppID(APPID);
        clientInfo.setAppName("WJLoginAndroidDemo");
        clientInfo.setArea("SHA");
        clientInfo.setUuid(getDeviceId());
        clientInfo.setDwGetSig(1);
        clientInfo.setPartner("jingdong");
        clientInfo.setUnionId("50965");
        clientInfo.setSubunionId("jingdong");
        return clientInfo;
    }

    public static void refreshA2() {
        getWJLoginHelper().refreshA2(new OnCommonCallback() {

            @Override
            public void onSuccess() {
                Toast.makeText(BaseApplication.getApplication(), "刷新A2成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(FailResult failResult) {

            }

            @Override
            public void onError(ErrorResult error) {
                // TODO 自动生成的方法存根
                Toast.makeText(BaseApplication.getApplication(), error + "",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 单例模式的helper
     *
     * @return
     */
    public synchronized static WJLoginHelper getWJLoginHelper() {
        if (helper == null) {
            helper = WJLoginHelper.createInstance(BaseApplication.getApplication(), UserUtil.getClientInfo());
        }
        return helper;
    }


    public static String getDeviceId() {
        try {
            TelephonyManager tm = (TelephonyManager) BaseApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            return imei == null ? "" : imei;
        } catch (Exception e) {
            return "";
        }
    }
}

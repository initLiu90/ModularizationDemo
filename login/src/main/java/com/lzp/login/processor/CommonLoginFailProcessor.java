package com.lzp.login.processor;

import com.lzp.login.utils.UserUtil;

import jd.wjlogin_sdk.common.listener.AbsFailureProcessor;
import jd.wjlogin_sdk.common.listener.ILoginReplyCodeHandle;
import jd.wjlogin_sdk.model.FailResult;
import jd.wjlogin_sdk.model.JumpResult;
import jd.wjlogin_sdk.model.PicDataInfo;

/**
 * 用户名密码登录以及微信登录
 */
public abstract class CommonLoginFailProcessor extends AbsFailureProcessor implements ILoginReplyCodeHandle {
    public static final int TYPE_PWD = 1;//用户名密码登录
    public static final int TYPE_UNION = 2;//统一登录

    static final int FAIL_TYPE_commondHandler = 0x01;
    /**
     * 账号不存在
     */
    static final int FAIL_TYPE_accountNotExist = 0x02;

    /**
     * 账号因为安全组策略禁止登陆
     */
    static final int FAIL_TYPE_forbid = 0x03;

    /**
     * 账号不锁定
     */
    static final int FAIL_TYPE_accountLocked = 0x04;


    private int mType;

    public CommonLoginFailProcessor(int type) {
        mType = type;
    }

    @Deprecated
    @Override
    protected void handleFailResult(FailResult failResult) {
        if (mType == TYPE_PWD) {
            handlePwdLoginFail(failResult);
        } else if (mType == TYPE_UNION) {
            handleUnionLoginFail(failResult);
        }
    }

    private void handleUnionLoginFail(FailResult failResult) {
        byte var2;
        if ((var2 = failResult.getReplyCode()) != 35 && var2 != 37) {
            if (var2 == 36) {
                this.handle0x24(failResult);
            } else if (var2 == 106) {
                this.handle0x6a(failResult);
            } else if (var2 == 100) {
                this.handle0x64(failResult);
            } else if (var2 == 8) {
                this.handle0x8(failResult);
            } else if (var2 >= 123 && var2 <= 126) {
                this.handleBetween0x7bAnd0x7e(failResult);
            } else if (var2 >= -128 && var2 <= -113) {
                this.onSendMsg(failResult);
            } else if (var2 >= 119 && var2 <= 122) {
                this.handleBetween0x77And0x7a(failResult);
            } else {
                this.onCommonHandler(failResult);
            }
        } else {
            this.bindJDAccount(failResult);
        }
    }

    private void handlePwdLoginFail(FailResult failResult) {
        byte var2 = failResult.getReplyCode();
        PicDataInfo var3;
        if ((var3 = failResult.getPicDataInfo()) != null) {
            this.showPicData(var3);
        } else {
            this.hidePicData();
        }

        if (6 != var2 && 7 != var2) {
            if (var2 == 106) {
                this.handle0x6a(failResult);
            } else if (var2 == 100) {
                this.handle0x64(failResult);
            } else if (var2 == 8) {
                this.handle0x8(failResult);
            } else if (var2 >= 123 && var2 <= 126) {
                this.handleBetween0x7bAnd0x7e(failResult);
            } else if (var2 == 103) {
                this.getBackPassword(failResult);
            } else if (var2 >= -128 && var2 <= -113) {
                this.onSendMsg(failResult);
            } else if (var2 >= 119 && var2 <= 122) {
                this.handleBetween0x77And0x7a(failResult);
            } else {
                this.onCommonHandler(failResult);
            }
        } else {
            this.accountNotExist(failResult);
        }
    }

    private void onCommonHandler(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_commondHandler, replyCode, message);
    }

    @Deprecated
    public void getBackPassword(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onAccountLocked(replyCode, message);
    }

    private void bindJDAccount(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        JumpResult jumpResult = failResult.getJumpResult();
        String url = "";
        String token = "";
        if (null != jumpResult) {
            url = jumpResult.getUrl();
            token = jumpResult.getToken();
        }
        String fullBindUrl = String.format("%1$s?appid=%2$s&token=%3$s&succcb=openApp.jdMobile://communication",
                url, UserUtil.APPID, token);
        onBindJDAccount(fullBindUrl, token, message);
    }

    @Deprecated
    public void accountNotExist(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_accountNotExist, replyCode, message);
    }

    private void hidePicData() {
        onHidePicData();
    }

    private void showPicData(PicDataInfo var1) {
        onShowPicData(var1);
    }

    /**
     * 账号因为安全组策略禁止登陆
     *
     * @param failResult
     */
    @Deprecated
    public void handle0x6a(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_forbid, replyCode, message);
    }

    /**
     * 账号因为安全策略禁止登陆
     *
     * @param failResult
     */
    @Deprecated
    public void handle0x64(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_forbid, replyCode, message);
    }

    /**
     * 账号因为安全策略禁止登陆
     *
     * @param failResult
     */
    @Deprecated
    public void handle0x8(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_forbid, replyCode, message);
    }

    /**
     * 账号因为安全策略禁止登陆
     *
     * @param failResult
     */
    private void handle0x24(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_forbid, replyCode, message);
    }


    /**
     * 单按钮预留,商城Ui处理：显示单按钮
     *
     * @param failResult
     */
    @Deprecated
    public void handleBetween0x7bAnd0x7e(FailResult failResult) {
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        onFail(FAIL_TYPE_forbid, replyCode, message);
    }

    /**
     * 为不带Token跳转到M页进行预留
     *
     * @param failResult
     */
    @Deprecated
    public void handleBetween0x77And0x7a(FailResult failResult) {
        String url = "";
        String token = "";
        JumpResult jumpResult = failResult.getJumpResult();
        if (null != jumpResult) {
            url = jumpResult.getUrl();
            token = jumpResult.getToken();
        }
        String message = failResult.getMessage();
        byte replyCode = failResult.getReplyCode();
        toMPage(url, token, message);
    }

    /**
     * 风险用户，未绑或已绑手机的用户需要去做上行或下行短信验证。
     *
     * @param failResult
     */
    @Deprecated
    public void onSendMsg(FailResult failResult) {
        String url = "";
        String token = "";
        JumpResult jumpResult = failResult.getJumpResult();
        if (null != jumpResult) {
            url = jumpResult.getUrl();
            token = jumpResult.getToken();
        }
        String fullBindUrl = getFengkongM(url, token);
        sendSms(fullBindUrl, token);
    }


    /**
     * 登录失败，通常是不需要页面跳转的失败类型
     *
     * @param failType     登录失败的类型 {@link CommonLoginFailProcessor#FAIL_TYPE_accountNotExist#FAIL_TYPE_commondHandler#FAIL_TYPE_forbid}
     * @param originalCode jd登录返回的原始errorcode
     * @param message
     */
    public abstract void onFail(int failType, byte originalCode, String message);

    /**
     * 账号不存在
     *
     * @param originalCode
     * @param message
     */
    public abstract void onAccountLocked(byte originalCode, String message);

    /**
     * 不需要显示图片验证码
     */
    public void onHidePicData() {

    }

    /**
     * 需要显示图片验证码
     *
     * @param picDataInfo
     */
    public void onShowPicData(PicDataInfo picDataInfo) {

    }

    /**
     * 第三方账号需要绑定京东账号
     *
     * @param url
     * @param token
     * @param message
     */
    public void onBindJDAccount(String url, String token, String message) {

    }

    /**
     * 跳转到M页
     *
     * @param url
     * @param token
     * @param message
     */
    public abstract void toMPage(String url, String token, String message);

    public abstract void sendSms(String url, String token);

    /**
     * 风控的URL
     *
     * @param url
     * @param token
     * @return
     */
    private String getFengkongM(String url, String token) {
        String fullBindUrl = String.format("%1$s?appid=%2$s&token=%3$s&returnurl=openmyapp.care://myhost",
                url, UserUtil.APPID, token);
        return fullBindUrl;
    }

}

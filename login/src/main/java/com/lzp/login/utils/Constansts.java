package com.lzp.login.utils;

public class Constansts {
    public static final String WXAPP_ID = "wx7db1270135ecd93b";
    public static final String BROADCAST_FROM_WXLOGIN = "wxloginreceiver";
    public static final String SLEF_BROADCAST_PERMISSION = "permission.self_broadcast";
    public static final String FROMREGIST = "https%3a%2f%2fplogin.m.jd.com%2fuser%2flogin.action%3fappid%3d100%26returnurl%3dregist.openApp.jdMobile%3a%2f%2fcommunication";

    public static final int FAIL_TYPE_DEFAULE = 0x00;

    /********************短信验证码相关 START*********************/
    /**
     * 其他错误
     */
    public static final int FAIL_TYPE_SMS_CODE1 = 0x01;

    /**
     * 提交过于频繁
     */
    public static final int FAIL_TYPE_SMS_CODE2 = 0x02;

    /**
     * 短信已发送，请勿重复提交
     */
    public static final int FAIL_TYPE_SMS_CODE3 = 0x03;
    /********************短信验证码相关 END*********************/

    /********************图片验证码相关 START*********************/
    /**
     * 隐藏验证码
     */
    public static final int FAIL_TYPE_HIDE_PICDATA = 0x20;

    /**
     * 显示验证码
     */
    public static final int FAIL_TYPE_SHOW_PICDATA = 0x21;
    /********************图片验证码相关 END*********************/

    /**
     * 账号被锁定
     */
    public static final int FAIL_TYPE_ACCOUNT_LOCK = 0x40;

    /********************跳转相关 START*********************/
    /**
     * 跳转到web页面
     */
    public static final int TRANSLATE_TYPE_WEB = 0x01;

    /********************跳转相关 END*********************/
}

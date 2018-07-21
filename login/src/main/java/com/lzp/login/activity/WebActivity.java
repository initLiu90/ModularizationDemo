package com.lzp.login.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lzp.core.base.BaseActivity;
import com.lzp.library.util.MLog;
import com.lzp.login.R;
import com.lzp.login.utils.UserUtil;

import jd.wjlogin_sdk.common.listener.OnCommonCallback;
import jd.wjlogin_sdk.model.ErrorResult;
import jd.wjlogin_sdk.model.FailResult;

public class WebActivity extends BaseActivity {
    private static final String TAG = "WebActivity";
    public static final String KEY_URL = "url";
    public static final String KEY_TAG = "tag";
    public static final String KEY_REGIST = "isRegist";

    private WebView mWebView;
    private String tag = "";
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_web);
        initDataAndView();
        initWebSetting();
    }

    private void initDataAndView() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(KEY_URL);
        tag = intent.getStringExtra(KEY_TAG);
        mWebView = (WebView) findViewById(R.id.activity_web_webview);
    }

    private void initWebSetting() {

        // 允许js
        WebSettings wSet = mWebView.getSettings();
        wSet.setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边

                MLog.e("Test", TAG, "ertterre:" + url);
                try {
                    Uri uri = Uri.parse(url);
                    if (uri.getScheme().equals("openmyapp.care")) {
                        String status = uri.getQueryParameter("status");
                        String token = null;
                        if (status.equals("true")) {
                            token = uri.getQueryParameter("token");
                        }
                        if (!TextUtils.isEmpty(token)) {
                            checkToken(token);
                        } else {
                            Toast.makeText(WebActivity.this, "关联帐号失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        view.loadUrl(url);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();

                    Toast.makeText(WebActivity.this, "关联帐号失败",
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void checkToken(String token) {

        try {
            if ("sms".equals(tag)) {
                smsVerifyLogin(token);
            } else if ("thirdLogin".equals(tag)) {
                bindLogin(token);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 微信或者QQ授权登陆
     *
     * @param token
     */
    private void bindLogin(String token) {
        UserUtil.getWJLoginHelper().bindAccountLogin(token, new OnCommonCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ErrorResult s) {

            }

            @Override
            public void onFail(FailResult failResult) {

            }
        });
    }

    /**
     * 上下行短信验证
     *
     * @param token
     */
    private void smsVerifyLogin(String token) {
        UserUtil.getWJLoginHelper().h5BackToApp(token, new OnCommonCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ErrorResult s) {

            }

            @Override
            public void onFail(FailResult failResult) {

            }
        });
    }
}

package com.lzp.library.net;

import com.lzp.library.util.MLog;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(String message) {
        // 请求或者响应开始
        if (message.startsWith("-->")) {
            mMessage.setLength(0);
        }

        mMessage.append(message.concat("\n"));
        // 响应结束，打印整条日志
        if (message.startsWith("<--")) {
            MLog.w("Test", "HttpLog", mMessage.toString());
        }
    }
}

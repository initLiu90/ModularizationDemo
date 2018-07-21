package com.lzp.login.adapter;

import android.os.Bundle;

public abstract class Adapter {

    public abstract void process(Argument arg, Callback callback);

    public void finish(){

    }

    public static class Argument {
        public String arg1;
        public String arg2;
        public Object arg3;
    }

    public interface Callback {
        void onSuccess(Object data);

        void onFail(int failType, String message, Object data);

        void onError(String message);

        /**
         * 需要跳转到其他页面处理
         * @param type 页面跳转的类型
         * @param bundle 携带的参数
         */
        void onTranslate(int type, Bundle bundle);
    }
}

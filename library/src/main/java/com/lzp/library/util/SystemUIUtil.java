package com.lzp.library.util;

import android.content.Context;

public class SystemUIUtil {
    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusbarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }
}

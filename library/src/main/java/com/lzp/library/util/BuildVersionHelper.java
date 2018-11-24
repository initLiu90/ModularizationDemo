package com.lzp.library.util;

import android.os.Build;

/**
 * 判断版本号工具类
 */
public class BuildVersionHelper {
    /**
     * 判断Build.VERSION.SDK_INT是否大于等于buildVersion
     *
     * @param buildVersion {@link Build.VERSION_CODES}
     * @return
     */
    public static boolean greaterEqual(int buildVersion) {
        return Build.VERSION.SDK_INT >= buildVersion;
    }

    /**
     * 判断Build.VERSION.SDK_INT是否大于buildVersion
     *
     * @param buildVersion {@link Build.VERSION_CODES}
     * @return
     */
    public static boolean greater(int buildVersion) {
        return Build.VERSION.SDK_INT > buildVersion;
    }

    /**
     * 判断Build.VERSION.SDK_INT是否等于buildVersion
     *
     * @param buildVersion {@link Build.VERSION_CODES}
     * @return
     */
    public static boolean equal(int buildVersion) {
        return Build.VERSION.SDK_INT == buildVersion;
    }

    /**
     * 判断Build.VERSION.SDK_INT是否小于等于buildVersion
     *
     * @param buildVersion {@link Build.VERSION_CODES}
     * @return
     */
    public static boolean lessThanEqual(int buildVersion) {
        return Build.VERSION.SDK_INT <= buildVersion;
    }

    /**
     * 判断Build.VERSION.SDK_INT是否小于buildVersion
     *
     * @param buildVersion {@link Build.VERSION_CODES}
     * @return
     */
    public static boolean lessThan(int buildVersion) {
        return Build.VERSION.SDK_INT < buildVersion;
    }
}

package com.lzp.library.util;

import android.app.Activity;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lzp.library.view.ImmersiveStatusBarView;

/**
 * 沉浸式状态栏工具类
 */
public final class ImmersiveStatusBarHelper {
    /**
     * 开启沉浸式状态栏
     *
     * @param activity
     * @param color
     */
    public static void enableImmersiveStatusBar(Activity activity, int color) {
        if (BuildVersionHelper.greaterEqual(Build.VERSION_CODES.LOLLIPOP)) {//>=21
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /**
             * Sets the color of the status bar to {@code color}.
             *
             * For this to take effect,
             * the window must be drawing the system bar backgrounds with
             * {@link android.view.WindowManager.LayoutParams#FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS} and
             * {@link android.view.WindowManager.LayoutParams#FLAG_TRANSLUCENT_STATUS} must not be set.
             */
            activity.getWindow().setStatusBarColor(color);
        } else if (BuildVersionHelper.greaterEqual(Build.VERSION_CODES.KITKAT)) {//>=19
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //如果已经给activity设置了布局，取布局中的根元素，设置setFitsSystemWindows(true)
            ViewGroup content = activity.findViewById(android.R.id.content);
            if (content.getChildAt(0) != null) {
                content.getChildAt(0).setFitsSystemWindows(true);
            }
            //设置setFitsSystemWindows(true)后，系统会为布局设置一个padding，为DecoreView添加一个根statusbar相同高度的view，填充这块
            ImmersiveStatusBarView statusBarView = new ImmersiveStatusBarView(activity, color);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUIUtil.getStatusbarHeight(activity));
            ((ViewGroup) activity.getWindow().getDecorView()).addView(statusBarView, 0, params);
        }
    }
}

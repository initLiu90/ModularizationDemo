package com.lzp.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public class MToast {
    private Toast mToast;

    public MToast makeText(Context context, CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        return this;
    }

    public MToast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, duration);
        } else {
            mToast.setText(resId);
            mToast.setDuration(duration);
        }
        return this;
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
}

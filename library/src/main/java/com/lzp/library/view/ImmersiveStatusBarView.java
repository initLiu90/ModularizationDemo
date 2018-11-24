package com.lzp.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lzp.library.util.SystemUIUtil;

public class ImmersiveStatusBarView extends View {
    public ImmersiveStatusBarView(Context context, int color) {
        this(context, null, color);
    }

    public ImmersiveStatusBarView(Context context, AttributeSet attrs, int color) {
        this(context, attrs, 0, color);
    }

    public ImmersiveStatusBarView(Context context, AttributeSet attrs, int defStyleAttr, int color) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = SystemUIUtil.getStatusbarHeight(getContext());
        int widthSize = ((ViewGroup) getParent()).getMeasuredWidth();
        setMeasuredDimension(widthSize, heightSize);
    }
}

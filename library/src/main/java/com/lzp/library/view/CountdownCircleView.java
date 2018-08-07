package com.lzp.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CountdownCircleView extends View {
    private Paint mPaint;
    private Paint mTextPaint;
    private static final String TEXT = "跳过";
    private Rect mTextRect = new Rect();
    private int mSweepAngle = 360;
    private ObjectAnimator mAnimator;
    private CountdownListener mListener;

    public CountdownCircleView(Context context) {
        this(context, null);
    }

    public CountdownCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10f);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(35f);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.getTextBounds(TEXT, 0, TEXT.length(), mTextRect);

        mAnimator = ObjectAnimator.ofInt(this, "sweepAngle", -360, 0);
        mAnimator.setDuration(1000 * 10);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onEnd();
                }
            }
        });
    }

    public void setSweepAngle(int sweepAngle) {
        this.mSweepAngle = sweepAngle;
        invalidate();
    }

    public void startCountdown() {
        if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    public void stopCountdown() {
        if (!mAnimator.isStarted()) {
            mAnimator.cancel();
        }
    }

    public boolean isCountdowning() {
        return mAnimator.isStarted();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int lenght = Math.min(getWidth(), getHeight());
        final int paintWidth = (int) mPaint.getStrokeWidth();

        final int left = paintWidth, top = paintWidth;
        final int right = lenght - paintWidth, bottom = lenght - paintWidth;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawArc(rectF, -90, mSweepAngle, false, mPaint);

        final float x = rectF.centerX();
        final float y = rectF.centerY();
        float textHeight = (-mTextPaint.getFontMetrics().ascent - mTextPaint.getFontMetrics().descent);
        canvas.drawText(TEXT, x - mTextRect.width() / 2, y + textHeight / 2, mTextPaint);
    }

    public void setListener(CountdownListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCountdown();
        if (mListener != null) {
            mListener = null;
        }
    }

    public interface CountdownListener {
        void onEnd();
    }
}

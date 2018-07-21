/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lzp.library.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 * <p>
 * Example of showing a 30 second countdown in a text field:
 * <p>
 * <pre class="prettyprint">
 * new CountDownTimer() {
 * <p>
 * public void onTick(long millisUntilFinished) {
 * mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 * }
 * <p>
 * public void onFinish() {
 * mTextField.setText("done!");
 * }
 * }.start(60, 1);
 * </pre>
 * <p>
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
public abstract class CountdownTimer {

    /**
     * Millis since epoch when alarm should stop.
     */
    private int mSecondsInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private int mCountdownInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    private boolean mIsRunning = false;

    public CountdownTimer() {

    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mIsRunning = false;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countdown.
     *
     * @param secondsInFuture  The number of seconds in the future from the call
     *                          to {@link #start(int, int)} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public synchronized final CountdownTimer start(int secondsInFuture, int countDownInterval) {
        if (mIsRunning) return this;

        mSecondsInFuture = secondsInFuture;
        mCountdownInterval = countDownInterval;

        mCancelled = false;
        if (mSecondsInFuture <= 0) {
            mIsRunning = false;
            onFinish();
            return this;
        }
        if (!mIsRunning) mIsRunning = true;

        mStopTimeInFuture = SystemClock.elapsedRealtime() + mSecondsInFuture * 1000;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public boolean isRunning() {
        return mIsRunning;
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountdownTimer.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    mIsRunning = false;
                    onFinish();
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft/1000);

                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}

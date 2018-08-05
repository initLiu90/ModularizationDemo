package com.lzp.library.util;

import android.util.Log;

public final class MLog {

    public static boolean mDebug = true;

    private MLog() {

    }

    /**
     * Send a {@link Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        if (mDebug) {
            return Log.v(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String subTag, String msg) {
        if (mDebug) {
            return Log.v(tag, "[" + subTag + "] " + msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String tag, String subTag, String msg, Throwable tr) {
        if (mDebug) {
            return Log.v(tag, "[" + subTag + "] " + msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        if (mDebug) {
            return Log.d(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String subTag, String msg) {
        if (mDebug) {
            return Log.d(tag, "[" + subTag + "] " + msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String tag, String subTag, String msg, Throwable tr) {
        if (mDebug) {
            return Log.d(tag, "[" + subTag + "] " + msg, tr);
        }
        return 0;
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        if (mDebug) {
            return Log.i(tag, msg);
        }
        return 0;
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String subTag, String msg) {
        if (mDebug) {
            return Log.i(tag, "[" + subTag + "] " + msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String tag, String subTag, String msg, Throwable tr) {
        if (mDebug) {
            return Log.i(tag, "[" + subTag + "] " + msg, tr);
        }
        return 0;
    }

    /**
     * Send a {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        if (mDebug) {
            return Log.w(tag, msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String subTag, String msg) {
        if (mDebug) {
            return Log.w(tag, "[" + subTag + "] " + msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(String tag, String subTag, String msg, Throwable tr) {
        if (mDebug) {
            return Log.w(tag, "[" + subTag + "] " + msg, tr);
        }
        return 0;
    }

    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static int w(String tag, String subTag, Throwable tr) {
        if (mDebug) {
            return Log.w(tag, tr);
        }
        return 0;
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        if (mDebug) {
            return Log.e(tag, msg);
        }
        return 0;
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String subTag, String msg) {
        if (mDebug) {
            return Log.e(tag, "[" + subTag + "] " + msg);
        }
        return 0;
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String tag, String subTag, String msg, Throwable tr) {
        if (mDebug) {
            return Log.e(tag, "[" + subTag + "] " + msg, tr);
        }
        return 0;
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (mDebug) {
            return Log.getStackTraceString(tr);
        }
        return "";
    }

    /**
     * Low-level logging call.
     *
     * @param priority The priority/type of this log message
     * @param tag      Used to identify the source of a log message.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, String tag, String subTag, String msg) {
        if (mDebug) {
            Log.println(priority, tag, "[" + subTag + "] " + msg);
        }
        return 0;
    }
}

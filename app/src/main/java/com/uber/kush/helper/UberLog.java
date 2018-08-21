package com.uber.kush.helper;

import android.util.Log;

/**
 * Created by kushpatel on 7/11/2017.
 */

public class UberLog {

    public static boolean isDebuggable = true;

    public static void d(String tag, String msg) {
        if (isDebuggable)
            Log.d(tag, msg != null ? msg : "");
    }

    public static void e(String tag, String msg) {
        if (isDebuggable)
            Log.e(tag, msg != null ? msg : "");
    }

    public static void v(String tag, String msg) {
        if (isDebuggable)
            Log.v(tag, msg != null ? msg : "");
    }

    public static void w(String tag, String msg) {
        if (isDebuggable)
            Log.w(tag, msg != null ? msg : "");
    }

    public static void i(String tag, String msg) {
        if (isDebuggable)
            Log.i(tag, msg != null ? msg : "");
    }
}

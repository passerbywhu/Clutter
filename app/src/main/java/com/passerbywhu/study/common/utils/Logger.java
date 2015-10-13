package com.passerbywhu.study.common.utils;

import android.util.Log;

import com.passerbywhu.study.MyApplication;
import com.passerbywhu.study.common.consts.Const;

/**
 * Created by wuwenchao3 on 2015/8/28.
 */
public class Logger {
    private static final String DEFAULT_TAG = MyApplication.getInstance().getPackageName();

    public static void e(String TAG, String msg) {
        if (Const.IS_DEBUG) {
            Log.e(TAG, getStackInfo() + ": " + msg);
        }
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    public static void i(String TAG, String msg) {
        if (Const.IS_DEBUG) {
            Log.i(TAG, getStackInfo() + ": " + msg);
        }
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void w(String TAG, String msg) {
        if (Const.IS_DEBUG) {
            Log.w(TAG,getStackInfo() + ": " + msg);
        }
    }

    public static void w(String msg) {
        w(DEFAULT_TAG, msg);
    }

    private static String getStackInfo() {
        String info = "";
        Throwable throwable = new Throwable();
        StackTraceElement[] traceElements = throwable.getStackTrace();
        if (traceElements != null && traceElements.length > 3) {
            StackTraceElement ele = traceElements[3];
            info += " " + ele.getClassName().substring(ele.getClassName().lastIndexOf(".") + 1) + " " + ele.getMethodName() + " " + ele.getLineNumber();
        }
        return info;
    }
}

package com.mysdk.sdkutil;

import android.util.Log;

/**
 * Created by cwl on 2017/5/15.
 */

public class LogUtils {
    /**
     * 定制日志工具
     */
    public static final int VERBOSE=1;
    public static final int DEBUG=2;
    public static final int INFO=3;
    public static final int WARN=4;
    public static final int ERROR=5;
    public static final int NOTHING=6;
    public static final int Level=VERBOSE;

    public static void v(String tag,String msg){
        if (Level<=VERBOSE){
            Log.d(tag, msg);
        }
    }
    public static void d(String tag,String msg){
        if (Level<=DEBUG){
            Log.d(tag, msg);
        }
    }
    public static void i(String tag,String msg){
        if (Level<=INFO){
            Log.d(tag, msg);
        }
    }
    public static void w(String tag,String msg){
        if (Level<=WARN){
            Log.d(tag, msg);
        }
    }
    public static void e(String tag,String msg){
        if (Level<=ERROR){
            Log.d(tag, msg);
        }
    }
}

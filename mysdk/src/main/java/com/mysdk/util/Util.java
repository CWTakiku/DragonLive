package com.mysdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by cwl on 2017/5/9.
 */

public class Util {

    /**
     * 获得版本号
     * @param context
     * @return
     */
     public static int getversionCode(Context context){
         int versionCode=1;
         try{
             PackageManager pm=context.getPackageManager();
             PackageInfo pi=pm.getPackageInfo(context.getPackageName(),0);
             versionCode=pi.versionCode;
         } catch (Exception e) {
             e.printStackTrace();
         }
         return versionCode;
     }

    /**
     * 获得版本名
     * @param context
     * @return
     */
    public static String getversionName(Context context){
        String versionName="1.0.0";
        try{
            PackageManager pm=context.getPackageManager();
            PackageInfo pi=pm.getPackageInfo(context.getPackageName(),0);
            versionName=pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 隐藏软键盘
     * @param context
     * @param v
     */
    public static void hideSoftInputMethod(Context context, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    /**
     * 显示系统软件盘
     * 传入的View必须是EditText及其子类才可以强制显示出
     */
    public static void showSoftInputMethod(Context context, View v) {
        /* 隐藏软键盘 */
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }


}

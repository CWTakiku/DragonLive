package com.mysdk.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

/**
 * Created by cwl on 2018/5/31.
 */

public class PermissionUtil {
    /**
     * 判断是否有权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context,String permission){
        if (Build.VERSION.SDK_INT>=23){  //大于6.0安卓系统 需要动态申请权限
            if (context.checkSelfPermission(permission)!= PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermission(Activity activity,String[] permissions,int requestCode){
        if (Build.VERSION.SDK_INT>23){
            activity.requestPermissions(permissions,requestCode);
        }
    }

    /**
     * 缺少的权限 被拒绝或者没有获取到
     * @param context
     * @param permissions
     * @return
     */
    public static String[] getDeniedPermissions(Context context, String[] permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for(String permission : permissions){
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                    deniedPermissionList.add(permission);
                }
            }
            int size = deniedPermissionList.size();
            if(size > 0){
                return deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
            }
        }
        return null;
    }

}

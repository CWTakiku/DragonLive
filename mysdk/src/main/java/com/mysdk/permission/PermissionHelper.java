package com.mysdk.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * Created by cwl on 2018/5/31.
 */

public class PermissionHelper {

    private Activity mActivity;
    private PermissionInterface mPermissionInterface;
    public PermissionHelper(Activity activity,PermissionInterface permissionInterface){
        this.mActivity=activity;
        this.mPermissionInterface=permissionInterface;
   }

    /**
     * 请求权限
     */
   public void requestPermission(){
       String[] deniedPermissions=PermissionUtil.getDeniedPermissions(mActivity,mPermissionInterface.getPermissions());
       if (deniedPermissions!=null&&deniedPermissions.length>0){
           PermissionUtil.requestPermission(mActivity,deniedPermissions,mPermissionInterface.getPermissionsRequestCode());
       }else {
           mPermissionInterface.requestPermissionsSuccess();
       }
   }


    /**
     * 请求结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return  true 代表已处理该request false代表没处理
     */
    public boolean requestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        if (requestCode==mPermissionInterface.getPermissionsRequestCode()){
            boolean isAllGranted=true;//已全部授权
            for (int result:grantResults){
                if (result== PackageManager.PERMISSION_DENIED){
                    isAllGranted=false;
                    break;
                }
            }
            if (isAllGranted){
              mPermissionInterface.requestPermissionsSuccess();
            }else {
               mPermissionInterface.requestPermissionsFail();
            }
            return true;
        }
     return false;
    }

}

package dragonlive.cwl.com.dragonlive.hostlive;

import android.hardware.Camera;

import com.tencent.ilivesdk.core.ILiveLoginManager;

/**
 * Created by cwl on 2018/5/21.
 * 闪光灯
 */

public class FlashLightHelper {


    public boolean isFlashLightOn(){
        Object obj= ILiveLoginManager.getInstance().getAVConext().getVideoCtrl().getCamera();
        if (obj==null||!(obj instanceof Camera)){
            return false;
        }
        Camera camera= (Camera) obj;
       Camera.Parameters parameters=  camera.getParameters();
        if (parameters==null){
            return false;
        }
        if (Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())){
            return true;
        }
        return false;
    }
    public boolean enableFlashLight(boolean enable){
        Object obj= ILiveLoginManager.getInstance().getAVConext().getVideoCtrl().getCamera();
        if (obj==null||!(obj instanceof Camera)){
            return false;
        }
        Camera camera= (Camera) obj;
        Camera.Parameters parameters=  camera.getParameters();
        if (parameters==null){
            return false;
        }
        if (enable){
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        try {
            camera.setParameters(parameters);
        }catch (Exception e){
          return false;
        }
        return true;
    }
}

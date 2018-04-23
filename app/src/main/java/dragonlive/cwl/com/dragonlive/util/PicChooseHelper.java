package dragonlive.cwl.com.dragonlive.util;

import android.app.Activity;

import dragonlive.cwl.com.dragonlive.editprofile.PicChooseDialog;

/**
 * Created by cwl on 2018/4/23.
 */

public class PicChooseHelper {
     private Activity activity;
    public PicChooseHelper( Activity activity){
        this.activity=activity;
    }
    public void showPicChooseDialog(){
        PicChooseDialog dialog=new PicChooseDialog(activity);
        dialog.setOnDialogClickListener(new PicChooseDialog.OnDialogClickListener() {
            @Override
            public void onCamera() {
                //拍照
                takePicFromCamera();
            }

            @Override
            public void onAlbum() {
              //从相册中选择
                takePicFromAlbum();
            }
        });
        dialog.show();
    }

    private void takePicFromAlbum() {
    }

    private void takePicFromCamera() {
      
    }
}

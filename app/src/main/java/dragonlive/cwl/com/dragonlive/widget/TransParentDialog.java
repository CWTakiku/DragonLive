package dragonlive.cwl.com.dragonlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import dragonlive.cwl.com.dragonlive.R;

/**
 * Created by cwl on 2018/4/23.
 */

public class TransParentDialog {
    protected Activity activity;
    protected Dialog dialog;
    public TransParentDialog(Activity activity){
        this.activity=activity;
        dialog=new Dialog(activity, R.style.dialog);
    }
    public void setDialogWidthAndHeght(int width,int height){
        Window win=dialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        if (params!=null){
            params.width=width;
            params.height=height;
            win.setAttributes(params);
        }
    }
    public void setContentView(View view){
        dialog.setContentView(view);
    }
    public void show(){
        dialog.show();
    }
    public void hide()
    {
        dialog.hide();
    }
    public void dismiss(){dialog.dismiss();}
}

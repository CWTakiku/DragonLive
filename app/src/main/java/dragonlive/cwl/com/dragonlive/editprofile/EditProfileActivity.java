package dragonlive.cwl.com.dragonlive.editprofile;

import android.content.Context;
import android.content.SharedPreferences;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;

public class EditProfileActivity extends BaseActivity {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("is_first",false);
        editor.commit();
    }
}

package dragonlive.cwl.com.dragonlive.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import butterknife.Bind;
import dragonlive.cwl.com.dragonlive.MainActivity;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.editprofile.EditProfileActivity;
import dragonlive.cwl.com.dragonlive.register.RegisterActivity;

/**
 * Created by cwl on 2018/4/22.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.account)
    EditText  mAccountEdt;
    @Bind(R.id.password)
    EditText mPasswordEdt;
    @Bind(R.id.login)
    Button loginBtn;
    @Bind(R.id.register)
    Button registerBtn;
    @Bind(R.id.activity_login)
    RelativeLayout activityLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    private static final String TAG = "LoginActivity";
    @Override
    protected void initData() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Log.i("info1", "onClick: ");
                register();
            }
        });

    }
  //注册
    private void register() {
        Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
 //登录
    private void login() {
        final String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();
        if (TextUtils.isEmpty(accountStr) || TextUtils.isEmpty(passwordStr)){
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ILiveLoginManager.getInstance().tlsLoginAll(accountStr, passwordStr, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                  LoginLive(accountStr,String.valueOf(data));
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                Toast.makeText(LoginActivity.this, "tls登录失败：" + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoginLive(String accountStr, String data) {
        ILiveLoginManager.getInstance().iLiveLogin(accountStr, data, new ILiveCallBack() {

            @Override
            public void onSuccess(Object data) {
                //最终登录成功
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
                boolean isFirst=sp.getBoolean("is_first",true);
                if (isFirst) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                getSelfInfo();

                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                Toast.makeText(LoginActivity.this, "iLive登录失败：" + errMsg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getSelfInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoginActivity.this, "获取信息失败：" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                //获取自己信息成功
                MyApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }
}




package dragonlive.cwl.com.dragonlive.register;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import butterknife.Bind;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;

public class RegisterActivity extends BaseActivity {


    @Bind(R.id.titlebar)
    Toolbar titlebar;
    @Bind(R.id.account)
    EditText mAccountEdt;
    @Bind(R.id.password)
    EditText  mPasswordEdt;
    @Bind(R.id.confirm_password)
    EditText  mConfirmPasswordEt;
    @Bind(R.id.register)
    Button registerBtn;
   
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
         titlebar.setTitle("注册新用户");
         titlebar.setTitleTextColor(Color.WHITE);
         setSupportActionBar(titlebar);
    }

    @Override
    protected void initData() { 
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();
        String confirmPswStr = mConfirmPasswordEt.getText().toString();

        if (TextUtils.isEmpty(accountStr) ||
                TextUtils.isEmpty(passwordStr) ||
                TextUtils.isEmpty(confirmPswStr)) {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordStr.equals(confirmPswStr)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
     //注册
        ILiveLoginManager.getInstance().tlsRegister(accountStr, passwordStr, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //注册成功
                Toast.makeText(RegisterActivity.this, "注册成功，去登录", Toast.LENGTH_SHORT).show();
                //登录一下
                //login();

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //注册失败
                Toast.makeText(RegisterActivity.this, "注册失败：错误码" + errMsg, Toast.LENGTH_SHORT).show();

            }
        });
    }


}

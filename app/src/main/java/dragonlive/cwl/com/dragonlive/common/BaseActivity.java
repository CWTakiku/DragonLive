package dragonlive.cwl.com.dragonlive.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mysdk.common.ActivityManager;

import butterknife.ButterKnife;

/**
 * Created by cwl on 2018/4/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public final static String TAG="info1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(getLayoutId());
          ButterKnife.bind(this);
        ActivityManager.getInstance().add(this);
        initData();
        initView();

    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    //启动activity
    public void goToActivity(Class activity,Bundle bundle){
        Intent intent=new Intent(this,activity);
        if (bundle!=null&&bundle.size()>0){
            intent.putExtra("data",bundle);
        }
      startActivity(intent);
    }
    //移除当前activity
    public void removeCurrent(){
        ActivityManager.getInstance().removeCurrent();
    }
    public void removeAll(){
        ActivityManager.getInstance().removeall();
    }
}

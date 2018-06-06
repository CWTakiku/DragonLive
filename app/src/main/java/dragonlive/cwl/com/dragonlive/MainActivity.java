package dragonlive.cwl.com.dragonlive;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.permission.PermissionHelper;
import com.mysdk.permission.PermissionInterface;
import com.mysdk.util.Util;

import butterknife.Bind;
import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.createroom.CreateLiveActivity;
import dragonlive.cwl.com.dragonlive.editprofile.EditProfileFragment;
import dragonlive.cwl.com.dragonlive.livelist.LiveListFragment;
import dragonlive.cwl.com.dragonlive.model.UpdateInfo;
import dragonlive.cwl.com.dragonlive.model.UpdateInfoModel;
import dragonlive.cwl.com.dragonlive.network.RequestCenter;
import dragonlive.cwl.com.dragonlive.updateapk.DownloadService;

public class MainActivity extends BaseActivity implements PermissionInterface{


    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @Bind(R.id.fragment_tabhost)
    FragmentTabHost mTabHost;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    private static final int MESSAGE_BACK=1;
    private static final int MESSAGE_UPDATE=2;
    int backTimes=0;

    private Handler mHandler;
    private UpdateInfo updateInfo;
    private PermissionHelper mPermissionHelper;
    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder= (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
         setTabs();
        mPermissionHelper=new PermissionHelper(this,this);
        mPermissionHelper.requestPermission();
        checkVersion();

    }

    private void setTabs() {
        mTabHost.setup(this,getSupportFragmentManager(),R.id.fragment_container);

        {
            TabHost.TabSpec profileTab=mTabHost.newTabSpec("livelive").setIndicator(getIndicator(R.drawable.tab_livelist));
            mTabHost.addTab(profileTab,LiveListFragment.class,null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
        {
            TabHost.TabSpec profileTab=mTabHost.newTabSpec("createlive").setIndicator(getIndicator(R.drawable.tab_publish_live));
            mTabHost.addTab(profileTab,null,null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
        {
            TabHost.TabSpec profile=mTabHost.newTabSpec("profile").setIndicator(getIndicator(R.drawable.tab_profile));
            mTabHost.addTab(profile,EditProfileFragment.class,null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
     mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent();
             intent.setClass(MainActivity.this,CreateLiveActivity.class);
             startActivity(intent);
         }
     });
    }

    private View getIndicator(int resId) {
      View view= LayoutInflater.from(this).inflate(R.layout.view_indicator,null);
        ImageView tabImg= (ImageView) view.findViewById(R.id.tab_icon);
        tabImg.setImageResource(resId);
        return view;
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_BACK:
                        backTimes = 0;
                        break;
                    case MESSAGE_UPDATE:
                        if (downloadBinder!=null) {
                            downloadBinder.startDownload(updateInfo.apkUrl);
                        }
                        break;
                }
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler!=null){
            mHandler.removeMessages(MESSAGE_BACK);
            mHandler.removeMessages(MESSAGE_UPDATE);
        }
        unbindService(connection);//解绑服务
    }


        /**
         * 显示是否需要联网下载最新版本apk的Dialog
         */
        private void showDownloadDialog() {

            new AlertDialog.Builder(this)
                    .setTitle("下载最新版本")
                    .setMessage(updateInfo.desc)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // showDownLoad();

                         mHandler.sendEmptyMessage(MESSAGE_UPDATE);

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

        //检查最新版本信息
        private void checkVersion(){
            RequestCenter.checkVersion(new DisposeDataListener() {
                @Override
                public void onSuccess(Object object) {
                   UpdateInfoModel updateInfoModel= (UpdateInfoModel) object;
                    updateInfo=updateInfoModel.getData();
                    if (!(updateInfo.version.equals(Util.getversionName(MainActivity.this)))){ //如果版本不一致，则认为可更新
                        showDownloadDialog();
                    }
                }

                @Override
                public void onFailure(Object object) {
                    Toast.makeText(MainActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                }
            });
        }

    //防止误按back 连击2次退出程序
    @Override
    public void onBackPressed() {
      
        backTimes++;
        if (backTimes==2) {
            super.onBackPressed();
            System.exit(0);
        }else {
            Toast.makeText(MyApplication.getContext(), "再点击一次退出互动直播", Toast.LENGTH_SHORT).show(); //避免内存泄漏
            mHandler.sendEmptyMessageDelayed(MESSAGE_BACK, 2000);
        }
    }

    @Override
    public int getPermissionsRequestCode() {
        return 100;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {
        Toast.makeText(this, "授权失败会影响功能的使用，请重新授权或者手动打开应用权限设置", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode,permissions,grantResults)){
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}



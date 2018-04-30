package dragonlive.cwl.com.dragonlive.application;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.ArrayList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.editprofile.CustomProfile;
import dragonlive.cwl.com.dragonlive.model.MessageObservable;

/**
 * Created by cwl on 2018/4/22.
 */

public class MyApplication extends Application {
    private static MyApplication app;
    private static Context appContext;
    private ILVLiveConfig mLiveConfig;

    private TIMUserProfile mSelfProfile;

    private OSS oss;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        appContext = getApplicationContext();
        if(MsfSdkUtils.isMainProcess(this)){    // 仅在主线程初始化
            // 初始化LiveSDK
            ILiveSDK.getInstance().setCaptureMode(ILiveConstants.CAPTURE_MODE_SURFACEVIEW);
            ILiveLog.setLogLevel(ILiveLog.TILVBLogLevel.DEBUG);
            ILiveSDK.getInstance().initSdk(this, 1400086611, 25624);
           // ILiveSDK.getInstance().initSdk(this, 1400028096, 11851);
            //用户信息字段
            List<String> customInfos = new ArrayList<String>();
            customInfos.add(CustomProfile.CUSTOM_GET);
            customInfos.add(CustomProfile.CUSTOM_SEND);
            customInfos.add(CustomProfile.CUSTOM_LEVEL);
            customInfos.add(CustomProfile.CUSTOM_RENZHENG);
            TIMManager.getInstance().initFriendshipSettings(CustomProfile.allBaseInfo,customInfos);
            ILVLiveManager.getInstance().init(new ILVLiveConfig()
                    .setLiveMsgListener(MessageObservable.getInstance()));
            //阿里
            String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
            // 推荐使用OSSAuthCredentialsProvider，token过期后会自动刷新。
            String stsServer = "http://47.106.155.189:7085";
            OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
//config
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSS oss = new OSSClient(MyApplication.getContext(), endpoint, credentialProvider, conf);
            this.oss=oss;

        }
    }
    public void setSelfProfile(TIMUserProfile userProfile) {
        mSelfProfile = userProfile;
    }
    public static Context getContext() {
        return appContext;
    }
    public static MyApplication getApplication() {return app;}
    public TIMUserProfile getSelfProfile() {
        return mSelfProfile;
    }

    public ILVLiveConfig getLiveConfig() {
        return mLiveConfig;
    }
    public  OSS getOss(){return oss;}
}

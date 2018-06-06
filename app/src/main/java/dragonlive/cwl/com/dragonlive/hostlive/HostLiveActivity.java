package dragonlive.cwl.com.dragonlive.hostlive;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.okhttp.request.RequestParams;
import com.mysdk.util.Util;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.customCmdHandle.CustomCmdManager;
import dragonlive.cwl.com.dragonlive.network.NetConfig;
import dragonlive.cwl.com.dragonlive.network.RequestCenter;
import dragonlive.cwl.com.dragonlive.view.BottomControlView;
import dragonlive.cwl.com.dragonlive.view.ChatMsgListView;
import dragonlive.cwl.com.dragonlive.view.ChatView;
import dragonlive.cwl.com.dragonlive.view.DanmuView;
import dragonlive.cwl.com.dragonlive.view.GiftFullView;
import dragonlive.cwl.com.dragonlive.view.GiftRepeatView;
import dragonlive.cwl.com.dragonlive.view.TitleView;
import dragonlive.cwl.com.dragonlive.view.VipEnterView;
import dragonlive.cwl.com.dragonlive.widget.HostControlDialog;
import dragonlive.cwl.com.dragonlive.widget.SizeChangeRelative;
import tyrantgit.widget.HeartLayout;


/**
 * Created by cwl on 2018/5/4.
 */

public class HostLiveActivity extends BaseActivity {

    @Bind(R.id.av_root_view)
    AVRootView mAvRootView;
    @Bind(R.id.control_view)
    BottomControlView mBottomControlView;
    @Bind(R.id.chat_view)
    ChatView mChat_view;
    @Bind(R.id.host_live_relative)
    SizeChangeRelative mHost_Live_relative;
    @Bind(R.id.chat_list)
    ChatMsgListView mChatMsgListView;
    @Bind(R.id.danmu_view)
    DanmuView mDanmuView;
    @Bind(R.id.gift_repeat_view)
    GiftRepeatView mGiftRepeatView;
    @Bind(R.id.parche_view)
    GiftFullView mGiftFullView;
    @Bind(R.id.heart_layout)
    HeartLayout mHearLayout;
    @Bind(R.id.title_view)
    TitleView mTitleView;
    @Bind(R.id.vip_center_view)
    VipEnterView mVipEnterView;
    @Bind(R.id.share_room)
    ImageView mShareRoomView;



    private HostControlState hostControlState;
    private FlashLightHelper flashLightHelper;
    private CustomCmdManager customCmdManager;
    private Timer heartTimer = new Timer();
    private Timer heartBeatTimer = new Timer();
    private Random heartRandom = new Random();


    private int mRoomId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_host_live;
    }

    @Override
    protected void initView() {

        //初始时底部控住见面可见， 聊天栏不可见
        mBottomControlView.setVisibility(View.VISIBLE);
        mBottomControlView.setIsHost(true);
        mChat_view.setVisibility(View.INVISIBLE);

        mHost_Live_relative.setOnSizeChangeListener(new SizeChangeRelative.OnSizeChangeListener() {
            @Override
            public void onLarge() {
//                Log.i(TAG, "onLarge: ");//软键盘隐藏
                mBottomControlView.setVisibility(View.VISIBLE);
                mChat_view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSmall() {
//                Log.i(TAG, "onSmall: ");//软键盘显示
                mBottomControlView.setVisibility(View.INVISIBLE);
                mChat_view.setVisibility(View.VISIBLE);
            }
        });
        ILVLiveManager.getInstance().setAvVideoView(mAvRootView);
        mBottomControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                //点击了聊天按钮，显示聊天操作栏
                mChat_view.setVisibility(View.VISIBLE);
                mBottomControlView.setVisibility(View.INVISIBLE);
                mChat_view.setOnChatSendListener(new ChatView.OnChatSendListener() {


                    @Override
                    public void onChatSend(ILVCustomCmd customCmd, EditText editText) {
                        //发送消息
                        customCmdManager.sendMsg(customCmd);
                        //隐藏软键盘
                        Util.hideSoftInputMethod(HostLiveActivity.this,editText);
                    }

                    @Override
                    public void onBackClick() {
                        mChat_view.setVisibility(View.INVISIBLE);
                        mBottomControlView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onCloseClick() {
               quitLive();
            }

            @Override
            public void onGiftClick() {


            }

            @Override
            public void onOptionClick(View view) {
                //显示主播操作对话框

                boolean beautyOn = hostControlState.isBeautyOn();
                boolean flashOn = flashLightHelper.isFlashLightOn();
                boolean voiceOn = hostControlState.isVoiceOn();
                HostControlDialog hostControlDialog=new HostControlDialog(HostLiveActivity.this);
                hostControlDialog.setOnControlClickListener(new HostControlDialog.OnControlClickListener() {
                    @Override
                    public void onBeautyClick() {
                        //点击美颜
                        boolean isBeautyOn = hostControlState.isBeautyOn();
                        if (isBeautyOn) {
                            //关闭美颜
                            ILiveRoomManager.getInstance().enableBeauty(0);
                            hostControlState.setBeautyOn(false);
                        } else {
                            //打开美颜
                            ILiveRoomManager.getInstance().enableBeauty(50);
                            hostControlState.setBeautyOn(true);
                        }
                    }

                    @Override
                    public void onFlashClick() {
                        // 闪光灯
                        boolean isFlashOn = flashLightHelper.isFlashLightOn();
                        if (isFlashOn) {
                            flashLightHelper.enableFlashLight(false);
                        } else {
                            flashLightHelper.enableFlashLight(true);
                        }
                    }

                    @Override
                    public void onVoiceClick() {
                        //声音
                        boolean isVoiceOn = hostControlState.isVoiceOn();
                        if (isVoiceOn) {
                            //静音
                            ILiveRoomManager.getInstance().enableMic(false);
                            hostControlState.setVoiceOn(false);
                        } else {
                            ILiveRoomManager.getInstance().enableMic(true);
                            hostControlState.setVoiceOn(true);
                        }
                    }

                    @Override
                    public void onCameraClick() {
                        int cameraId = hostControlState.getCameraid();
                        if (cameraId == ILiveConstants.FRONT_CAMERA) {
                            ILiveRoomManager.getInstance().switchCamera(ILiveConstants.BACK_CAMERA);
                            hostControlState.setCameraid(ILiveConstants.BACK_CAMERA);
                        } else if (cameraId == ILiveConstants.BACK_CAMERA) {
                            ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
                            hostControlState.setCameraid(ILiveConstants.FRONT_CAMERA);
                        }
                    }
                });
                hostControlDialog.updateView(beautyOn,flashOn,voiceOn);
               hostControlDialog.showDiaglog(view);
            }
        });
        createLive();
        receiveMsg();
    }
   //退出直播间
    private void quitLive() {
        customCmdManager.quitLive(mRoomId, MyApplication.getApplication().getSelfProfile().getIdentifier());
        removeCurrent();
        //发送退出直播间消息给服务器

    }

    //接收消息
    private void receiveMsg() {
       // Log.i(TAG, "receiveMsg: ");
        //MessageObservable.getInstance().addObserver(this);
        //StatusObservable.getInstance().addObserver(this);
        customCmdManager.recMgs();

    }
    //创建房间
    private void createLive() {

        hostControlState = new HostControlState();
        flashLightHelper=new FlashLightHelper();
        mRoomId=getIntent().getIntExtra("roomId",-1)+100; //房间号为自增长 再加100
        if (mRoomId<1) {
            return;
        }
      //  Log.i("info1", "createLive1: "+mRoomId);
        //创建房间配置项
        ILVLiveRoomOption hostOption=new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId())
                .controlRole("LiveMaster")
                .autoFocus(true)
                .autoMic(hostControlState.isVoiceOn())
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT) //权限设置
                .cameraId(hostControlState.getCameraid())//摄像头前置后置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO); //是否开始半自动接收
        //创建房间
       // Log.i("info1", "createLive:2 ");
        ILVLiveManager.getInstance().createRoom(mRoomId, hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                mTitleView.setHostAvatar(MyApplication.getApplication().getSelfProfile());
               //开始心形动画
                startHeartAnim();
                //开始发送心跳
                startHeartBeat();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
//                Log.i("info1", "onError: "+module+" errCode:"+errCode+" errMsg:"+errMsg);
                Toast.makeText(HostLiveActivity.this, "创建房间失败", Toast.LENGTH_SHORT).show();
              removeCurrent();
            }
        });
    }

    private void startHeartBeat() {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RequestParams params=new RequestParams();
                params.put("action","heartBeat");
                params.put("roomId",String.valueOf(mRoomId));
                params.put("userId",String.valueOf(MyApplication.getApplication().getSelfProfile().getIdentifier()));
                RequestCenter.postRequest(NetConfig.ROOM, params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object object) {

                    }

                    @Override
                    public void onFailure(Object object) {

                    }
                },null);
            }
        }, 0, 4000); //4秒钟 。服务器是10秒钟去检测一次。
    }

    //开始心形动画
    private void startHeartAnim() {
   heartTimer.scheduleAtFixedRate(new TimerTask() {
       @Override
       public void run() {
           mHearLayout.post(new Runnable() {
               @Override
               public void run() {
                 if (mHearLayout!=null)
                   mHearLayout.addHeart(getRandomColor());
               }
           });
       }
   },0,500);
    }
//随机获得颜色
    private int getRandomColor() {
        return Color.rgb(heartRandom.nextInt(255),heartRandom.nextInt(255),heartRandom.nextInt(255));
    }

    @Override
    protected void initData() {
  customCmdManager =new CustomCmdManager(mChatMsgListView,mDanmuView,mGiftRepeatView,mGiftFullView,mTitleView,mVipEnterView);
        mShareRoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }


        });
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(MyApplication.getApplication().getSelfProfile().getNickName()+"的直播间");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("房间号:"+mRoomId);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
       oks.setImageUrl(MyApplication.getApplication().getSelfProfile().getFaceUrl());//网络图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment(MyApplication.getApplication().getSelfProfile().getNickName()+"的直播间");
        // 启动分享GUI
        oks.show(HostLiveActivity.this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        heartTimer.cancel();
      heartBeatTimer.cancel();
        ILVLiveManager.getInstance().onDestory();
    }


    @Override
    public void onBackPressed() {
      quitLive();
    }
}

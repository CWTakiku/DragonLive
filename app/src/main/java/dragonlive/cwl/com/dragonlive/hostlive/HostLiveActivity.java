package dragonlive.cwl.com.dragonlive.hostlive;

import android.view.View;
import android.widget.Toast;

import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

import butterknife.Bind;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.customCmdHandle.CustomCmdManager;
import dragonlive.cwl.com.dragonlive.view.BottomControlView;
import dragonlive.cwl.com.dragonlive.view.ChatMsgListView;
import dragonlive.cwl.com.dragonlive.view.ChatView;
import dragonlive.cwl.com.dragonlive.widget.SizeChangeRelative;

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


    private HostControlState hostControlState;
    private CustomCmdManager customCmdManager;

    private int mRoomId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_host_live;
    }

    @Override
    protected void initView() {

        //初始时底部控住见面可见， 聊天栏不可见
        mBottomControlView.setVisibility(View.VISIBLE);
        mChat_view.setVisibility(View.INVISIBLE);

        mHost_Live_relative.setOnSizeChangeListener(new SizeChangeRelative.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                mBottomControlView.setVisibility(View.VISIBLE);
                mChat_view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSmall() {
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

            }
        });
        mChat_view.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend( ILVCustomCmd customCmd) {
                //发送消息
              customCmdManager.sendMsg(customCmd);
            }
        });
         createLive();
        receiveMsg();
    }
   //退出直播间
    private void quitLive() {
        customCmdManager.quitLive();
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
        mRoomId=getIntent().getIntExtra("roomId",-1)+1;
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
               // Toast.makeText(HostLiveActivity.this, "roomId:"+mRoomId+" hostId:"+ILiveLoginManager.getInstance().getMyUserId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
               // Log.i("info1", "onError: "+module+" errCode:"+errCode+" errMsg:"+errMsg);
                Toast.makeText(HostLiveActivity.this, "创建房间失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initData() {
  customCmdManager =new CustomCmdManager(mChatMsgListView);
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
        ILVLiveManager.getInstance().onDestory();
       // heartTimer.cancel();
        //heartBeatTimer.cancel();
    }


}

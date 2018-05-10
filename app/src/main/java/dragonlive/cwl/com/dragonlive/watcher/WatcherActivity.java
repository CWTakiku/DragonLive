package dragonlive.cwl.com.dragonlive.watcher;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

import butterknife.Bind;
import dragonlive.cwl.com.dragonlive.customCmdHandle.CustomCmdManager;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.view.BottomControlView;
import dragonlive.cwl.com.dragonlive.view.ChatMsgListView;
import dragonlive.cwl.com.dragonlive.view.ChatView;
import dragonlive.cwl.com.dragonlive.widget.SizeChangeRelative;

/**
 * Created by cwl on 2018/5/6.
 */

public class WatcherActivity extends BaseActivity {
    @Bind(R.id.live_view)
    AVRootView mLiveView;
    @Bind(R.id.control_view)
    BottomControlView mControlView;
    @Bind(R.id.chat_view)
    ChatView mChatView;
    @Bind(R.id.bottom_view)
    FrameLayout bottomView;
    @Bind(R.id.chat_list)
    ChatMsgListView mChatMsgListView;
    @Bind(R.id.chat_list_view)
    LinearLayout chatListView;
    @Bind(R.id.size_change_layout)
    SizeChangeRelative sizeChangeLayout;

    private CustomCmdManager customCmdManager;
    private String hostId;
    private int mRoomId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watcher;
    }



    @Override
    protected void initData() {
        ILVLiveManager.getInstance().setAvVideoView(mLiveView);
        customCmdManager =new CustomCmdManager(mChatMsgListView);
        joinRoom();
        receiveMsg();
    }
    //接收消息
    private void receiveMsg() {
        customCmdManager.recMgs();
    }
   //加入房间
    private void joinRoom() {
        mRoomId = getIntent().getIntExtra("roomId", -1);
        hostId = getIntent().getStringExtra("hostId");
        if (mRoomId < 0 || TextUtils.isEmpty(hostId)) {
            return;
        }
       // Log.i("info1", "joinRoom: " + mRoomId + " hostId" + hostId);
       // Toast.makeText(this, "roomId: " + mRoomId + " userId:" + hostId, Toast.LENGTH_SHORT).show();
        //加入房间配置项
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(hostId)
                .autoCamera(false) //是否自动打开摄像头
                .controlRole("Guest") //角色设置
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO) //权限设置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO) //是否开始半自动接收
                .autoMic(false);//是否自动打开mic
        ILVLiveManager.getInstance().joinRoom(mRoomId, memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
               // Log.i("info1", "onError: " + errMsg);
                Toast.makeText(WatcherActivity.this, "直播已结束", Toast.LENGTH_SHORT).show();
                quitRoom();
            }
        });
    }

    @Override
    protected void initView() {
        sizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelative.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                //键盘隐藏
                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {
                //键盘显示
            }
        });
        mControlView.setIsHost(false);
        mControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                //点击了聊天按钮，显示聊天操作栏
                mChatView.setVisibility(View.VISIBLE);
                mControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {
                // 点击了关闭按钮，关闭直播
                quitRoom();
            }

            @Override
            public void onGiftClick() {

            }

            @Override
            public void onOptionClick(View view) {

            }
        });
        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(ILVCustomCmd customCmd) {
                //发送消息
                 customCmdManager.sendMsg(customCmd);
            }
        });
    }


    private void quitRoom() {
        //退出房间
      customCmdManager.quitRoom();
        //发送退出消息给服务器


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
        //heartTimer.cancel();
        // heartBeatTimer.cancel();
    }

}

package dragonlive.cwl.com.dragonlive.watcher;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mysdk.okhttp.exception.OkHttpException;
import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.okhttp.request.RequestParams;
import com.mysdk.util.Util;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.customCmdHandle.CustomCmdManager;
import dragonlive.cwl.com.dragonlive.model.Watchers;
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
import dragonlive.cwl.com.dragonlive.widget.GiftSelectDialog;
import dragonlive.cwl.com.dragonlive.widget.SizeChangeRelative;
import tyrantgit.widget.HeartLayout;

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
    @Bind(R.id.danmu_view)
    DanmuView mDanmuView;
    @Bind(R.id.gift_repeat_view)
    GiftRepeatView mGiftRepeatView;
    @Bind(R.id.parche_view)
    GiftFullView mGiftFullView;
    @Bind(R.id.heart_layout)
    HeartLayout mHearLayout;
    @Bind(R.id.heart_view)
    ImageView mHeart_view;
    @Bind(R.id.title_view)
    TitleView mTitleView;
    @Bind(R.id.vip_center_view)
    VipEnterView mVipEnterView;
    @Bind(R.id.share_room)
    ImageView mShreaRoomView;

    private CustomCmdManager customCmdManager;
    private GiftSelectDialog mGiftDialog;
    private String hostId;
    private int mRoomId;

    private Timer heartTimer = new Timer();
    private Timer heartBeatTimer = new Timer();
    private Random heartRandom = new Random();
    boolean zan=false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watcher;
    }


    @Override
    protected void initData() {
        ILVLiveManager.getInstance().setAvVideoView(mLiveView);
        customCmdManager = new CustomCmdManager(mChatMsgListView, mDanmuView,mGiftRepeatView,mGiftFullView,mTitleView,mVipEnterView);
        joinRoom();
        receiveMsg();
        mShreaRoomView.setOnClickListener(new View.OnClickListener() {
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
       oks.setImageUrl(MyApplication.getApplication().getSelfProfile().getFaceUrl());
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment(MyApplication.getApplication().getSelfProfile().getNickName()+"的直播间");
        // 启动分享GUI
        oks.show(WatcherActivity.this);
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
                startHeartAnim();
                mHeart_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zan=!zan;
                        if (zan) {
                            mHeart_view.setImageResource(R.drawable.redheart);

                        }else {
                            mHeart_view.setImageResource(R.drawable.whiteheart);
                        }
                    }
                });


                //更新titleView
                updateTitleView();
                //发送加入房间消息并把自己加入房间titleView
                sendEnterRoomMsg();
               //开始心跳包
                startHeartBeat();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                // Log.i("info1", "onError: " + errMsg);
                Toast.makeText(WatcherActivity.this, "直播已结束", Toast.LENGTH_SHORT).show();
                quitRoom();
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
        },0,4000);
    }

    //更新房间内成员
    private void updateTitleView() {
        //先加入主播
        List<String> list = new ArrayList<String>();
        list.add(hostId);
        TIMFriendshipManager.getInstance().getUsersProfile(list, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
             mTitleView.setHostAvatar(timUserProfiles.get(0));
            }
        });
        //加入已经进入主播间的成员
        RequestParams params=new RequestParams();
        params.put("action","getWatcher");
        params.put("roomId",String.valueOf(mRoomId));
        RequestCenter.postRequest(NetConfig.ROOM, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object object) {
                Watchers watchers= (Watchers) object;
                TIMFriendshipManager.getInstance().getUsersProfile(watchers.data, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(WatcherActivity.this, "加载成员失败，请刷新再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                         mTitleView.addWatchers(timUserProfiles);
                    }
                });
            }

            @Override
            public void onFailure(Object object) {

            }
        }, Watchers.class);

    }

    private void sendEnterRoomMsg() {

        customCmdManager.sendEnterRoomMgs();
        //发送进入房间消息给服务器
        RequestParams params=new RequestParams();
        params.put("action","join");
        params.put("userId",MyApplication.getApplication().getSelfProfile().getIdentifier());
        params.put("roomId",String.valueOf(mRoomId));
        RequestCenter.postRequest(NetConfig.ROOM, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onFailure(Object object) {
               OkHttpException e= (OkHttpException) object;
                if (e!=null){
                    if (e.getEcode()==-1&&e.getEmsg().toString().equals("empty")){  //服务器无返回JSON 已请求成功
                        return;
                    }
                }
                Toast.makeText(WatcherActivity.this, "网络连接异常，进入房间失败", Toast.LENGTH_SHORT).show();
            }
        },null);
    }

    //开始心形动画
    private void startHeartAnim() {
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHearLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mHearLayout!=null&&zan)
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
    protected void initView() {
        sizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelative.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                Log.i(TAG, "onLarge: ");
                //键盘隐藏
                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {
                Log.i(TAG, "onSmall: ");
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

                if (mGiftDialog == null) {
                    mGiftDialog = new GiftSelectDialog(WatcherActivity.this);
                    mGiftDialog.setGiftSendListener(new GiftSelectDialog.OnGiftSendListener() {
                        @Override
                        public void onGiftSendClick(ILVCustomCmd customCmd) {
                            customCmdManager.sendMsg(customCmd);
                        }

                        @Override
                        public void onCloseClick() {
                            mGiftDialog.hide();
                        }
                    });
                }
                mGiftDialog.show();
            }

            @Override
            public void onOptionClick(View view) {

            }
        });
        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {


            @Override
            public void onChatSend(ILVCustomCmd customCmd, EditText editText) {
                //发送消息
                customCmdManager.sendMsg(customCmd);
                //隐藏软键盘
                Util.hideSoftInputMethod(WatcherActivity.this,editText);
            }

            @Override
            public void onBackClick() {
              mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }
        });

    }


    private void quitRoom() {
        //退出房间
        customCmdManager.quitRoom(mRoomId, MyApplication.getApplication().getSelfProfile().getIdentifier());
      removeCurrent();


    }

    @Override
    public void onBackPressed() {
      quitRoom();
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
        if(mGiftDialog!=null) {
            mGiftDialog.dismiss();
        }
        heartTimer.cancel();
        heartBeatTimer.cancel();
        ILVLiveManager.getInstance().onDestory();
    }


}

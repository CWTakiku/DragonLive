package dragonlive.cwl.com.dragonlive.customCmdHandle;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.okhttp.request.RequestParams;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVText;

import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.model.ChatMsgInfo;
import dragonlive.cwl.com.dragonlive.model.Constants;
import dragonlive.cwl.com.dragonlive.model.GiftCmdInfo;
import dragonlive.cwl.com.dragonlive.model.GiftInfo;
import dragonlive.cwl.com.dragonlive.network.NetConfig;
import dragonlive.cwl.com.dragonlive.network.RequestCenter;
import dragonlive.cwl.com.dragonlive.view.ChatMsgListView;
import dragonlive.cwl.com.dragonlive.view.DanmuView;
import dragonlive.cwl.com.dragonlive.view.GiftFullView;
import dragonlive.cwl.com.dragonlive.view.GiftRepeatView;
import dragonlive.cwl.com.dragonlive.view.TitleView;
import dragonlive.cwl.com.dragonlive.view.VipEnterView;

/**
 * Created by cwl on 2018/5/9.
 */

public class CustomCmdManager {

   // private ILVCustomCmd customCmd;
     private ChatMsgListView mChatMsgListView;
    private DanmuView mDanmuView;
    private GiftRepeatView mGiftRepeatView;
    private GiftFullView mGiftFullView;
    private TitleView mTitleView;
    private VipEnterView mVipEnterView;
    public CustomCmdManager(ChatMsgListView chatMsgListView,DanmuView danmuView, GiftRepeatView giftRepeatView,
                            GiftFullView giftFullView,TitleView titleView,VipEnterView vipEnterView){
        mChatMsgListView=chatMsgListView;
        mDanmuView=danmuView;
        //this.customCmd=customCmd;
        mGiftRepeatView=giftRepeatView;
        mGiftFullView=giftFullView;
        mTitleView=titleView;
        mVipEnterView=vipEnterView;
    }
   public void sendMsg(final ILVCustomCmd customCmd){

       customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
       ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
           @Override
           public void onSuccess(Object data) {
               //Log.i("info1", "onSuccessSend: ");
               if (customCmd.getCmd()== Constants.CMD_CHAT_MSG_LIST){ //消息类型是发给列表
                   String chatContent = customCmd.getParam();
                   String userId= MyApplication.getApplication().getSelfProfile().getIdentifier();
                   String avatar = MyApplication.getApplication().getSelfProfile().getFaceUrl();
                   String nickNmae= MyApplication.getApplication().getSelfProfile().getNickName();
                   if (TextUtils.isEmpty(nickNmae)){
                       nickNmae=userId;
                   }
                   ChatMsgInfo msgInfo=ChatMsgInfo.createListInfo(chatContent,userId,avatar,nickNmae);
                   mChatMsgListView.addMsgInfo(msgInfo);
               }else {
                   if (customCmd.getCmd()==Constants.CMD_CHAT_MSG_DANMU){//消息类型是发给列表和弹幕的
                       String chatContent = customCmd.getParam();
                       String userId = MyApplication.getApplication().getSelfProfile().getIdentifier();
                       String avatar = MyApplication.getApplication().getSelfProfile().getFaceUrl();
                       String name = MyApplication.getApplication().getSelfProfile().getNickName();
                       if (TextUtils.isEmpty(name)) {
                           name = userId;
                       }
                       ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent,userId,avatar,name);
                       mChatMsgListView.addMsgInfo(info);
                       ChatMsgInfo danmuInfo= ChatMsgInfo.createDanmuInfo(chatContent,userId,avatar);
                       mDanmuView.addMsgInfo(danmuInfo);
                   }else {
                       if (customCmd.getCmd()==Constants.CMD_CHAT_GIFT){
                           //界面显示礼物动画。
                           GiftCmdInfo giftCmdInfo = new Gson().fromJson(customCmd.getParam(), GiftCmdInfo.class);
                           int giftId=giftCmdInfo.giftId;
                           String repeatId=giftCmdInfo.repeatId;
                           GiftInfo giftInfo=GiftInfo.getGiftById(giftId);
                           if (giftInfo==null){
                               return;
                           }
                           //可持续点击礼物
                           if (giftInfo.type==GiftInfo.Type.ContinueGift){
                               if (mGiftRepeatView!=null) {
                                   mGiftRepeatView.showGift(giftInfo, repeatId, MyApplication.getApplication().getSelfProfile());
                               }

                           }else if(giftInfo.type==GiftInfo.Type.FullScreenGift){
                               //全屏礼物
                            if (mGiftFullView!=null){
                                mGiftFullView.showGiftFull(giftInfo, MyApplication.getApplication().getSelfProfile());
                            }
                           }
                       }
                   }
               }
           }

           @Override
           public void onError(String module, int errCode, String errMsg) {
              // Log.i("info1", "error "+module+" msg: " +errMsg);
           }
       });

   }





    public  void recMgs(){

           ILVLiveConfig config=MyApplication.getApplication().getLiveConfig();

       config.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {  //内存泄漏
           @Override
           public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
               //接收到文本消息

           }

           @Override
           public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
               //接收到自定义文本消息

               if (cmd.getCmd()==Constants.CMD_CHAT_MSG_LIST){
                   String content = cmd.getParam();
                   String name=userProfile.getNickName();
                   if (!TextUtils.isEmpty(name))
                       name=userProfile.getNickName();
                   else
                       name=userProfile.getIdentifier();
                   ChatMsgInfo info=ChatMsgInfo.createListInfo(content,id,userProfile.getFaceUrl(),name);
                   mChatMsgListView.addMsgInfo(info);
               }else  if (cmd.getCmd()==Constants.CMD_CHAT_MSG_DANMU){

                   String content = cmd.getParam();
                   String name=userProfile.getNickName();
                   if (!TextUtils.isEmpty(name))
                       name=userProfile.getNickName();
                   else
                       name=userProfile.getIdentifier();
                   ChatMsgInfo info=ChatMsgInfo.createListInfo(content,id,userProfile.getFaceUrl(),name);
                   mChatMsgListView.addMsgInfo(info);

                   ChatMsgInfo danmuInfo=ChatMsgInfo.createDanmuInfo(content,id,userProfile.getFaceUrl());
                   mDanmuView.addMsgInfo(danmuInfo);
               }else  if (cmd.getCmd()==Constants.CMD_CHAT_GIFT){ //礼物消息
                   //界面显示礼物动画。
                   GiftCmdInfo giftCmdInfo = new Gson().fromJson(cmd.getParam(), GiftCmdInfo.class);
                   int giftId=giftCmdInfo.giftId;
                   String repeatId=giftCmdInfo.repeatId;
                   GiftInfo giftInfo=GiftInfo.getGiftById(giftId);
                   if (giftInfo==null){
                       return;
                   }
                   //可持续点击礼物
                   if (giftInfo.type==GiftInfo.Type.ContinueGift){
                       if (mGiftRepeatView!=null)
                           mGiftRepeatView.showGift(giftInfo,repeatId,userProfile);

                   }else if(giftInfo.type==GiftInfo.Type.FullScreenGift){
                       //全屏礼物
                       if (mGiftFullView!=null){
                           mGiftFullView.showGiftFull(giftInfo, userProfile);
                       }
                   }
               }else if (cmd.getCmd()==ILVLiveConstants.ILVLIVE_CMD_ENTER) {  //观众进入房间消息
                   //用户进入直播间
                   mTitleView.addWatcher(userProfile);
                   if (mVipEnterView!=null){
                       mVipEnterView.showVipEnter(userProfile);
                   }
               }else if (cmd.getCmd()==ILVLiveConstants.ILVLIVE_CMD_LEAVE){  //观众退出房间消息
                   //用户离开房间
                   mTitleView.removeWatcher(userProfile);
               }
           }

           @Override
           public void onNewOtherMsg(TIMMessage message) {
               //接收到其他消息

           }
       });
   }
   public void quitRoom(int mRoomId, String identifier){
       ILVCustomCmd customCmd = new ILVCustomCmd();
       customCmd.setType(ILVText.ILVTextType.eGroupMsg);
       customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_LEAVE);
       customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
       ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
           @Override
           public void onSuccess(Object data) {
               ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                   @Override
                   public void onSuccess(Object data) {

                   }

                   @Override
                   public void onError(String module, int errCode, String errMsg) {

                   }
               });
           }

           @Override
           public void onError(String module, int errCode, String errMsg) {

           }
       });
      // 发送退出消息给服务器
       RequestParams params=new RequestParams();
       params.put("action","quit");
       params.put("roomId",String.valueOf(mRoomId));
       params.put("userId",identifier);
       RequestCenter.postRequest(NetConfig.ROOM, params, new DisposeDataListener() {
           @Override
           public void onSuccess(Object object) {

           }

           @Override
           public void onFailure(Object object) {

           }
       },null);
       mTitleView.removeWatcher(MyApplication.getApplication().getSelfProfile());

   }
   public void quitLive(int roomId,String identifier){
       ILVCustomCmd customCmd = new ILVCustomCmd();
       customCmd.setType(ILVText.ILVTextType.eGroupMsg);
       customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_LEAVE);
       customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
       ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
           @Override
           public void onSuccess(Object data) {
               ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                   @Override
                   public void onSuccess(Object data) {

                   }

                   @Override
                   public void onError(String module, int errCode, String errMsg) {

                   }
               });
           }

           @Override
           public void onError(String module, int errCode, String errMsg) {

           }
       });
       RequestParams params=new RequestParams();
       params.put("action","quit");
       params.put("roomId",String.valueOf(roomId));
       params.put("userId",identifier);
       RequestCenter.postRequest(NetConfig.ROOM, params, new DisposeDataListener() {
           @Override
           public void onSuccess(Object object) {

           }

           @Override
           public void onFailure(Object object) {

           }
       },null);

   }
   //发送加入房间消息
   public void sendEnterRoomMgs(){
       ILVCustomCmd customCmd = new ILVCustomCmd();
       customCmd.setType(ILVText.ILVTextType.eGroupMsg);
       customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_ENTER);
       customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
       ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
           @Override
           public void onSuccess(Object data) {

           }

           @Override
           public void onError(String module, int errCode, String errMsg) {

           }
       });
       mTitleView.addWatcher(MyApplication.getApplication().getSelfProfile());
       if (mVipEnterView!=null){
           mVipEnterView.showVipEnter(MyApplication.getApplication().getSelfProfile());
       }
   }
}

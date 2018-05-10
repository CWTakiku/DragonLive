package dragonlive.cwl.com.dragonlive.customCmdHandle;

import android.text.TextUtils;
import android.util.Log;

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
import dragonlive.cwl.com.dragonlive.view.ChatMsgListView;

/**
 * Created by cwl on 2018/5/9.
 */

public class CustomCmdManager {

   // private ILVCustomCmd customCmd;
     private ChatMsgListView mChatMsgListView;
    public CustomCmdManager(ChatMsgListView chatMsgListView){
        mChatMsgListView=chatMsgListView;
        //this.customCmd=customCmd;
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
                       mChatMsgListView.addMsgInfo(danmuInfo);
                   }
               }
           }

           @Override
           public void onError(String module, int errCode, String errMsg) {
              // Log.i("info1", "error "+module+" msg: " +errMsg);
           }
       });


   }
   public void recMgs(){
       Log.i("info1", "recMgs: ");
           ILVLiveConfig config=MyApplication.getApplication().getLiveConfig();
       config.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {
           @Override
           public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
               //接收到文本消息
           }

           @Override
           public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
               //接收到自定义文本消息
               Log.i("info1", "onNewCustomMsg: ");
               if (cmd.getCmd()==Constants.CMD_CHAT_MSG_LIST){
                   String content = cmd.getParam();
                   String name=userProfile.getNickName();
                   if (!TextUtils.isEmpty(name))
                       name=userProfile.getNickName();
                   else
                       name=userProfile.getIdentifier();
                   ChatMsgInfo info=ChatMsgInfo.createListInfo(content,id,userProfile.getFaceUrl(),name);
                   mChatMsgListView.addMsgInfo(info);
               }else   if (cmd.getCmd()==Constants.CMD_CHAT_MSG_DANMU){

                   String content = cmd.getParam();
                   String name=userProfile.getNickName();
                   if (!TextUtils.isEmpty(name))
                       name=userProfile.getNickName();
                   else
                       name=userProfile.getIdentifier();
                   ChatMsgInfo info=ChatMsgInfo.createListInfo(content,id,userProfile.getFaceUrl(),name);
                   mChatMsgListView.addMsgInfo(info);

                   ChatMsgInfo danmuInfo=ChatMsgInfo.createDanmuInfo(content,id,userProfile.getFaceUrl());
                   mChatMsgListView.addMsgInfo(danmuInfo);
               }else  if (cmd.getCmd()==Constants.CMD_CHAT_GIFT){

               }
           }

           @Override
           public void onNewOtherMsg(TIMMessage message) {
               //接收到其他消息
           }
       });
   }
   public void quitRoom(){
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
   }
   public void quitLive(){
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
   }
}

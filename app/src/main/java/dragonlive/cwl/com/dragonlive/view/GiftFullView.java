package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tencent.TIMUserProfile;

import java.util.LinkedList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.model.GiftInfo;

/**
 * Created by cwl on 2018/5/18.
 */

public class GiftFullView extends RelativeLayout {


    private class GiftUserInfo{
        GiftInfo giftInfo;
        TIMUserProfile userProfile;
    }
    private List<GiftUserInfo> giftUserInfoList = new LinkedList<GiftUserInfo>();
    private boolean isAvaliable = false;

    private PorcheView mPorcheView;
    public GiftFullView(Context context) {
        super(context);
        init();
    }

    public GiftFullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftFullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
         isAvaliable=true;
    }
    //Show 全屏礼物
    public void showGiftFull(GiftInfo giftInfo, TIMUserProfile userProfile){
        if (giftInfo==null||giftInfo.type!=GiftInfo.Type.FullScreenGift){
            return;
        }
        if (isAvaliable){
            isAvaliable=false;
            if (giftInfo.giftId==GiftInfo.Gift_BaoShiJie.giftId){
                showProcheView(userProfile);
            }else {
                //其他全屏礼物
            }
        }else {  //加入缓存 待Show
            GiftUserInfo  giftUserInfo=new GiftUserInfo();
            giftUserInfo.giftInfo = giftInfo;
            giftUserInfo.userProfile = userProfile;
            giftUserInfoList.add(giftUserInfo);
        }
    }

    private void showProcheView(TIMUserProfile userProfile) {
        if (mPorcheView==null){
            mPorcheView=new PorcheView(getContext());
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(mPorcheView, rlp);

            mPorcheView.setOnAvaliableListener(new PorcheView.OnAvaliableListener() {
                @Override
                public void onAvaliable() {
                    isAvaliable=true;
                    if (giftUserInfoList.size()>0){
                        GiftUserInfo info= giftUserInfoList.remove(0);
                      showGiftFull(info.giftInfo,info.userProfile);
                    }
                }
            });
        }
        mPorcheView.show(userProfile);
    }
}

package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysdk.glide.GlideUtil;
import com.tencent.TIMUserProfile;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.model.GiftInfo;

/**
 * Created by cwl on 2018/5/16.
 */

public class GiftRepeatItemView extends LinearLayout {
    @Bind(R.id.user_header)
    ImageView userHeader;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.gift_name)
    TextView giftName;
    @Bind(R.id.gift_info)
    LinearLayout giftInfo;
    @Bind(R.id.gift_img)
    ImageView giftImg;
    @Bind(R.id.gift_num)
    TextView giftNum;

    private Animation viewInAnim;
    private Animation giftViewInAnim;
    private Animation textScaleAnim;

    public GiftRepeatItemView(Context context) {
        super(context);
        init();
    }

    public GiftRepeatItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftRepeatItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_gift_repeat_item, this, true);
        ButterKnife.bind(view);
        initAnim();
    }

    private void initAnim() {
        viewInAnim= AnimationUtils.loadAnimation(getContext(),R.anim.repeat_gift_view_in);
        giftViewInAnim=AnimationUtils.loadAnimation(getContext(),R.anim.repeat_gift_img_view_in);
        textScaleAnim=AnimationUtils.loadAnimation(getContext(),R.anim.repeat_gift_num_scale);

        //View 动画监听
        viewInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
                giftImg.setVisibility(INVISIBLE);
                giftNum.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                   post(new Runnable() {
                       @Override
                       public void run() {
                           //在View 显示完 再显示礼物动画
                           giftImg.startAnimation(giftViewInAnim);
                       }
                   });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //礼物动画监听
        giftViewInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                giftImg.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        //礼物动画完成后开始数字跳动 动画
                     giftNum.startAnimation(textScaleAnim);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textScaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                giftNum .setText("x" + totalNum);
                giftNum.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 if (leftNum>0){
                     leftNum--;
                     totalNum++;
                     post(new Runnable() {
                         @Override
                         public void run() {
                        giftNum.startAnimation(textScaleAnim);
                         }
                     });
                 }else {
                     setVisibility(INVISIBLE);
                     if (listener!=null){
                         //不可见 即可用
                         listener.onAvaliable();
                     }
                 }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private int giftId = -1;
    private String userId = "";
    private String repeatId = "";

    private int leftNum = 0;
    private int totalNum = 0;

    public void showGift(GiftInfo giftInfo, String repeatId, TIMUserProfile profile){
        giftId = giftInfo.giftId;
        userId = profile.getIdentifier();
        this.repeatId = repeatId;
        if (getVisibility()==INVISIBLE){
            //所有动画不可见 即可用
            totalNum=1;
            String avatarUrl = profile.getFaceUrl();
            if (TextUtils.isEmpty(avatarUrl)) {
                GlideUtil.loadLocalImageCircle(getContext(),R.drawable.default_avatar,userHeader,25);
            } else {
               GlideUtil.loadImageCircle(getContext(),avatarUrl,userHeader,25);
            }

            String nickName = profile.getNickName();
            if (TextUtils.isEmpty(nickName)) {
                nickName = profile.getIdentifier();
            }
            userName.setText(nickName);

            giftNum.setText("送出一个" + giftInfo.name);
            GlideUtil.loadLocalImage(getContext(),giftInfo.giftResId,giftImg);
            giftNum.setText("x" + 1);
            //开始动画
            post(new Runnable() {
                @Override
                public void run() {
                    startAnimation(viewInAnim);
                }
            });
        }else {
            leftNum++;
        }
    }

    public interface OnGiftItemAvaliableListener {
        void onAvaliable();
    }

    private OnGiftItemAvaliableListener listener;

    public void setOnGiftItemAvaliableListener(OnGiftItemAvaliableListener l) {
        listener = l;
    }
    //只有当 同样一个礼物 同一个用户 指定时间段 才认为可用
    public boolean isAvaliable(GiftInfo giftInfo, String repeatId, TIMUserProfile profile) {
        boolean sameGift = giftId == giftInfo.giftId;
        boolean sameRepeat = this.repeatId.equals(repeatId);
        boolean sameUser = this.userId.equals(profile.getIdentifier());
        boolean canContinue = giftInfo.type == GiftInfo.Type.ContinueGift;
        return sameGift && sameRepeat && sameUser && canContinue;
    }
}

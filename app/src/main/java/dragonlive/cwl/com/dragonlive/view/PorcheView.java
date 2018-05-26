package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysdk.glide.GlideUtil;
import com.tencent.TIMUserProfile;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;

/**
 * Created by cwl on 2018/5/18.
 */

public class PorcheView extends LinearLayout {
    @Bind(R.id.sender_avatar)
    ImageView senderAvatar;
    @Bind(R.id.sender_name)
    TextView senderName;
    @Bind(R.id.wheel_back)
    ImageView wheelBack;
    @Bind(R.id.wheel_front)
    ImageView wheelFront;

    private boolean avaliable = false;

    private AnimationDrawable drawb;
    private AnimationDrawable drawf;

    //进出动画
    private Animation inAnim;
    private Animation outAnim;
    public PorcheView(Context context) {
        super(context);
        init();
    }

    public PorcheView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PorcheView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_porche, this, true);
        ButterKnife.bind(view);
        drawb= (AnimationDrawable) wheelBack.getDrawable();
        drawb.setOneShot(false);//循环
        drawf= (AnimationDrawable) wheelFront.getDrawable();
        drawf.setOneShot(false);

        //可用
        setVisibility(INVISIBLE);
        avaliable=true;
    }
    private boolean layouted=false; //已经布局完了
    private boolean needShowAnim=false;//需要展示动画
    public void show(TIMUserProfile userProfile){

         bindUserInfo(userProfile);
        if (layouted) {
            startAnim();
        } else {
            needShowAnim = true;
        }

    }

    private void bindUserInfo(TIMUserProfile userProfile) {
       String avatar=userProfile.getFaceUrl();
        if (TextUtils.isEmpty(avatar)){
            GlideUtil.loadLocalImageCircle(getContext(),R.drawable.default_avatar,senderAvatar,30);
        }else {
            GlideUtil.loadImageCircle(getContext(),avatar,senderAvatar,30);
        }
        String name=userProfile.getNickName();
        if (TextUtils.isEmpty(name)){
            senderName.setText(userProfile.getIdentifier());
        }else {
            senderName.setText(name);
        }
    }

    private void  startAnim(){
        avaliable=false;
        int width = getWidth();
        int left = getLeft();
         inAnim=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0-(width+left)*1.0f/width, //fromX
                 Animation.RELATIVE_TO_SELF, 0,//to X
                 Animation.RELATIVE_TO_SELF, -1,//fromY
                 Animation.RELATIVE_TO_SELF, 0  //to Y
                  );
        inAnim.setDuration(2000);
        outAnim=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, //fromX
                Animation.RELATIVE_TO_SELF, (width+left)*1.0f/width,//to X
                Animation.RELATIVE_TO_SELF, 0,//fromY
                Animation.RELATIVE_TO_SELF, 1  //to Y
        );
        outAnim.setDuration(2000);
        inAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
                drawb.start();
                drawf.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       startAnimation(outAnim);
                   }
               },2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                drawb.stop();
                drawf.stop();
                setVisibility(INVISIBLE);
                avaliable=true;
                needShowAnim=false;
                if (onAvaliableListener!=null){
                    onAvaliableListener.onAvaliable();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(inAnim);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layouted=true;
        if (needShowAnim){
            startAnim();
        }
    }

    private OnAvaliableListener onAvaliableListener;

    public void setOnAvaliableListener(OnAvaliableListener l) {
        onAvaliableListener = l;
    }

    public interface OnAvaliableListener {
        void onAvaliable();
    }
}

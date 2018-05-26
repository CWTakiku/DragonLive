package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysdk.glide.GlideUtil;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.model.ChatMsgInfo;

/**
 * Created by cwl on 2018/5/13.
 */

public class DanmuItemView extends RelativeLayout {

    private ImageView mSenderAvatar;
    private TextView mSenderName;
    private TextView mChatContent;

    private TranslateAnimation translateAnim = null;

    public DanmuItemView(Context context) {
        super(context);
        init();
    }

    public DanmuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_danmu_item,this,true);
        findViews();
       translateAnim= (TranslateAnimation) AnimationUtils.loadAnimation(getContext(),R.anim.danmu_item_enter);
        translateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
              setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(INVISIBLE);
                if (onAvaliableListener!=null){
                    onAvaliableListener.onAvaliable();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void findViews() {
        mSenderAvatar = (ImageView) findViewById(R.id.user_avatar);
        mSenderName = (TextView) findViewById(R.id.user_name);
        mChatContent = (TextView) findViewById(R.id.chat_content);
    }
//移动  展示
   public void showMsg(ChatMsgInfo msgInfo){
        String avatar=msgInfo.getAvatar();
        if (TextUtils.isEmpty(avatar)){
            GlideUtil.loadLocalImageCircle(getContext(),R.drawable.default_avatar,mSenderAvatar,20);
        }else {
            GlideUtil.loadImageCircle(getContext(),avatar,mSenderAvatar,20);
        }
        mSenderName.setText(msgInfo.getSenderName());
        mChatContent.setText(msgInfo.getContent());
        post(new Runnable() {
            @Override
            public void run() {
           DanmuItemView.this.startAnimation(translateAnim);
            }
        });
    }
    private OnAvaliableListener onAvaliableListener;

    public void setOnAvaliableListener(OnAvaliableListener l) {
        onAvaliableListener = l;
    }

    public interface OnAvaliableListener {
        public void onAvaliable();
    }
}

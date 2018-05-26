package dragonlive.cwl.com.dragonlive.widget;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVText;

import java.util.ArrayList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.adapter.GiftAdapter;
import dragonlive.cwl.com.dragonlive.model.Constants;
import dragonlive.cwl.com.dragonlive.model.GiftCmdInfo;
import dragonlive.cwl.com.dragonlive.model.GiftInfo;

/**
 * Created by cwl on 2018/5/13.
 */

public class GiftSelectDialog extends TransParentNoDimDialog {

   private ViewPager giftPager;

    private  ImageView indicatorOne;

    private  ImageView indicatorTwo;

    private Button send;
    private ImageView close;


    private GiftInfo selectGiftInfo = null;



    private static List<GiftInfo> giftInfos = new ArrayList<GiftInfo>();

    static {
        giftInfos.add(GiftInfo.Gift_BingGun);
        giftInfos.add(GiftInfo.Gift_BingJiLing);
        giftInfos.add(GiftInfo.Gift_MeiGui);
        giftInfos.add(GiftInfo.Gift_PiJiu);
        giftInfos.add(GiftInfo.Gift_HongJiu);
        giftInfos.add(GiftInfo.Gift_Hongbao);
        giftInfos.add(GiftInfo.Gift_ZuanShi);
        giftInfos.add(GiftInfo.Gift_BaoXiang);
        giftInfos.add(GiftInfo.Gift_BaoShiJie);
    }

    private GiftAdapter mGiftAdapter;
    public GiftSelectDialog(Activity activity) {
        super(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_gift_select, null, false);
        setContentView(view);
        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        findViews(view);


        //GiftAdapter 继承PageAdapter 里面装了了GridView
        mGiftAdapter=new GiftAdapter(activity,send);
        initData();

    }

    private void findViews(View view) {
        giftPager= (ViewPager) view.findViewById(R.id.gift_pager);
        indicatorOne= (ImageView) view.findViewById(R.id.indicator_one);
        indicatorTwo = (ImageView) view.findViewById(R.id.indicator_two);
        send= (Button) view.findViewById(R.id.send);
        close= (ImageView) view.findViewById(R.id.close);

    }

    private void initData() {


        //加入礼物信息
        mGiftAdapter.setData(giftInfos);
        //选择礼物监听 成成包装 从最底层BaseAdapter 里回调出到GridView 到GiftAdapter 最终到Dialog0
        mGiftAdapter.setOnSelectGiftListener(new GiftAdapter.OnSelectGiftListener() {
            @Override
            public void onSelectGift(GiftInfo giftInfo) {
                selectGiftInfo=giftInfo;
               // Log.i("info1", "onSelectGift: "+giftInfo.name);
            }
        });
        giftPager.setAdapter(mGiftAdapter);
        giftPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    indicatorOne.setImageResource(R.drawable.ind_s);
                    indicatorTwo.setImageResource(R.drawable.ind_uns);
                } else if (position == 1) {
                    indicatorOne.setImageResource(R.drawable.ind_uns);
                    indicatorTwo.setImageResource(R.drawable.ind_s);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        indicatorOne.setImageResource(R.drawable.ind_s);
        indicatorTwo.setImageResource(R.drawable.ind_uns);
        send.setVisibility(View.INVISIBLE);
        //发送建监听
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Log.i("info1", "onClick: "+selectGiftInfo.name);
                if (TextUtils.isEmpty(repeatId)){
                    repeatId=System.currentTimeMillis()+"";
                }
                if (onGiftSendListener!=null){
                    ILVCustomCmd giftCmd = new ILVCustomCmd();
                    giftCmd.setCmd(Constants.CMD_CHAT_GIFT);
                    giftCmd.setType(ILVText.ILVTextType.eGroupMsg);
                    GiftCmdInfo giftCmdInfo=new GiftCmdInfo();
                    giftCmdInfo.giftId=selectGiftInfo.giftId;
                    giftCmdInfo.repeatId=repeatId;
                    giftCmd.setParam(new Gson().toJson(giftCmdInfo));
                    onGiftSendListener.onGiftSendClick(giftCmd);
                    if (selectGiftInfo.type==GiftInfo.Type.ContinueGift){
                        restartTimer();
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGiftSendListener.onCloseClick();
            }
        });
    }


    private Handler sendRepeatTimer=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           int what=msg.what;
                if (WHAT_UPDATE_TIME == what) {
                    send.setText("发送(" + leftTime + "s)");
                    sendRepeatTimer.sendEmptyMessageDelayed(WHAT_MINUTES_TIME, 250);
                } else if (WHAT_MINUTES_TIME == what) {
                    leftTime--;
                    if (leftTime > 0) {
                        send.setText("发送(" + leftTime + "s)");
                        sendRepeatTimer.sendEmptyMessageDelayed(WHAT_MINUTES_TIME, 250);
                    } else {
                        send.setText("发送");
                        repeatId = "";
                    }
                }
        }
    };

    private static final int WHAT_UPDATE_TIME = 0;
    private static final int WHAT_MINUTES_TIME = 1;
    private int leftTime = 10;
    private String repeatId = "";

    //重启时间计算
    private void restartTimer() {
        stopTimer();
        sendRepeatTimer.sendEmptyMessage(WHAT_UPDATE_TIME);
    }

    private void stopTimer() {
        sendRepeatTimer.removeMessages(WHAT_UPDATE_TIME);
        sendRepeatTimer.removeMessages(WHAT_MINUTES_TIME);
        send.setText("发送");
        leftTime = 10;
    }

    @Override
    public void show() {
        Window window=dialog.getWindow();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        super.show();
    }
   //礼物发送监听
    private OnGiftSendListener onGiftSendListener;

    public interface OnGiftSendListener {
        void onGiftSendClick(ILVCustomCmd customCmd);
        void onCloseClick();
    }

    public void setGiftSendListener(OnGiftSendListener l) {
        onGiftSendListener = l;
    }
}

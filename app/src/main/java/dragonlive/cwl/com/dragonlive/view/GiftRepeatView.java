package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.TIMUserProfile;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.model.GiftInfo;

/**
 * Created by cwl on 2018/5/16.
 */

public class GiftRepeatView extends LinearLayout {

    @Bind(R.id.item1)
    GiftRepeatItemView item1;
    @Bind(R.id.item0)
    GiftRepeatItemView item0;

    private class GiftSenderAndInfo {
        public GiftInfo giftInfo;
        public String repeatId;
        public TIMUserProfile senderProfile;
    }

    private List<GiftSenderAndInfo> giftSenderAndInfoList = new LinkedList<GiftSenderAndInfo>();

    public GiftRepeatView(Context context) {
        super(context);
        init();
    }

    public GiftRepeatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftRepeatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_gift_repeat, this, true);
        ButterKnife.bind(view);
        item0.setOnGiftItemAvaliableListener(avaliableListener);
        item1.setOnGiftItemAvaliableListener(avaliableListener);
        item0.setVisibility(INVISIBLE);
        item1.setVisibility(INVISIBLE);

    }
    private GiftRepeatItemView.OnGiftItemAvaliableListener avaliableListener = new GiftRepeatItemView.OnGiftItemAvaliableListener() {
        @Override
        public void onAvaliable() {
            //一旦有可用的GiftRepeatItemView 就从缓存数据里拿出一个去Show
            if (giftSenderAndInfoList.size() > 0) {
                GiftSenderAndInfo info = giftSenderAndInfoList.remove(0);
                showGift(info.giftInfo, info.repeatId, info.senderProfile);
            }
        }
    };

    /**
     * 展示礼物
     * @param giftInfo
     * @param repeatId
     * @param senderProfile
     */
    public void showGift(GiftInfo giftInfo, String repeatId, TIMUserProfile senderProfile) {
        GiftRepeatItemView avaliableView = getAvaliableView(giftInfo, repeatId, senderProfile);

        if (avaliableView==null){
            //如果无可用的GiftRepeatItemView
            GiftSenderAndInfo info = new GiftSenderAndInfo();
            info.giftInfo = giftInfo;
            info.senderProfile =senderProfile;
            info.repeatId = repeatId;
            giftSenderAndInfoList.add(info);
        } else {
            //展示
            avaliableView.showGift(giftInfo, repeatId, senderProfile);
        }
        }


    private GiftRepeatItemView getAvaliableView(GiftInfo giftInfo, String repeatId, TIMUserProfile profile) {

        if (item0.isAvaliable(giftInfo, repeatId, profile)) {
            return item0;
        }

        if (item1.isAvaliable(giftInfo, repeatId, profile)) {
            return item1;
        }

        if (item0.getVisibility() == INVISIBLE) {
            return item0;
        }
        if (item1.getVisibility() == INVISIBLE) {
            return item1;
        }
        return null;
    }

}

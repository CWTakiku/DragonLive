package dragonlive.cwl.com.dragonlive.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.model.GiftInfo;
import dragonlive.cwl.com.dragonlive.view.GiftGridView;

/**
 * Created by cwl on 2018/5/13.
 */

public class GiftAdapter extends PagerAdapter{
    private Button  sendBtn;
    private List<GiftGridView> pageViews = new ArrayList<GiftGridView>();
   // private GiftGridView mGiftGridView;
    private Context mContext;
    private GiftInfo selectGiftInfo = null;
    private  static List<GiftInfo> giftInfos = new ArrayList<GiftInfo>();
    public GiftAdapter(Context context,Button button){
        mContext=context;
        sendBtn=button;
    }
    public  void  setData(List<GiftInfo> gifts ){
       giftInfos=gifts;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 创建item view
        final GiftGridView itemView = new GiftGridView(mContext);
      itemView.setOnClickGiftItemListener(new GiftGridView.OnClickGiftItemListener() {
          @Override
          public void onClick(GiftInfo giftInfo) {
              //stopTimer();
             // repeatId = "";

              selectGiftInfo = giftInfo;

              if (selectGiftInfo != null) {
                  mOnSelectGiftListener.onSelectGift(selectGiftInfo);
                  sendBtn.setVisibility(View.VISIBLE);
              } else {
                  sendBtn.setVisibility(View.INVISIBLE);
              }

              for (GiftGridView giftGridView : pageViews) {
                  giftGridView.setSelectGift(selectGiftInfo);
                  giftGridView.notifyDataSetChanged();
              }
          }
      });
        int endIndex = (position + 1) * 8;
        int emptyNum = 0;
        //最后一页的边界处理
        if (endIndex > giftInfos.size()) {
            emptyNum = endIndex - giftInfos.size();
            endIndex = giftInfos.size();
        }

        List<GiftInfo> targetInfos = giftInfos.subList(position * 8, endIndex);
        //超出边界的，用空填充。保证每个页面都有item
        for (int i = 0; i < emptyNum; i++) {
            targetInfos.add(GiftInfo.Gift_Empty);
        }
        itemView.setGiftInfoList(targetInfos);

        int gridViewHeight = itemView.getGridViewHeight();

        container.addView(itemView);
        pageViews.add(itemView);

        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        layoutParams.height = gridViewHeight;
        container.setLayoutParams(layoutParams);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        pageViews.remove(object);

    }
    private OnSelectGiftListener mOnSelectGiftListener;
    public void setOnSelectGiftListener(OnSelectGiftListener l){
        mOnSelectGiftListener=l;
    }
    public interface OnSelectGiftListener{
        void onSelectGift(GiftInfo giftInfo);
    }
}

package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.adapter.GridAdapter;
import dragonlive.cwl.com.dragonlive.model.GiftInfo;

/**
 * Created by cwl on 2018/5/13.
 */

public class GiftGridView extends GridView{

    private GridAdapter gridAdapter;
    private List<GiftInfo> giftInfoList = new ArrayList<GiftInfo>();
    public GiftGridView(Context context) {
        super(context);
        init();
    }

    public GiftGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        //一行四个
        setNumColumns(4);
        gridAdapter=new GridAdapter(getContext(),giftInfoList);
        gridAdapter.setOnGiftItemClickListener(new GridAdapter.OnGiftItemClickListener() {
            @Override
            public void onClick(GiftInfo giftInfo) {
                onClickGiftItemListener.onClick(giftInfo);
            }
        });
        setAdapter(gridAdapter);
    }
    public void setSelectGift(GiftInfo gift){
        if (gridAdapter!=null)
        gridAdapter.setSelectGift(gift);
    }
    public void setGiftInfoList(List<GiftInfo> giftInfos){
        giftInfoList.clear();
        giftInfoList.addAll(giftInfos);
        gridAdapter.notifyDataSetChanged();
    }
    //通知更新数据
    public void notifyDataSetChanged() {
        gridAdapter.notifyDataSetChanged();
    }
    //得到九宫格高
    public int getGridViewHeight(){
        View view=gridAdapter.getView(0,null,this);
        view.measure(0,0);
        int height=view.getMeasuredHeight();
        return height*2;
    }
    private OnClickGiftItemListener onClickGiftItemListener;
    public void setOnClickGiftItemListener(OnClickGiftItemListener l){
        onClickGiftItemListener=l;
    }
   public interface OnClickGiftItemListener{
       void onClick(GiftInfo giftInfo);
   }
}

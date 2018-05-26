package dragonlive.cwl.com.dragonlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysdk.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.model.GiftInfo;

import static dragonlive.cwl.com.dragonlive.model.GiftInfo.Gift_Empty;

/**
 * Created by cwl on 2018/5/13.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private GiftInfo mSelectGift;
    private List<GiftInfo> giftInfoList = new ArrayList<GiftInfo>();
    public GridAdapter(Context context,List<GiftInfo> giftInfoList){
        mContext=context;
        this.giftInfoList=giftInfoList;
    }

    @Override
    public int getCount() {
        return giftInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return giftInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectGift(GiftInfo gift){
        mSelectGift=gift;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_gift_item, parent, false);
             viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        GiftInfo giftInfo=giftInfoList.get(position);
        viewHolder.bindData(giftInfo);
        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.gift_img)
        ImageView giftImg;
        @Bind(R.id.gift_exp)
        TextView giftExp;
        @Bind(R.id.gift_name)
        TextView giftName;
        @Bind(R.id.gift_select)
        ImageView giftSelect;
        private View view;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.view=view;
        }
        public void bindData(final GiftInfo giftInfo){
            GlideUtil.loadLocalImage(mContext,giftInfo.giftResId,giftImg);
            if (giftInfo != Gift_Empty) {
                giftExp.setText(giftInfo.expValue + "经验值");
                giftName.setText(giftInfo.name);
                if (giftInfo ==  mSelectGift) {
                    giftSelect.setImageResource(R.drawable.gift_selected);
                } else {
                    if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                        giftSelect.setImageResource(R.drawable.gift_repeat);
                    } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                        giftSelect.setImageResource(R.drawable.gift_none);
                    }
                }
            } else {
                giftExp.setText("");
                giftName.setText("");
                giftSelect.setImageResource(R.drawable.gift_none);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (giftInfo == Gift_Empty) {
                        return;
                    }

                    if (giftInfo == mSelectGift) {
                        if (mOnGiftItemClickListener != null) {
                            mOnGiftItemClickListener.onClick(null);
                        }
                    } else {
                        if (mOnGiftItemClickListener != null) {
                            mOnGiftItemClickListener.onClick(giftInfo);
                        }
                    }
                }
            });
        }
    }
    private OnGiftItemClickListener mOnGiftItemClickListener;

    public void setOnGiftItemClickListener(OnGiftItemClickListener l) {
        mOnGiftItemClickListener = l;
    }

    public interface OnGiftItemClickListener {
        void onClick(GiftInfo giftInfo);
    }
}

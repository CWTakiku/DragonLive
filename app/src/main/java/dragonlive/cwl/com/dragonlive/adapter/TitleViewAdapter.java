package dragonlive.cwl.com.dragonlive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mysdk.glide.GlideUtil;
import com.tencent.TIMUserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dragonlive.cwl.com.dragonlive.R;

/**
 * Created by cwl on 2018/5/22.
 */

public class TitleViewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List< TIMUserProfile> watchersUserInfo=new ArrayList<TIMUserProfile>();
    private Map<String , TIMUserProfile> watchersMap = new HashMap<String , TIMUserProfile>();
    public TitleViewAdapter(Context context){mContext=context;}
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView= LayoutInflater.from(mContext).inflate(R.layout.adapter_watcher,parent,false);
        WatcherHolder holder=new WatcherHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          if (holder instanceof WatcherHolder){
              ((WatcherHolder) holder).bindData(watchersUserInfo.get(position));
          }
    }

    @Override
    public int getItemCount() {
        return watchersUserInfo.size();
    }
    private  class WatcherHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public WatcherHolder(View itemView) {
            super(itemView);
          imageView= (ImageView) itemView.findViewById(R.id.user_avatar);
        }
        public void bindData(final TIMUserProfile userProfile){
          String avatar=userProfile.getFaceUrl();
            if (TextUtils.isEmpty(avatar)){
                GlideUtil.loadLocalImageCircle(mContext,R.drawable.default_avatar,imageView,30);
            }else {
                GlideUtil.loadImageCircle(mContext,avatar,imageView,30);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (mOnClickUserListener!=null){
                        mOnClickUserListener.onclick(userProfile.getIdentifier());
                    }

                }
            });
        }
    }

    public  void addWatcher(TIMUserProfile userProfile){
        if (userProfile!=null){
            boolean isExist=watchersMap.containsKey(userProfile.getIdentifier());
            if (!isExist){
                watchersUserInfo.add(userProfile);
                watchersMap.put(userProfile.getIdentifier(),userProfile);
                if (mOnClickUserListener!=null){
                    mOnClickUserListener.addNums(1);
                }
                notifyDataSetChanged();
            }
        }
    }
    public  void addWatchers(List<TIMUserProfile> userProfiles){
        if (userProfiles!=null) {
            for (TIMUserProfile userProfile : userProfiles) {
                boolean isExist = watchersMap.containsKey(userProfile.getIdentifier());
                if (!isExist) {
                    watchersUserInfo.add(userProfile);
                    watchersMap.put(userProfile.getIdentifier(), userProfile);
                }
            }
            if (mOnClickUserListener!=null){
                mOnClickUserListener.addNums(userProfiles.size());
            }
            notifyDataSetChanged();
        }
    }
    public void removeWatcher(TIMUserProfile userProfile){
        if (userProfile!=null){
           TIMUserProfile targetUser=watchersMap.get(userProfile.getIdentifier());
            if (targetUser!=null){
                watchersUserInfo.remove(targetUser);
                watchersMap.remove(userProfile.getIdentifier());
                if (mOnClickUserListener!=null){
                    mOnClickUserListener.addNums(-1);
                }
                notifyDataSetChanged();
            }
        }
    }
    public void removeWatchers(List<TIMUserProfile> userProfiles){
        if (userProfiles!=null) {
            for (TIMUserProfile userProfile : userProfiles) {
                TIMUserProfile targetUser = watchersMap.get(userProfile.getIdentifier());
                if (targetUser != null) {
                    watchersUserInfo.remove(targetUser);
                    watchersMap.remove(userProfile.getIdentifier());

                }
            }
            if (mOnClickUserListener!=null){
                mOnClickUserListener.addNums(-1);
            }
            notifyDataSetChanged();
        }
    }
    private onClickUserListener mOnClickUserListener;
    public void setonClickUserListener(onClickUserListener l ){
        mOnClickUserListener=l;
    }
    public interface onClickUserListener{
        void onclick(String id);
        void addNums(int num);
    }


}

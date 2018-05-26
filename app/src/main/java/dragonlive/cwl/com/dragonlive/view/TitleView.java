package dragonlive.cwl.com.dragonlive.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysdk.glide.GlideUtil;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.adapter.TitleViewAdapter;
import dragonlive.cwl.com.dragonlive.widget.UserInfoDialog;

/**
 * Created by cwl on 2018/5/22.
 */

public class TitleView extends LinearLayout {
    @Bind(R.id.host_avatar)
    ImageView hostAvatar;
    @Bind(R.id.watch_nums)
    TextView mWatchNums;
    @Bind(R.id.watch_list)
    RecyclerView watchList;
  private int watcherNums=0;
  private TitleViewAdapter mTitleViewAdapter;
    private UserInfoDialog mUserInfoDialog;

    private String hostId;

    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.titile_view, this, true);
        ButterKnife.bind(view);

        hostAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO showDialog
                showUserInfoDialog(hostId);

            }
        });
        watchList.setHasFixedSize(true);
        //设置水平滚动
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        watchList.setLayoutManager(manager);
        //设置Adapter
        mTitleViewAdapter=new TitleViewAdapter(getContext());
        watchList.setAdapter(mTitleViewAdapter);
        mTitleViewAdapter.setonClickUserListener(new TitleViewAdapter.onClickUserListener() {
            @Override
            public void onclick(String id) {

                showUserInfoDialog(id);
            }

            @Override
            public void addNums(int num) {
                watcherNums+=num;
                mWatchNums.setText("观看人数:"+watcherNums);
            }

        });
    }
   public void addWatcher(TIMUserProfile userProfile){
       mTitleViewAdapter.addWatcher(userProfile);


   }
   public void addWatchers(List<TIMUserProfile> userProfiles){
       mTitleViewAdapter.addWatchers(userProfiles);

   }
   public void removeWatcher(TIMUserProfile userProfile){
       mTitleViewAdapter.removeWatcher(userProfile);
   }

   public void setHostAvatar(TIMUserProfile userProfile){
       if (userProfile!=null){
           hostId=userProfile.getIdentifier();
           String avatar=userProfile.getFaceUrl();
           if (TextUtils.isEmpty(avatar)){
               GlideUtil.loadLocalImageCircle(getContext(),R.drawable.default_avatar,hostAvatar,30);
           }else {
               GlideUtil.loadImageCircle(getContext(),avatar,hostAvatar,30);
           }
       }
   }
   public void showUserInfoDialog(String id){
       Context context = TitleView.this.getContext();
       if (mUserInfoDialog==null){
           if (context instanceof Activity) {
               mUserInfoDialog = new UserInfoDialog((Activity) context);
           }
       }
       List<String> ids=new ArrayList<String>();
       ids.add(id);
       TIMFriendshipManager.getInstance().getUsersProfile(ids, new TIMValueCallBack<List<TIMUserProfile>>() {
           @Override
           public void onError(int i, String s) {

           }

           @Override
           public void onSuccess(List<TIMUserProfile> userProfiles) {
             mUserInfoDialog.bindDataToViews(userProfiles.get(0));
               mUserInfoDialog.show();
           }
       });
   }

}

package dragonlive.cwl.com.dragonlive.createroom;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mysdk.glide.GlideUtil;
import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.okhttp.request.RequestParams;
import com.tencent.TIMUserProfile;

import java.io.File;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.hostlive.HostLiveActivity;
import dragonlive.cwl.com.dragonlive.model.RoomInfoModel;
import dragonlive.cwl.com.dragonlive.network.NetConfig;
import dragonlive.cwl.com.dragonlive.network.RequestCenter;
import dragonlive.cwl.com.dragonlive.util.AliYunOssHelper;
import dragonlive.cwl.com.dragonlive.util.PicChooseHelper;


public class CreateLiveActivity extends AppCompatActivity {
    private View mSetCoverView;
    private ImageView mCoverImg;
    private TextView mCoverTipTxt;
    private EditText mTitleEt;
    private TextView mCreateRoomBtn;
    private TextView mRoomNoText;
    private String coverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        findAllViews();
        setListeners();
        setupTitlebar();

    }

    private void findAllViews() {
        mSetCoverView = findViewById(R.id.set_cover);
        mCoverImg = (ImageView) findViewById(R.id.cover);
        mCoverTipTxt = (TextView) findViewById(R.id.tv_pic_tip);
        mTitleEt = (EditText) findViewById(R.id.title);
        mCreateRoomBtn = (TextView) findViewById(R.id.create);
        mRoomNoText = (TextView) findViewById(R.id.room_no);
    }

    private void setListeners() {
        mSetCoverView.setOnClickListener(clickListener);
        mCreateRoomBtn.setOnClickListener(clickListener);
    }

    private void setupTitlebar() {
        Toolbar titlebar = (Toolbar) findViewById(R.id.titlebar);
        titlebar.setTitle("<  开始我的直播");
        titlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        titlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titlebar);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.create) {
                //创建直播
               requestCreateRoom();
            } else if (id == R.id.set_cover) {
                //选择图片
                choosePic();
            }
        }
    };

    private void requestCreateRoom() {

        TIMUserProfile selfProfile = MyApplication.getApplication().getSelfProfile();
       //上传封面到阿里OSS
        AliYunOssHelper aliOssHelper=new AliYunOssHelper();
        aliOssHelper.setOnResultListener(new AliYunOssHelper.OnResultListener() {
            @Override
            public void onSuccess(String url) {
                coverUrl=url;
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(CreateLiveActivity.this, "设置封面失败", Toast.LENGTH_SHORT).show();
            }
        });
        String name = selfProfile.getIdentifier()+ "_" + System.currentTimeMillis() + "_cover";
        aliOssHelper.uploadToOSS("dragonlive",name,coverPath);
      //发送创建Room请求
        RequestParams params=new RequestParams();
        params.put("action","create");
        params.put("userId",selfProfile.getIdentifier());
        params.put("userAvatar",selfProfile.getFaceUrl());
        String nickName = selfProfile.getNickName();
        params.put("userName",TextUtils.isEmpty(nickName) ? selfProfile.getIdentifier() : nickName);
        params.put("liveTitle", mTitleEt.getText().toString());
        params.put("liveCover",coverUrl);
        RequestCenter.postRequest(NetConfig.ROOM, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object object) {
                RoomInfoModel roomInfoModel= (RoomInfoModel) object;
               // Log.i("info1", "onSuccess: roomid"+ roomInfoModel.getData().roomId+" userid"+roomInfoModel.getData().userId);
               // Toast.makeText(CreateLiveActivity.this, "请求成功"+roomInfoModel.getData().roomId, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(CreateLiveActivity.this,HostLiveActivity.class);
                intent.putExtra("roomId",roomInfoModel.getData().roomId);
                startActivity(intent);
               finish();
                //Log.i("info1", "onSuccess: "+roomInfo.roomId);
            }

            @Override
            public void onFailure(Object object) {
               // Log.i("info1", "onFailure: sb"+object.toString());
                Toast.makeText(CreateLiveActivity.this, "创建房间失败", Toast.LENGTH_SHORT).show();

            }
        }, RoomInfoModel.class);

    }


    private PicChooseHelper mPicChooserHelper;

    private void choosePic() {
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooseHelper(this, PicChooseHelper.PicType.Cover);
            mPicChooserHelper.setOnChooseResultListener(new PicChooseHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String path) {
                    //获取图片成功
                    updateCover(path);
                }

                @Override
                public void onFail(String msg) {
                    //获取图片失败
                    Toast.makeText(CreateLiveActivity.this, "选择失败：" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        mPicChooserHelper.showPicChooseDialog();
    }

    private String coverPath = null;

    private void updateCover(String path) {

        coverPath = path;
        mCoverTipTxt.setVisibility(View.GONE);
        File file=new File(path);
        if (file.exists()) {
            Uri uri = Uri.fromFile(new File(path));
            mCoverImg.setImageURI(uri);
            return;
        }
        GlideUtil.loadLocalImage(this,R.drawable.default_avatar,mCoverImg);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPicChooserHelper!=null)
        mPicChooserHelper.diaglogDismiss();
        super.onDestroy();

    }
}

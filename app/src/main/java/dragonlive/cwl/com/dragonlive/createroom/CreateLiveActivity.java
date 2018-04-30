package dragonlive.cwl.com.dragonlive.createroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mysdk.glide.GlideUtil;
import com.tencent.TIMUserProfile;

import java.io.File;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.application.MyApplication;
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
        titlebar.setTitle("开始我的直播");
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
       // Log.i("info1", "updateCover: "+path);
        coverPath = path;
        mCoverTipTxt.setVisibility(View.GONE);
        File file=new File(path);
       // Log.i("info1", "updateCover: "+file.getName());
        GlideUtil.loadLocalFileImage(this,file,mCoverImg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

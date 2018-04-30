package dragonlive.cwl.com.dragonlive;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import butterknife.Bind;
import dragonlive.cwl.com.dragonlive.common.BaseActivity;
import dragonlive.cwl.com.dragonlive.createroom.CreateLiveActivity;
import dragonlive.cwl.com.dragonlive.editprofile.EditProfileFragment;
import dragonlive.cwl.com.dragonlive.livelist.LiveListFragment;

public class MainActivity extends BaseActivity {


    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @Bind(R.id.fragment_tabhost)
    FragmentTabHost mTabHost;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
         setTabs();
    }

    private void setTabs() {
        mTabHost.setup(this,getSupportFragmentManager(),R.id.fragment_container);

        {
            TabHost.TabSpec profileTab=mTabHost.newTabSpec("livelive").setIndicator(getIndicator(R.drawable.tab_livelist));
            mTabHost.addTab(profileTab,LiveListFragment.class,null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
        {
            TabHost.TabSpec profileTab=mTabHost.newTabSpec("createlive").setIndicator(getIndicator(R.drawable.tab_publish_live));
            mTabHost.addTab(profileTab,null,null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
        {
            TabHost.TabSpec profile=mTabHost.newTabSpec("profile").setIndicator(getIndicator(R.drawable.tab_profile));
            mTabHost.addTab(profile,EditProfileFragment.class,null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
     mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent();
             intent.setClass(MainActivity.this,CreateLiveActivity.class);
             startActivity(intent);
         }
     });
    }

    private View getIndicator(int resId) {
      View view= LayoutInflater.from(this).inflate(R.layout.view_indicator,null);
        ImageView tabImg= (ImageView) view.findViewById(R.id.tab_icon);
        tabImg.setImageResource(resId);
        return view;
    }

    @Override
    protected void initData() {

    }


}

package dragonlive.cwl.com.dragonlive.livelist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mysdk.okhttp.listener.DisposeDataListener;
import com.mysdk.okhttp.request.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.adapter.LiveListAdapter;
import dragonlive.cwl.com.dragonlive.common.BaseFragment;
import dragonlive.cwl.com.dragonlive.model.ListRoomInfoModel;
import dragonlive.cwl.com.dragonlive.network.NetConfig;
import dragonlive.cwl.com.dragonlive.network.RequestCenter;


/**
 * Created by cwl on 2018/4/29.
 */

public class LiveListFragment extends BaseFragment {
    @Bind(R.id.titlebar)
    Toolbar titlebar;
    @Bind(R.id.live_list)
    ListView mLiveList;
    @Bind(R.id.swipe_refresh_layout_list)
    SwipeRefreshLayout mSwipeRefreshLayoutList;
    @Bind(R.id.activity_live_list)
    RelativeLayout activityLiveList;

    private LiveListAdapter mLiveListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_live_list, null, false);
        ButterKnife.bind(this, view);
        setData();
        requestLiveList();
        return view;
    }

    private void setData() {
        titlebar.setTitle("热播列表");
        titlebar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(titlebar);
           mLiveListAdapter=new  LiveListAdapter(getActivity());
        mLiveList.setAdapter(mLiveListAdapter);
        mSwipeRefreshLayoutList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestLiveList();
            }
        });
    }




    //请求直播列表
    private void requestLiveList() {
        RequestParams params=new RequestParams();
        params.put("action","getList");
        params.put("pageIndex","0");
        RequestCenter.postRequest(NetConfig.Room, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object object) {
                ListRoomInfoModel listRoominfos= (ListRoomInfoModel) object;


                mLiveListAdapter.removeAllRoomInfos();
                mLiveListAdapter.addRoomInfos(listRoominfos.getData());

                mSwipeRefreshLayoutList.setRefreshing(false);
            }

            @Override
            public void onFailure(Object object) {
                Log.i("info1", "onFailure: "+ object.toString());
                Toast.makeText(getActivity(), "请求列表失败", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayoutList.setRefreshing(false);
            }
        },ListRoomInfoModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

package dragonlive.cwl.com.dragonlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysdk.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.model.RoomInfo;
import dragonlive.cwl.com.dragonlive.watcher.WatcherActivity;

/**
 * Created by cwl on 2018/5/5.
 */

public class LiveListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RoomInfo> liveRooms = new ArrayList<RoomInfo>();
    public LiveListAdapter(Context context){
        mContext=context;
    }

    public void removeAllRoomInfos() {
        liveRooms.clear();
    }

    public void addRoomInfos(List<RoomInfo> roomInfos) {
        if (roomInfos != null) {
            liveRooms.clear();
            liveRooms.addAll(roomInfos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
       // Log.i("info1", "getCount: "+liveRooms.size());
        return liveRooms.size();

    }

    @Override
    public Object getItem(int position) {
        return liveRooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Log.i("info1", "getView: ");
        RoomHolder holder = null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_live_list,null);
            holder=new RoomHolder(convertView);
            convertView.setTag(holder);
        }else {
           holder= (RoomHolder) convertView.getTag();
        }
        holder.bindData(liveRooms.get(position));
        return convertView;
    }

    private class RoomHolder {
        View itemView;
        TextView liveTitle;
        ImageView liveCover;
        ImageView hostAvatar;
        TextView hostName;
        TextView watchNums;

        public RoomHolder(View view) {
            itemView = view;
            liveTitle = (TextView) view.findViewById(R.id.live_title);
            liveCover = (ImageView) view.findViewById(R.id.live_cover);
            hostName = (TextView) view.findViewById(R.id.host_name);
            hostAvatar = (ImageView) view.findViewById(R.id.host_avatar);
            watchNums = (TextView) view.findViewById(R.id.watch_nums);
        }

        public void bindData(final RoomInfo roomInfo) {
            String userName = roomInfo.userName;
            if (TextUtils.isEmpty(userName)) {
                userName = roomInfo.userId;
            }
            hostName.setText(userName);

            String liveTitleStr = roomInfo.liveTitle;
            if (TextUtils.isEmpty(liveTitleStr)) {
                this.liveTitle.setText(userName + "的直播");
            } else {
                this.liveTitle.setText(liveTitleStr);
            }
            String url = roomInfo.liveCover;
            if (TextUtils.isEmpty(url)) {
                GlideUtil.loadLocalImage(mContext, R.drawable.default_cover, liveCover);
            } else {
                GlideUtil.loadImageView(mContext, url, liveCover);
            }

            String avatar = roomInfo.userAvatar;
            if (TextUtils.isEmpty(avatar)) {
                GlideUtil.loadLocalImageCircle(mContext, R.drawable.default_avatar, hostAvatar, 50);
            } else {
                GlideUtil.loadImageCircle(mContext, avatar, hostAvatar, 50);
            }

            int watchers = roomInfo.watcherNums;
            String watchText = watchers + "人\r\n正在看";
            watchNums.setText(watchText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, WatcherActivity.class);
                    intent.putExtra("roomId", roomInfo.roomId+100);//因为 在创建直播间号的时候已经加了一百 所以这里需要跟着加100
                    intent.putExtra("hostId", roomInfo.userId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

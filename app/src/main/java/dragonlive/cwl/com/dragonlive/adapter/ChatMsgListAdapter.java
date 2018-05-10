package dragonlive.cwl.com.dragonlive.adapter;

import android.content.Context;
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
import dragonlive.cwl.com.dragonlive.model.ChatMsgInfo;

/**
 * Created by cwl on 2018/5/9.
 */

public class ChatMsgListAdapter extends BaseAdapter {

    private List<ChatMsgInfo> mChatMsgInfos = new ArrayList<ChatMsgInfo>();
    private Context mContext;
    public ChatMsgListAdapter(Context context){
        mContext=context;
    }

    public void addMsgInfo(ChatMsgInfo info) {
        if (info != null) {
            mChatMsgInfos.add(info);
            notifyDataSetChanged();
        }
    }

    public void addMsgInfos(List<ChatMsgInfo> infos) {
        if (infos != null) {
            mChatMsgInfos.addAll(infos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mChatMsgInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatMsgInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.i("info1", "getView: ");
        ViewHolder viewHolder=null;
        if (convertView==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_msg_list_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.bindData(mChatMsgInfos.get(position));
        return convertView;
    }
    private class ViewHolder{
        private ImageView avatar;
        private TextView content;

        public ViewHolder(View itemView){
            avatar = (ImageView) itemView.findViewById(R.id.sender_avatar);
            content = (TextView) itemView.findViewById(R.id.chat_content);
        }

        public void  bindData(ChatMsgInfo info){
            if (!TextUtils.isEmpty(info.getAvatar())) {
                GlideUtil.loadImageCircle(mContext, info.getAvatar(), avatar, 20);
            }else {
                GlideUtil.loadLocalImageCircle(mContext,R.drawable.default_avatar,avatar,20);
            }
            content.setText(info.getContent());
        }
    }
}

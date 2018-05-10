package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.adapter.ChatMsgListAdapter;
import dragonlive.cwl.com.dragonlive.model.ChatMsgInfo;

/**
 * Created by cwl on 2018/5/9.
 */

public class ChatMsgListView extends RelativeLayout {

    private ListView mChatMsgListView;
    private ChatMsgListAdapter mMsgListAdapter;
    public ChatMsgListView(Context context) {
        super(context);
        init();
    }

    public ChatMsgListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatMsgListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat_msg_list,this,true);
        mChatMsgListView= (ListView) findViewById(R.id.chat_msg_list);
        mMsgListAdapter=new ChatMsgListAdapter(getContext());
        mChatMsgListView.setAdapter(mMsgListAdapter);
        mChatMsgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatMsgInfo msgInfo= (ChatMsgInfo) mMsgListAdapter.getItem(position);
                showUserInfoDialog(msgInfo.getSenderId());
            }
        });

    }

    private void showUserInfoDialog(String senderId) {
        List<String> ids = new ArrayList<String>();
        ids.add(senderId);
        TIMFriendshipManager.getInstance().getUsersProfile(ids, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {

            }
        });
    }

    public void addMsgInfo(ChatMsgInfo info){
        if (info!=null) {
           // Log.i("info1", "addMsgInfo: ");
            mMsgListAdapter.addMsgInfo(info);
            mChatMsgListView.smoothScrollToPosition(mMsgListAdapter.getCount());
        }
    }

}

package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVText;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;
import dragonlive.cwl.com.dragonlive.model.Constants;

/**
 * Created by cwl on 2018/5/8.
 */

public class ChatView extends LinearLayout {
    @Bind(R.id.switch_chat_type)
    CheckBox switchChatType;
    @Bind(R.id.chat_content_edit)
    EditText chatContentEdit;
    @Bind(R.id.chat_send)
    TextView chatSend;
    @Bind(R.id.chat_back)
    TextView back;

    public ChatView(Context context) {
        super(context);
        init();
    }

    public ChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        int paddingPx = (int) (getResources().getDisplayMetrics().density * 10 + 0.5f);
        setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        setBackgroundColor(Color.parseColor("#ccffffff"));

        View view= LayoutInflater.from(getContext()).inflate(R.layout.view_chat, this, true);
        ButterKnife.bind(view);//绑定view

        switchChatType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    chatContentEdit.setHint("发送弹幕聊天消息");
                }else {
                    chatContentEdit.setHint("和大家聊点什么吧");
                }
            }
        });
        switchChatType.setChecked(false);
        chatContentEdit.setHint("和大家聊点什么吧");
        chatSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 sendChatMsg();
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChatSendListener!=null){
                    mOnChatSendListener.onBackClick();
                }
            }
        });
    }

    private void sendChatMsg() {
        if (mOnChatSendListener!=null){
            ILVCustomCmd customCmd=new ILVCustomCmd();
            customCmd.setType(ILVText.ILVTextType.eGroupMsg);
            boolean isBarrage=switchChatType.isChecked();
            if (isBarrage){
                customCmd.setCmd(Constants.CMD_CHAT_MSG_DANMU);
            }else {
                customCmd.setCmd(Constants.CMD_CHAT_MSG_LIST);
            }
            customCmd.setParam(chatContentEdit.getText().toString());
            mOnChatSendListener.onChatSend(customCmd,chatContentEdit);
        }
    }

    private OnChatSendListener mOnChatSendListener;

    public void setOnChatSendListener(OnChatSendListener l) {
        mOnChatSendListener = l;
    }

    public interface OnChatSendListener {
        public void onChatSend(ILVCustomCmd msg,EditText editText);
        void onBackClick();
    }
}

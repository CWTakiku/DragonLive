package dragonlive.cwl.com.dragonlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import dragonlive.cwl.com.dragonlive.R;

/**
 * Created by cwl on 2018/5/8.
 */

public class BottomControlView extends RelativeLayout {


    @Bind(R.id.chat)
    ImageView chat;
    @Bind(R.id.close)
    ImageView close;
    @Bind(R.id.gift)
    ImageView gift;
    @Bind(R.id.option)
    ImageView option;


    public BottomControlView(Context context) {
        super(context);
        init();
    }

    public BottomControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_control, this, true);
         ButterKnife.bind(view);

        chat.setOnClickListener(clickListener);
        close.setOnClickListener(clickListener);
        gift.setOnClickListener(clickListener);
        option.setOnClickListener(clickListener);
    }
    public void setIsHost(boolean isHost) {
        if (isHost) {
            gift.setVisibility(INVISIBLE);
            option.setVisibility(VISIBLE);
        } else {
            option.setVisibility(INVISIBLE);
            gift.setVisibility(VISIBLE);
        }
    }
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.chat:
                    if (onControlListener != null)
                        onControlListener.onChatClick();
                    break;
                case R.id.close:
                    if (onControlListener != null)
                        onControlListener.onCloseClick();
                    break;
                case R.id.gift:
                    if (onControlListener != null)
                        onControlListener.onGiftClick();
                    break;
                case R.id.option:
                    if (onControlListener != null)
                        onControlListener.onOptionClick(v);
                    break;
            }
        }
    };
    private OnControlListener onControlListener;

    public void setOnControlListener(OnControlListener l) {
        onControlListener = l;
    }

    public interface OnControlListener {

        public void onChatClick();

        public void onCloseClick();

        public void onGiftClick();

        public void onOptionClick(View view);

    }
}


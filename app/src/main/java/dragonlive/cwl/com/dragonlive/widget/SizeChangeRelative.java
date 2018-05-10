package dragonlive.cwl.com.dragonlive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by cwl on 2018/5/9.
 */

public class SizeChangeRelative extends RelativeLayout{
    public SizeChangeRelative(Context context) {
        super(context);


    }

    public SizeChangeRelative(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SizeChangeRelative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h<oldh){
            //键盘显示 画面变小
            mOnSizeChangeListener.onSmall();
        }else {
            //键盘隐藏，画面变大
            mOnSizeChangeListener.onLarge();
        }
    }
    private OnSizeChangeListener mOnSizeChangeListener;

    public void setOnSizeChangeListener(OnSizeChangeListener l) {
        mOnSizeChangeListener = l;
    }

    public interface OnSizeChangeListener {
        public void onLarge();

        public void onSmall();
    }
}

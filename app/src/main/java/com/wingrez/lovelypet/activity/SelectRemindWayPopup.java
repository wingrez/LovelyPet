package com.wingrez.lovelypet.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wingrez.lovelypet.R;

public class SelectRemindWayPopup implements OnClickListener {
    private TextView tv_vibrate, tv_ring;
    public PopupWindow mPopupWindow;
    private SelectRemindWayPopupOnClickListener selectRemindWayPopupListener;

    private Context mContext;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    public SelectRemindWayPopup(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initViews());

        // TODO: 2019/6/29 mPopupWindow区域外点击不消失

    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.selectremindway_pop_window, null);

        tv_vibrate = (TextView) view.findViewById(R.id.tv_vibrate);
        tv_vibrate.setOnClickListener(this);
        tv_ring = (TextView) view.findViewById(R.id.tv_ring);
        tv_ring.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_vibrate:
                selectRemindWayPopupListener.obtainMessage(0);
                break;
            case R.id.tv_ring:
                selectRemindWayPopupListener.obtainMessage(1);
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface SelectRemindWayPopupOnClickListener {
        void obtainMessage(int flag);
    }

    public void setOnSelectRemindWayPopupListener(SelectRemindWayPopupOnClickListener l) {
        this.selectRemindWayPopupListener = l;
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三、第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
}

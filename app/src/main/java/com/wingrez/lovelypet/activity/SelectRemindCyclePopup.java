package com.wingrez.lovelypet.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

public class SelectRemindCyclePopup implements OnClickListener {
    private TextView tv_cycle_mon; //周一
    private TextView tv_cycle_tue; //周二
    private TextView tv_cycle_wed; //周三
    private TextView tv_cycle_thu; //周四
    private TextView tv_cycle_fri; //周五
    private TextView tv_cycle_sat; //周六
    private TextView tv_cycle_sun; //周日
    private TextView tv_cycle_everyday; //每天
    private TextView tv_cycle_once; //一次
    private TextView tv_cycle_sure; //确定

    public PopupWindow mPopupWindow;
    private SelectRemindCyclePopupOnClickListener selectRemindCyclePopupListener;
    private Context mContext;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    /**
     * 构造方法
     *
     * @param context
     */
    public SelectRemindCyclePopup(Context context) {

        mContext = context;
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initViews());

        // TODO: 2019/6/29 mPopupWindow区域外点击不消失
    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.selectremindcycle_pop_window, null);
        tv_cycle_once = (TextView) view.findViewById(R.id.tv_cycle_once);
        tv_cycle_everyday = (TextView) view.findViewById(R.id.tv_cycle_everyday);
        tv_cycle_mon = (TextView) view.findViewById(R.id.tv_cycle_mon);
        tv_cycle_tue = (TextView) view.findViewById(R.id.tv_cycle_tue);
        tv_cycle_wed = (TextView) view.findViewById(R.id.tv_cycle_wed);
        tv_cycle_thu = (TextView) view.findViewById(R.id.tv_cycle_thu);
        tv_cycle_fri = (TextView) view.findViewById(R.id.tv_cycle_fri);
        tv_cycle_sat = (TextView) view.findViewById(R.id.tv_cycle_sat);
        tv_cycle_sun = (TextView) view.findViewById(R.id.tv_cycle_sun);
        tv_cycle_sure = (TextView) view.findViewById(R.id.tv_cycle_sure);

        tv_cycle_once.setOnClickListener(this);
        tv_cycle_mon.setOnClickListener(this);
        tv_cycle_tue.setOnClickListener(this);
        tv_cycle_wed.setOnClickListener(this);
        tv_cycle_thu.setOnClickListener(this);
        tv_cycle_fri.setOnClickListener(this);
        tv_cycle_sat.setOnClickListener(this);
        tv_cycle_sun.setOnClickListener(this);
        tv_cycle_sure.setOnClickListener(this);
        tv_cycle_everyday.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Drawable nav_right = mContext.getResources().getDrawable(R.drawable.cycle_check);
        nav_right.setBounds(0, 0, nav_right.getMinimumWidth(), nav_right.getMinimumHeight());
        switch (v.getId()) {
            case R.id.tv_cycle_once:
                selectRemindCyclePopupListener.obtainMessage(9, "");
                break;
            case R.id.tv_cycle_everyday:
                selectRemindCyclePopupListener.obtainMessage(8, "");
                break;
            case R.id.tv_cycle_mon:
                if (tv_cycle_mon.getCompoundDrawables()[2] == null)
                    tv_cycle_mon.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_mon.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(0, "");
                break;
            case R.id.tv_cycle_tue:
                if (tv_cycle_tue.getCompoundDrawables()[2] == null)
                    tv_cycle_tue.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_tue.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(1, "");
                break;
            case R.id.tv_cycle_wed:
                if (tv_cycle_wed.getCompoundDrawables()[2] == null)
                    tv_cycle_wed.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_wed.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(2, "");
                break;
            case R.id.tv_cycle_thu:
                if (tv_cycle_thu.getCompoundDrawables()[2] == null)
                    tv_cycle_thu.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_thu.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(3, "");
                break;
            case R.id.tv_cycle_fri:
                if (tv_cycle_fri.getCompoundDrawables()[2] == null)
                    tv_cycle_fri.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_fri.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(4, "");
                break;
            case R.id.tv_cycle_sat:
                if (tv_cycle_sat.getCompoundDrawables()[2] == null)
                    tv_cycle_sat.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_sat.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(5, "");
                break;
            case R.id.tv_cycle_sun:
                if (tv_cycle_sun.getCompoundDrawables()[2] == null)
                    tv_cycle_sun.setCompoundDrawables(null, null, nav_right, null);
                else tv_cycle_sun.setCompoundDrawables(null, null, null, null);
                selectRemindCyclePopupListener.obtainMessage(6, "");
                break;
            case R.id.tv_cycle_sure:
                int remind = ((tv_cycle_mon.getCompoundDrawables()[2] == null) ? 0 : 1) * 1 // 周一
                        + ((tv_cycle_tue.getCompoundDrawables()[2] == null) ? 0 : 1) * 2 // 周二
                        + ((tv_cycle_wed.getCompoundDrawables()[2] == null) ? 0 : 1) * 4 // 周三
                        + ((tv_cycle_thu.getCompoundDrawables()[2] == null) ? 0 : 1) * 8 // 周四
                        + ((tv_cycle_fri.getCompoundDrawables()[2] == null) ? 0 : 1) * 16 // 周五
                        + ((tv_cycle_sat.getCompoundDrawables()[2] == null) ? 0 : 1) * 32 // 周六
                        + ((tv_cycle_sun.getCompoundDrawables()[2] == null) ? 0 : 1) * 64; // 周日
                selectRemindCyclePopupListener.obtainMessage(7, String.valueOf(remind));
                dismiss();
                break;
            default:
                break;
        }

    }

    public interface SelectRemindCyclePopupOnClickListener {
        void obtainMessage(int flag, String ret);
    }

    public void setOnSelectRemindCyclePopupListener(SelectRemindCyclePopupOnClickListener l) {
        this.selectRemindCyclePopupListener = l;
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

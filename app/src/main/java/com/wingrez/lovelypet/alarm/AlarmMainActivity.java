package com.wingrez.lovelypet.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.wingrez.lovelypet.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmMainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_date; //日期
    private TextView tv_repeat_value, tv_ring_value; //重复，铃声

    private TimePickerView pv_time; //时间选择器

    private RelativeLayout repeat_rl, ring_rl; //布局
    private LinearLayout allLayout;

    private Button set_btn; //设置按钮
    private String time;

    private int cycle;
    private int ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_main);

        allLayout = (LinearLayout) findViewById(R.id.all_layout);

        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);

        tv_date = (TextView) findViewById(R.id.tv_date);

        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);

        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);

        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);

        pv_time = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pv_time.setCyclic(true); //设置时间循环
        pv_time.setCancelable(true); //设置取消按钮

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pv_time.setTime(new Date()); //设置为当前时间
                pv_time.show();
            }
        });

        //选择时间后回调
        pv_time.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time = simpleDateFormat.format(date);
                tv_date.setText(time);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            case R.id.set_btn:
                setClock();
                break;
            default:
                break;
        }
    }

    /**
     * 开启闹钟
     */
    private void setClock() {
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            if (cycle == 0) {//eyeryday
                AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0, 0, "闹钟响了", ring);
            } else if (cycle == -1) {//once
                AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0, 0, "闹钟响了", ring);
            } else {//week
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer.parseInt(times[1]), i, Integer.parseInt(weeks[i]), "闹钟响了", ring);
                }
            }
            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 设置提醒周期
     */
    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup.SelectRemindCyclePopupOnClickListener() {
            @Override
            public void obtainMessage(int flag, String ret) {
                //flag值在SelectRemindCycle中定义
                switch (flag) {
                    case 0: // 星期一
                        break;
                    case 1: // 星期二
                        break;
                    case 2: // 星期三
                        break;
                    case 3: // 星期四
                        break;
                    case 4: // 星期五
                        break;
                    case 5: // 星期六
                        break;
                    case 6: // 星期日
                        break;
                    case 7: // 确定
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("每天");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 设置提醒方式：铃声，震动
     */
    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup.SelectRemindWayPopupOnClickListener() {
            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText("震动");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tv_ring_value.setText("铃声");
                        ring = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 解析二进制闹钟周期
     *
     * @param repeat 周期值
     * @param flag   flag=0 返回“周一，周二...”，flag=1 返回“1,2,3...”
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + " " + "周二";
                weeks = weeks + " " + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + " " + "周三";
                weeks = weeks + " " + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + " " + "周四";
                weeks = weeks + " " + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + " " + "周五";
                weeks = weeks + " " + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + " " + "周六";
                weeks = weeks + " " + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + " " + "周日";
                weeks = weeks + " " + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

}

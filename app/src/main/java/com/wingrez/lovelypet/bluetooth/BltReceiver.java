package com.wingrez.lovelypet.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.bean.BltBean;

import org.greenrobot.eventbus.EventBus;

/**
 * 蓝牙设备接收器
 */
public class BltReceiver extends BroadcastReceiver {

    private static final int FIND_DEVICE = 1; //查找设备
    private static final int FIND_DEVICE_FINISHED = 2; //扫描完成
    private static final int FIND_STARTED = 3; //开始扫描
    private static final int CONN_SUCCESS = 4; //配对成功

    /**
     * 检测到蓝牙设备
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BluetoothAdapter.ACTION_STATE_CHANGED: //蓝牙状态改变
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON://蓝牙打开中
                        Log.e("BltReceiver", "蓝牙打开中");
                        break;
                    case BluetoothAdapter.STATE_ON://蓝牙打开完成
                        Log.e("BltReceiver", "蓝牙打开完成");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF://蓝牙关闭中
                        Log.e("BltReceiver", "蓝牙关闭中");
                        break;
                    case BluetoothAdapter.STATE_OFF://蓝牙关闭完成
                        Log.e("BltReceiver", "蓝牙关闭完成");
                        break;
                    default:
                        break;
                }
                break;
            case BluetoothDevice.ACTION_FOUND: //找到设备
                Log.e("BltReceiver", "找到设备");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                EventBus.getDefault().post(new BltBean(FIND_DEVICE, device));
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED: //搜索完成
                Log.e("BltReceiver", "搜索完成");
                EventBus.getDefault().post(new BltBean(FIND_DEVICE_FINISHED));
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED: //开始扫描
                Log.e("BltReceiver", "开始扫描");
                EventBus.getDefault().post(new BltBean(FIND_STARTED));
                break;
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED: //状态改变
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_NONE: //未绑定
                        break;
                    case BluetoothDevice.BOND_BONDING: //正在绑定
                        Toast.makeText(App.context, "配对中", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDED: //已绑定
                        Toast.makeText(App.context, "配对成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 蓝牙广播过滤器
     *
     * @return
     */
    public IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);//找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        return filter;
    }
}



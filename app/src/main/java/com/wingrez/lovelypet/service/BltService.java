package com.wingrez.lovelypet.service;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.bean.BltBean;
import com.wingrez.lovelypet.utils.BltConstant;
import com.wingrez.lovelypet.manager.BltManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * 蓝牙服务端
 */
public class BltService {

    public static int SERVER_ACCEPT = 11;//回调标记

    private BluetoothServerSocket bltServerSocket;
    private BluetoothSocket bltSocket;


    public BluetoothServerSocket getBltServerSocket() {
        return bltServerSocket;
    }

    public void setBltServerSocket(BluetoothServerSocket bltServerSocket) {
        this.bltServerSocket = bltServerSocket;
    }

    public BluetoothSocket getBltSocket() {
        return bltSocket;
    }

    public void setBltSocket(BluetoothSocket bltSocket) {
        this.bltSocket = bltSocket;
    }

    private BltService() {
        createBltService();
    }

    private static class BlueToothServices {
        private static BltService bltService = new BltService();
    }

    public static BltService getInstance() {
        return BlueToothServices.bltService;
    }

    /**
     * 从蓝牙适配器中创建一个蓝牙服务作为服务端，在获得蓝牙适配器后创建服务器端
     * 服务器端的bltSocket需要传入uuid和一个独立存在的字符串，以便验证，通常使用包名的形式
     */
    private void createBltService() {
        try {
            if (BltManager.getInstance().getmBluetoothAdapter() != null) {
                bltServerSocket = BltManager.getInstance().getmBluetoothAdapter().listenUsingRfcommWithServiceRecord("hlq.bluetooth", BltConstant.SPP_UUID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启服务端
     */
    public void startBluService() {
        while (true) {
            try {
                if (getBltServerSocket() == null){
                    Log.e("BltService","bltServerSocket为空");
                    return;
                }
                bltSocket = getBltServerSocket().accept();
                if (bltSocket != null) {
                    App.bltSocket = bltSocket;
                    EventBus.getDefault().post(new BltBean(SERVER_ACCEPT, bltSocket.getRemoteDevice()));
                    //如果你的蓝牙设备只是一对一的连接，则执行以下代码
                    getBltServerSocket().close();
                    //如果你的蓝牙设备是一对多的，则应该调用break；跳出循环
                    //break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭服务端
     */
    public void cancel() {
        try {
            getBltServerSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("BltService", "关闭服务器socket失败");
        }
    }

}

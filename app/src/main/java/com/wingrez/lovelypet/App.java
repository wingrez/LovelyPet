package com.wingrez.lovelypet;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

/**
 * Created by  Huanglinqing on 2018/8/24/024.
 * APPlication 类
 * 初始化
 */
public class App extends Application {

    //不管是蓝牙客户端还是服务器端，得到socket对象后都传入
    public static BluetoothSocket bluetoothSocket;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        com.orhanobut.logger.Logger.init();
    }
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}

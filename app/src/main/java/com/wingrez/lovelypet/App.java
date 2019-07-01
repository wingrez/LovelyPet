package com.wingrez.lovelypet;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wingrez.lovelypet.service.NotificationCollectorMonitorService;

public class App extends Application {

    public static Context context;
    public static BluetoothSocket bltSocket;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        startService(new Intent(this, NotificationCollectorMonitorService.class));
    }
}

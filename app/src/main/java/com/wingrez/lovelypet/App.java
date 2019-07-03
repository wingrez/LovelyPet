package com.wingrez.lovelypet;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import com.wingrez.lovelypet.service.NMService;

public class App extends Application {

    public static Context context;
    public static BluetoothSocket bltSocket;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        startService(new Intent(this, NMService.class));
    }
}

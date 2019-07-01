package com.wingrez.lovelypet.bean;

import android.bluetooth.BluetoothDevice;

public class BltBean {

    public int id;
    public BluetoothDevice bluetoothDevice;

    public BltBean(int id) {
        this.id = id;
    }

    public BltBean(int id, BluetoothDevice bluetoothDevice) {
        this.id = id;
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}

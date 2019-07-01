package com.wingrez.lovelypet.bean;

import android.bluetooth.BluetoothDevice;

public class BluRxBean {

    public int id;
    public BluetoothDevice bluetoothDevice;

    public BluRxBean(int id) {
        this.id = id;
    }

    public BluRxBean(int id, BluetoothDevice bluetoothDevice) {
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

package com.wingrez.lovelypet.bluetooth;

import android.os.Environment;
import android.util.Log;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.bean.MessageBean;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 接收消息服务端
 */
public class BltReceiveSocketService {

    private int RECEIVER_MESSAGE = 21; //收到消息

    public void receiveMessage() {
        if (App.bltSocket == null) {
            Log.e("BltReceiveSocketService","bltSocket为空");
            return;
        }
        try {
            InputStream inputStream = App.bltSocket.getInputStream();
            BufferedReader bff = new BufferedReader(new InputStreamReader(inputStream));
            String json;
            while (true) {
                while ((json = bff.readLine()) != null) {
                    EventBus.getDefault().post(new MessageBean(RECEIVER_MESSAGE, json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

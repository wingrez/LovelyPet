package com.wingrez.lovelypet.service;

import android.text.TextUtils;
import android.util.Log;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.bean.MessageBean;
import com.wingrez.lovelypet.utils.BltConstant;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 发送消息服务端
 */
public class BltSendSocketService {

    /**
     * 发送文本消息
     *
     * @param message
     */
    public static void sendMessage(String message) {
        if (App.bltSocket == null || TextUtils.isEmpty(message)){
            Log.e("BltSendSocketService","bltSocket为空或消息为空");
            return;
        }
        try {
            message += "\n";
            OutputStream outputStream = App.bltSocket.getOutputStream();
            outputStream.write(message.getBytes("utf-8"));
            outputStream.flush();
            EventBus.getDefault().post(new MessageBean(BltConstant.SEND_TEXT_SUCCESS));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

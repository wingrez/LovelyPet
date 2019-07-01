package com.wingrez.lovelypet.receiver;

import android.content.Context;
import android.content.Intent;

import com.wingrez.lovelypet.service.FWService;

/**
 * 广播接收类
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_BOOT: //开机启动
                Intent FWIntent = new Intent(context, FWService.class);
                FWIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(FWIntent);
                break;
            default:
                break;
        }
    }
}

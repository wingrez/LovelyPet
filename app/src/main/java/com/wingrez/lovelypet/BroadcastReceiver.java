package com.wingrez.lovelypet;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent FWIntent = new Intent(context, FWService.class);
            FWIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(FWIntent);
        }
    }
}

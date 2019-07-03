package com.wingrez.lovelypet.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.bean.NoticeBean;

import org.greenrobot.eventbus.EventBus;

/***
 * 监听微信消息通知服务实现类
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NMLService extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
    }

    /**
     * 当接受到一条消息时回调
     *
     * @param sbn 消息
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //当收到一条消息时回调，sbn里面带有这条消息的具体信息
        Log.e("onNotificationPosted_0", sbn.getPackageName());
        if (sbn.getPackageName().equals("com.tencent.mm")) { //微信
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString(Notification.EXTRA_TITLE); //通知title
            String content = extras.getString(Notification.EXTRA_TEXT); //通知内容
            int smallIconId = extras.getInt(Notification.EXTRA_SMALL_ICON); //通知小图标id
            Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON); //通知的大图标，注意和获取小图标的区别
            PendingIntent pendingIntent = sbn.getNotification().contentIntent; //获取通知的PendingIntent
            Log.e("onNotificationPosted_1", extras+"");
            Log.e("onNotificationPosted_2", title);
            Log.e("onNotificationPosted_3", content);
            Log.e("onNotificationPosted_4", smallIconId+"");
            Log.e("onNotificationPosted_5", largeIcon+"");
            Log.e("onNotificationPosted_6", pendingIntent+"");



            NoticeBean mBean=new NoticeBean("微信",title,content);
            EventBus.getDefault().post(mBean);

        }

    }

    /**
     * 移除一条消息时回调
     *
     * @param sbn 被移除的消息
     */
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}

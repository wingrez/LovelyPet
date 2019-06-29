/***
 * 监听微信消息通知服务实现类
 * By Wingrez 201906
 */

package com.wingrez.lovelypet;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WNLService extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
    }

    /**
     * 当接受到一条消息时回调
     * @param sbn 消息
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //当收到一条消息时回调，sbn里面带有这条消息的具体信息
        System.out.println("----------------onNotificationPosted--------->");
        System.out.println("----------------onNotificationPosted----0----->" + sbn.getPackageName());
        if(sbn.getPackageName().equals("com.tencent.tim")){
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString(Notification.EXTRA_TITLE); //通知title
            String content = extras.getString(Notification.EXTRA_TEXT); //通知内容
            int smallIconId = extras.getInt(Notification.EXTRA_SMALL_ICON); //通知小图标id
            Bitmap largeIcon =  extras.getParcelable(Notification.EXTRA_LARGE_ICON); //通知的大图标，注意和获取小图标的区别
            PendingIntent pendingIntent = sbn.getNotification().contentIntent; //获取通知的PendingIntent
            System.out.println("----------------onNotificationPosted----1---->" + extras);
            System.out.println("----------------onNotificationPosted----2----->" + title);
            System.out.println("----------------onNotificationPosted----3----->" + content);
            System.out.println("----------------onNotificationPosted----4----->" + smallIconId);
            System.out.println("----------------onNotificationPosted----5----->" + largeIcon);
            System.out.println("----------------onNotificationPosted----6----->" + pendingIntent);
        }


//        NoticesBean mBean = new NoticesBean();
//        mBean.setLargeIcon(largeIcon);
//        mBean.setSmallIconId(smallIconId);
//        if (title == null || largeIcon == null) {
//            return;
//        }
//        EventBus.getDefault().post(mBean);
    }

    /**
     * 移除一条消息时回调
     * @param sbn 被移除的消息
     */
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}

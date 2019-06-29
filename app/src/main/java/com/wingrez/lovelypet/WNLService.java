/***
 * 监听微信消息通知服务实现类
 * By Wingrez 201906
 */

package com.wingrez.lovelypet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WNLService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

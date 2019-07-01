package com.wingrez.lovelypet.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wingrez.lovelypet.service.FWService;
import com.wingrez.lovelypet.R;
import com.wingrez.lovelypet.service.NotificationCollectorMonitorService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Activity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FWService.class));
            }
        }
    }

    /**
     * 控件点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_openFW: //开启悬浮窗
                startFWService();
                break;
            case R.id.btn_closeFW: //关闭悬浮窗
                stopFWService();
                break;
            case R.id.btn_setAlarm:
                Intent intent = new Intent(MainActivity.this, AlmActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_listenWechat: //监听微信
                startWNLService();
                break;
            case R.id.btn_blt: //蓝牙
                intent = new Intent(MainActivity.this, BltActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }

    /**
     * 开启悬浮窗服务
     */
    public void startFWService() {
        if (FWService.isFWRunning) { //检查悬浮窗开启状态，只允许开启一个悬浮窗
            Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //需要判断系统版本是否大于Android 6.0
            if (!Settings.canDrawOverlays(this)) { //因为版本号大于6.0的系统才可以调用此方法，未授权状态
                Toast.makeText(this, "未授权开启悬浮窗", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0); //请求授权
            } else { //已授权状态
                Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FWService.class));
            }
        } else { //版本号小于6.0，直接开启悬浮窗，无需授权
            Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
            startService(new Intent(MainActivity.this, FWService.class));
        }
    }

    /**
     * 关闭悬浮窗服务
     */
    public void stopFWService() {
        stopService(new Intent(MainActivity.this, FWService.class));
    }

    /**
     * 检查并获取通知权限
     */
    public void startWNLService() {
        if (!isNotificationListenersEnabled()) {
            if (!gotoNotificationAccessSetting())
                Toast.makeText(this, "监听服务开启失败", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "监听服务已开启", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否拥有通知权限
     *
     * @return 是否拥有通知权限
     */
    public boolean isNotificationListenersEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 打开通知权限设置界面
     *
     * @return 是否找到通知权限设置界面
     */
    public boolean gotoNotificationAccessSetting() {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) { //普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                startActivity(intent);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}

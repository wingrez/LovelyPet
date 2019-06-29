package com.wingrez.lovelypet.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wingrez.lovelypet.WNLService;

public class Utils {
    /**
     * 获取View的宽度
     *
     * @return
     */
    public static int getViewWidth(View view) {
        if (view != null) {
            return view.getWidth();
        }
        return 0;
    }

    /**
     * 获取View的高度
     *
     * @return
     */
    public static int getViewHeight(View view) {
        if (view != null) {
            return view.getHeight();
        }
        return 0;
    }

}

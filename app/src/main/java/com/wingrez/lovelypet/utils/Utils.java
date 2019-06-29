package com.wingrez.lovelypet.utils;

import android.view.View;

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

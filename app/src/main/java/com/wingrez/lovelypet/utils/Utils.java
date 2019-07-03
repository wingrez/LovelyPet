package com.wingrez.lovelypet.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.wingrez.lovelypet.App;

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


    public static int getDrawableWidth(Drawable drawable){
        if(drawable!=null){
            return drawable.getBounds().width();
        }
        return 0;
    }

    public static int getDrawableHeight(Drawable drawable){
        if(drawable!=null){
            return drawable.getBounds().height();
        }
        return 0;
    }

    public static int getStatusBarHeight() {
        int resourceId = App.context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return App.context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}

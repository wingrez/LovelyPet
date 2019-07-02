package com.wingrez.lovelypet.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wingrez.lovelypet.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.wingrez.lovelypet.utils.Utils.*;

/**
 * 悬浮窗服务实现类
 */
public class FWService extends Service {

    public static boolean isFWRunning = false; //悬浮窗是否开启
    public static boolean isFWMoving = false; //悬浮窗是否正在移动

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View fwView;
    private GifImageView mGifIvPhoto;

    private int[] images;
    private int imageIndex = 0;

    private Handler changeImageHandler;

    private int screenWidth; //屏幕宽度
    private int screenHeight; //屏幕高度
    private float attachLength; //吸附距离

    @Override
    public void onCreate() {
        super.onCreate();

        isFWRunning = true;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        //获取屏幕宽度和高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;         // 屏幕宽度（像素）
        screenHeight = displayMetrics.heightPixels;       // 屏幕高度（像素）
        attachLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 3, getResources().getDisplayMetrics());
        Log.e("screenWidth", screenWidth + "");
        Log.e("screenHeight", screenHeight + "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //系统版本号大于等于8.0
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else { //系统版本号小于8.0
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //设置布局参数
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT; //悬浮窗宽高自适应，尽量选择尺寸相同（150）的素材，
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = screenWidth;
        layoutParams.y = screenHeight;

//        //图片资源
//        images = new int[]{
//                R.drawable.img_1,
//        };

        changeImageHandler = new Handler(this.getMainLooper(), changeImageCallback);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFW();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(fwView);
        isFWRunning = false;
    }

    /**
     * 设置悬浮窗的位置
     *
     * @param x
     * @param y
     */
    private void setViewPosition(int x, int y) {
        if (x < 0) layoutParams.x = 0;
        else if (x > screenWidth) layoutParams.x = screenWidth;
        else layoutParams.x = x;

//        if (y < 0) layoutParams.y = 0;
//        else if (y > screenHeight + getStatusBarHeight() - getViewHeight(fwView))
//            layoutParams.y = screenHeight + getStatusBarHeight() - getViewHeight(fwView);
//        else {
//            layoutParams.y = y;
//        }

        if (y < 0) layoutParams.y = 0;
        else if (y > screenHeight) layoutParams.y = screenHeight;
        else layoutParams.y = y;
    }

    /**
     * 显示悬浮窗
     */
    private void showFW() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        fwView = layoutInflater.inflate(R.layout.fw_main, null);
        fwView.setOnTouchListener(new FloatingOnTouchListener());
//
//        ImageView imageView = fwView.findViewById(R.id.imgv_fw);
//        imageView.setImageResource(images[imageIndex]);

        mGifIvPhoto = fwView.findViewById(R.id.gifv_fw);

        try {
            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "img_1.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        windowManager.addView(fwView, layoutParams);

//        changeImageHandler.sendEmptyMessageDelayed(0, 1000);
    }

    /**
     * 切换图片的回调方法
     */
    private Handler.Callback changeImageCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                if (isFWMoving == true) { //当悬浮窗正在移动时，不更新View
                    changeImageHandler.sendEmptyMessageDelayed(0, 1000);
                    return false;
                }
                imageIndex++;
                if (imageIndex >= images.length) {
                    imageIndex = 0;
                }
                if (fwView != null) {
                    ((ImageView) fwView.findViewById(R.id.imgv_fw)).setImageResource(images[imageIndex]);
                }
                changeImageHandler.sendEmptyMessageDelayed(0, 1000);
            }

            return false;
        }
    };

    /**
     * 悬浮窗移动事件
     */
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private float x; //点击屏幕的x坐标，相对屏幕坐标系 注意是手指点击位置的坐标
        private float y; //点击屏幕的x坐标，相对屏幕坐标系

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: //按下动作
                    x = event.getRawX();
                    y = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE: //移动动作
                    float nowX = event.getRawX();
                    float nowY = event.getRawY();
                    float moveX = nowX - x;
                    float moveY = nowY - y;
                    setViewPosition(layoutParams.x + Math.round(moveX), layoutParams.y + Math.round(moveY));
                    try {
                        if (!isFWMoving) {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "img_3.gif"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    windowManager.updateViewLayout(view, layoutParams);
                    isFWMoving = true;
                    x = nowX;
                    y = nowY;
                    break;
                case MotionEvent.ACTION_UP: //抬起动作，自动吸附屏幕边缘
                    isFWMoving = false;
                    if (layoutParams.x < attachLength) { //左边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_left.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setViewPosition(0, layoutParams.y);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        break;
                    }
                    if (layoutParams.x + getViewWidth(fwView) > screenWidth - attachLength) { //右边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_right.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setViewPosition(screenWidth, layoutParams.y);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        break;
                    }

                    if (layoutParams.y < attachLength) { //上边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_up.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setViewPosition(layoutParams.x, 0);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        break;
                    }
                    if (layoutParams.y + getViewHeight(fwView) > screenHeight - getStatusBarHeight() - attachLength) { //下边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_bottom.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setViewPosition(layoutParams.x, screenHeight);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        break;
                    }

                    try {
                        mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "img_1.gif"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                default:
                    break;
            }
            return false;
        }
    }


}

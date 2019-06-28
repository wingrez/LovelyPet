package com.wingrez.lovelypet;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class FWService extends Service {

    public static boolean isFWRunning = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;

    private int[] images;
    private int imageIndex = 0;

    private Handler changeImageHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        isFWRunning = true;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //系统版本号大于等于8.0
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else { //系统版本号小于8.0
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.height = 100;
        layoutParams.width = 100;
        layoutParams.x = 0; //使悬浮窗处于水平和垂直居中位置
        layoutParams.y = 0;

        images = new int[]{
                R.drawable.image_1,
                R.drawable.image_2,
                R.drawable.image_3
        };

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
        windowManager.removeView(displayView);
        isFWRunning = false;
    }

    /**
     * 显示悬浮窗
     */
    private void showFW() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        displayView = layoutInflater.inflate(R.layout.image_display, null);
        displayView.setOnTouchListener(new FloatingOnTouchListener());
        ImageView imageView = displayView.findViewById(R.id.image_display_imageview);
        imageView.setImageResource(images[imageIndex]);
        windowManager.addView(displayView, layoutParams);

        changeImageHandler.sendEmptyMessageDelayed(0, 100);
    }

    /**
     * 切换图片的回调方法
     */
    private Handler.Callback changeImageCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                imageIndex++;
                if (imageIndex >= images.length) {
                    imageIndex = 0;
                }
                if (displayView != null) {
                    ((ImageView) displayView.findViewById(R.id.image_display_imageview)).setImageResource(images[imageIndex]);
                }

                changeImageHandler.sendEmptyMessageDelayed(0, 100);
            }
            return false;
        }
    };

    /**
     * 悬浮窗移动事件
     */
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private float x;
        private float y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getRawX();
                    y = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float nowX = event.getRawX();
                    float nowY = event.getRawY();
                    float movedX = nowX - x;
                    float movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + Math.round(movedX);
                    layoutParams.y = layoutParams.y + Math.round(movedY);
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }


}

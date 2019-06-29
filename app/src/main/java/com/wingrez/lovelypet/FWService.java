/***
 * 悬浮窗服务实现类
 * By Wingrez 201906
 */

package com.wingrez.lovelypet;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import static com.wingrez.lovelypet.utils.Utils.*;

public class FWService extends Service {

    public static boolean isFWRunning = false; //悬浮窗是否开启
    public static boolean isFWMoving = false; //悬浮窗是否正在移动，移动状态无动画

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View fwView;

    private int[] images;
    private int imageIndex = 0;

    private Handler changeImageHandler;

    private int screenWidth;
    private int screenHeight;

    @Override
    public void onCreate() {
        super.onCreate();

        isFWRunning = true;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        //获取屏幕宽度和高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;         // 屏幕宽度（像素） 540
        screenHeight = displayMetrics.heightPixels;       // 屏幕高度（像素） 960

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

        //图片资源
        images = new int[]{
                R.drawable.img_1,
                R.drawable.img_2,
                R.drawable.img_3,
                R.drawable.img_4,
                R.drawable.img_5,
                R.drawable.img_6,
                R.drawable.img_7,
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
        else if (x > screenWidth - getViewWidth(fwView)) layoutParams.x = screenWidth - getViewWidth(fwView);
        else layoutParams.x = x;

        if (y < 0) layoutParams.y = 0;
        else if (y > screenHeight - getViewHeight(fwView))
            layoutParams.y = screenHeight - getViewHeight(fwView);
        else layoutParams.y = y;
    }

    /**
     * 显示悬浮窗
     */
    private void showFW() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        fwView = layoutInflater.inflate(R.layout.fw_main, null);
        fwView.setOnTouchListener(new FloatingOnTouchListener());

        ImageView imageView = fwView.findViewById(R.id.imgv_fw);
        imageView.setImageResource(images[imageIndex]);

        windowManager.addView(fwView, layoutParams);

        changeImageHandler.sendEmptyMessageDelayed(0, 1000);
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
                    isFWMoving = true;
                    float nowX = event.getRawX();
                    float nowY = event.getRawY();
                    float moveX = nowX - x;
                    float moveY = nowY - y;
                    setViewPosition(layoutParams.x + Math.round(moveX), layoutParams.y + Math.round(moveY));
                    windowManager.updateViewLayout(view, layoutParams);
                    x = nowX;
                    y = nowY;
                    break;
                case MotionEvent.ACTION_UP: //抬起动作，自动吸附屏幕边缘
                    isFWMoving = false;
                    if (layoutParams.x < 30 || layoutParams.x+getViewWidth(fwView) > screenWidth - 30) {
                        moveX = layoutParams.x <= screenWidth / 2 ? 0 : screenWidth;
                        setViewPosition((int) moveX, layoutParams.y);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        break;
                    }
                    if (layoutParams.y < 30 || layoutParams.y+getViewHeight(fwView) > screenHeight - 30) {
                        moveY = layoutParams.y <= screenHeight / 2 ? 0 : screenHeight;
                        setViewPosition(layoutParams.x, (int) moveY);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        break;
                    }
                default:
                    break;
            }
            return false;
        }
    }


}

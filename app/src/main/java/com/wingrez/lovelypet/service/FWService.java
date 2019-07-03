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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wingrez.lovelypet.R;
import com.wingrez.lovelypet.bean.NoticeBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.wingrez.lovelypet.utils.Utils.getStatusBarHeight;
import static com.wingrez.lovelypet.utils.Utils.getViewHeight;
import static com.wingrez.lovelypet.utils.Utils.getViewMeasureHeight;
import static com.wingrez.lovelypet.utils.Utils.getViewWidth;

/**
 * 悬浮窗服务实现类
 */
public class FWService extends Service {

    public static boolean isFWRunning = false; //悬浮窗是否开启
    public static boolean isFWMoving = false; //悬浮窗是否正在移动
    public static boolean isFWAttach = false; //悬浮窗是否贴边
    public static boolean isMessageShow = false;
    public static boolean isFunctionShow = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View fwView;
    private LinearLayout lyFWMessage;
    private LinearLayout lyFWFunction1;
    private LinearLayout lyFWFunction2;
    private LinearLayout lyFWPet;

    private TextView tvFWMessage;
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
        EventBus.getDefault().register(this);

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
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT; //悬浮窗宽高自适应，尽量选择尺寸相同（128）的素材，
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutParams.width = 128;
//        layoutParams.height = 128;
        layoutParams.x = screenWidth / 2;
        layoutParams.y = screenHeight / 2;

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
    private void setFWPosition(int x, int y) {
        if (x < 0) layoutParams.x = 0;
        else if (x > screenWidth - getViewWidth(fwView))
            layoutParams.x = screenWidth - getViewWidth(fwView);
        else layoutParams.x = x;

        if (y < 0) layoutParams.y = 0;
        else if (y > screenHeight - getViewWidth(fwView))
            layoutParams.y = screenHeight - getViewWidth(fwView);
        else layoutParams.y = y;
    }

    /**
     * 显示悬浮窗
     */
    private void showFW() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        fwView = layoutInflater.inflate(R.layout.fw_main, null);
        fwView.setOnTouchListener(new FloatingOnTouchListener());

//        ImageView imageView = fwView.findViewById(R.id.imgv_fw);
//        imageView.setImageResource(images[imageIndex]);

        lyFWMessage = fwView.findViewById(R.id.lyFWMessage);
        lyFWFunction1 = fwView.findViewById(R.id.lyFWFunction1);
        lyFWFunction2 = fwView.findViewById(R.id.lyFWFunction2);
        lyFWPet = fwView.findViewById(R.id.lyFWPet);

        tvFWMessage = fwView.findViewById(R.id.tvFWMessage);
        mGifIvPhoto = fwView.findViewById(R.id.gifFWPet);


        try {
            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "img_1.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        windowManager.addView(fwView, layoutParams);

//        changeImageHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvFWHome:
                stopSelf(); // 先stopSelf，确保killProcess后service不会重启
        }
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
//                    ((ImageView) fwView.findViewById(R.id.imgv_fw)).setImageResource(images[imageIndex]);
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
        private float preX;
        private float preY;
        private float x; //点击屏幕的x坐标，相对屏幕坐标系 注意是手指点击位置的坐标
        private float y; //点击屏幕的x坐标，相对屏幕坐标系
        private float nowX;
        private float nowY;
        private float moveX;
        private float moveY;
        private boolean inRange = false;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            nowX = event.getRawX();
            nowY = event.getRawY();

            float rangeX1 = layoutParams.x;
            float rangeY1 = layoutParams.y + getViewMeasureHeight(lyFWMessage);
            float rangeX2 = layoutParams.x + getViewWidth(lyFWPet);
            float rangeY2 = layoutParams.y + getViewMeasureHeight(lyFWMessage) + getViewHeight(lyFWPet);

            //判断点击是否发生在宠物身上
            if (!inRange && nowX >= rangeX1 && nowX <= rangeX2 && nowY >= rangeY1 && nowY <= rangeY2) {
                inRange = true;
            }

            if (!inRange) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: //按下动作
                    x = preX = event.getRawX();
                    y = preY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE: //移动动作（仅点击也会调用此）
                    nowX = event.getRawX();
                    nowY = event.getRawY();
                    moveX = nowX - x;
                    moveY = nowY - y;

                    if (moveX == 0 && moveY == 0) {
                        break;
                    }

                    if (lyFWFunction1.getVisibility() != View.GONE || lyFWFunction2.getVisibility() != View.GONE || lyFWMessage.getVisibility() != View.GONE) {
                        x = layoutParams.x;
                        y = layoutParams.y + getViewHeight(lyFWMessage);
                        lyFWFunction1.setVisibility(View.GONE);
                        lyFWFunction2.setVisibility(View.GONE);
                        lyFWMessage.setVisibility(View.GONE);
                        setFWPosition((int) x, (int) y);
                        windowManager.updateViewLayout(view, layoutParams);
                    }

                    try {
                        if (!isFWMoving) { //初始时不在移动时触发
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "img_3.gif"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setFWPosition(layoutParams.x + Math.round(moveX), layoutParams.y + Math.round(moveY));
                    windowManager.updateViewLayout(view, layoutParams);

                    isFWMoving = true;
                    x = nowX;
                    y = nowY;

                    break;
                case MotionEvent.ACTION_UP: //抬起动作，自动吸附屏幕边缘
                    isFWMoving = false;
                    isFWAttach = false;
                    inRange = false;

                    if (Math.abs(event.getRawX() - preX) == 0 && Math.abs(event.getRawY() - preY) == 0 && !isFWAttach) {
                        if (isFunctionShow) {
                            lyFWFunction1.setVisibility(View.INVISIBLE);
                            lyFWFunction2.setVisibility(View.INVISIBLE);
                            lyFWMessage.setVisibility(View.INVISIBLE);
                            isFunctionShow = false;
                        } else {
                            lyFWFunction1.setVisibility(View.VISIBLE);
                            lyFWFunction2.setVisibility(View.VISIBLE);
                            lyFWMessage.setVisibility(View.VISIBLE);
                            isFunctionShow = true;
                        }
                        break;
                    }

                    if (layoutParams.x < attachLength) { //左边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_left.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setFWPosition(0, layoutParams.y);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        isFWAttach = true;
                        break;
                    }
                    if (layoutParams.x + getViewWidth(view) > screenWidth - attachLength) { //右边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_right.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setFWPosition(screenWidth, layoutParams.y);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        isFWAttach = true;
                        break;
                    }

                    if (layoutParams.y < attachLength) { //上边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_up.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setFWPosition(layoutParams.x, 0);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        isFWAttach = true;
                        break;
                    }
                    if (layoutParams.y + getViewWidth(fwView) > screenHeight - getStatusBarHeight() - attachLength) { //下边
                        try {
                            mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "attach_bottom.gif"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setFWPosition(layoutParams.x, screenHeight);
                        windowManager.updateViewLayout(fwView, layoutParams);
                        isFWAttach = true;
                        break;
                    }

                    try {
                        mGifIvPhoto.setImageDrawable(new GifDrawable(getAssets(), "img_1.gif"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    
                    if (lyFWFunction1.getVisibility() != View.INVISIBLE || lyFWFunction2.getVisibility() != View.INVISIBLE || lyFWMessage.getVisibility() != View.INVISIBLE) {
                        lyFWFunction1.setVisibility(View.INVISIBLE);
                        lyFWFunction2.setVisibility(View.INVISIBLE);
                        lyFWMessage.setVisibility(View.INVISIBLE);
                    }

                    break;

                default:
                    break;
            }

            return false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoticeBean noticeBean) {
        Log.e("tag", noticeBean.getContent().split(":", 2)[0]);
        if (!noticeBean.getContent().split(":", 2)[0].endsWith(noticeBean.getTitle())) {
            tvFWMessage.setText("<" + noticeBean.getAppName() + "消息>" + noticeBean.getTitle() + ": " + noticeBean.getContent());
        } else tvFWMessage.setText("<" + noticeBean.getAppName() + "消息>" + noticeBean.getContent());
        lyFWMessage.setVisibility(View.VISIBLE);
    }

}

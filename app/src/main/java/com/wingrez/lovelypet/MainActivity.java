package com.wingrez.lovelypet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_floatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_floatWindow= this.<Button>findViewById(R.id.btn_floatWindow);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatWindowService.class));
            }
        }
    }

    private void startFloatingButtonService(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //判断系统版本是否大于Android 6.0
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "请授权开启悬浮窗", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            } else {
                startService(new Intent(MainActivity.this, FloatWindowService.class));
            }
        } else {
            startService(new Intent(MainActivity.this, FloatWindowService.class));
        }
    }

    public void onClick (View view){
        switch (view.getId()){
            case R.id.btn_floatWindow :
                startFloatingButtonService(view);
                break;
        }
    }

}

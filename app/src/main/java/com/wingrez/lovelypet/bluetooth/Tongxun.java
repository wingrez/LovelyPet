package com.wingrez.lovelypet.bluetooth;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.R;
import com.wingrez.lovelypet.bean.MessageBean;
import com.wingrez.lovelypet.utils.factory.ThreadPoolProxyFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Tongxun extends AppCompatActivity {

    LinearLayout contentLy;
    EditText goEditText;
    Button goTextBtn;
    Button goFileBtn;
    TextView text;

    public void initView() {
        contentLy = findViewById(R.id.cancel_action);
        goEditText = findViewById(R.id.go_edit_text);
        goFileBtn = findViewById(R.id.go_file_btn);
        text = findViewById(R.id.text);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //开启消息接收端
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                new ReceiveSocketService().receiveMessage();
            }
        });
    }

    protected int getLayoutId() {
        return R.layout.activity_tongxun;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_text_btn:
                //发送文字消息
                if (TextUtils.isEmpty(goEditText.getText().toString())) {
                    Toast.makeText(App.context, "请先输入消息", Toast.LENGTH_SHORT).show();
                } else {
                    SendSocketService.sendMessage(goEditText.getText().toString());
                }
                break;
            case R.id.go_file_btn:
                SendSocketService.sendMessageByFile(Environment.getExternalStorageDirectory() + "/test.png");
                break;
        }
    }

    /**
     * RECEIVER_MESSAGE:21 收到消息
     * BltContant.SEND_TEXT_SUCCESS:发送消息成功
     * BltContant.SEND_FILE_NOTEXIT:文件不存在
     * BltContant.SEND_FILE_IS_FOLDER:不能发送文件夹
     *
     * @param messageBean
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageBean messageBean) {
        switch (messageBean.getId()) {
            case 21:
                Log.d("收到消息", messageBean.getContent());
                text.append("收到消息:" + messageBean.getContent() + "\n");
                break;
            case BltConstant.SEND_TEXT_SUCCESS:
                text.append("我:" + goEditText.getText().toString() + "\n");
                goEditText.setText("");
                break;
            case BltConstant.SEND_FILE_NOTEXIT:
                Toast.makeText(App.context, "发送的文件不存在，内存根目录下的test.png", Toast.LENGTH_SHORT).show();
                break;
            case BltConstant.SEND_FILE_IS_FOLDER:
                Toast.makeText(App.context, "不能传送一个文件夹", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

package com.wingrez.lovelypet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.R;
import com.wingrez.lovelypet.bean.MessageBean;
import com.wingrez.lovelypet.service.BltReceiveSocketService;
import com.wingrez.lovelypet.service.BltSendSocketService;
import com.wingrez.lovelypet.utils.BltConstant;
import com.wingrez.lovelypet.utils.factory.ThreadPoolProxyFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BltCommunicate extends AppCompatActivity {

    LinearLayout lyMessage;
    EditText etMessage;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //开启消息接收端
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                new BltReceiveSocketService().receiveMessage();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void initView() {
        lyMessage = findViewById(R.id.lyMessage);
        etMessage = findViewById(R.id.etMessage);
        text = findViewById(R.id.text);
    }

    /**
     * 控件点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                //发送文字消息
                if (TextUtils.isEmpty(etMessage.getText().toString())) {
                    Toast.makeText(App.context, "请先输入消息", Toast.LENGTH_SHORT).show();
                } else {
                    BltSendSocketService.sendMessage(etMessage.getText().toString());
                }
                break;
        }
    }

    /**
     * RECEIVER_MESSAGE：接收消息成功
     * BltContant.SEND_TEXT_SUCCESS:发送消息成功
     *
     * @param messageBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageBean messageBean) {
        switch (messageBean.getId()) {
            case BltConstant.RECEIVER_MESSAGE:
                Log.d("收到消息", messageBean.getContent());
                text.append("收到消息:" + messageBean.getContent() + "\n");
                break;
            case BltConstant.SEND_TEXT_SUCCESS:
                text.append("我:" + etMessage.getText().toString() + "\n");
                etMessage.setText("");
                break;
            default:
                break;
        }
    }
}

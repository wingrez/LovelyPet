package com.wingrez.lovelypet.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.wingrez.lovelypet.App;
import com.wingrez.lovelypet.R;
import com.wingrez.lovelypet.bean.BltBean;
import com.wingrez.lovelypet.utils.factory.ThreadPoolProxyFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class BltActivity extends AppCompatActivity {

    private TextView tvLocalBlt;
    private TextView tvBltDevice;
    private ListView lvDevice;

    private List<Map<String, String>> listMap; //用来存放<设备名称, 设备地址>的列表
    private List<BluetoothDevice> deviceList; //

    private AlertDialog alertDialog; //对话框

    private BluetoothManager bltManger; //蓝牙管理器
    private BluetoothAdapter bltAdapter; //蓝牙适配器
    private BltReceiver bltReceiver;

    private SimpleAdapter simpleAdapter;

    private BluetoothSocket bltSocket; //配对成功后的蓝牙套接字

    private int connSuccess = 12;//连接成功


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blt_main);
        EventBus.getDefault().register(this);
        //注册蓝牙广播接受者
        bltReceiver = new BltReceiver();
        registerReceiver(bltReceiver, bltReceiver.makeFilter());
        //初始化
        BltManager.getInstance().initBltManager(this);
        initView();
        initBlt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                scanBlt();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bltReceiver);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化View和点击方法
     */
    public void initView() {
        tvLocalBlt = findViewById(R.id.tvLocalBlt);
        tvBltDevice = findViewById(R.id.tvBltDevice);
        lvDevice = findViewById(R.id.lvDevice);

        listMap = new ArrayList<>();
        deviceList = new ArrayList<>();

        //列表点击“已配对”
        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Map<String, String> map;
                map = listMap.get(position);
                if (map.get("statue").equals("已配对")) { //如果状态是“已配对”
                    alertDialog = DialogUtils.dialogloading(BltActivity.this, "正在连接", false, false); //弹出提示框
                    ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("BltActivity", "正在连接");
                            connect(deviceList.get(position));
                        }
                    });
                } else { //状态为“未配对”
                    try {
                        //如果想要取消已经配对的设备，只需要将 createBond 改为 removeBond
                        Log.e("BltActivity", "正在配对");
                        Method method = BluetoothDevice.class.getMethod("createBond");
                        method.invoke(deviceList.get(position));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 控件点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getLocalBlt:
                String name = bltAdapter.getName(); //获取本地蓝牙名称
                String address = bltAdapter.getAddress(); //获取本地蓝牙地址
                tvLocalBlt.setText("本地蓝牙名称:" + name + "\n" + "本地蓝牙地址:" + address);
                break;
            case R.id.btnSearch: //搜索蓝牙设备
                if (!isBltEnable()) { //如果蓝牙不可用，打开蓝牙
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //请求用户选择是否打开蓝牙
                    startActivityForResult(intent, 1);
                } else {
                    scanBlt();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化本地蓝牙设备，检查设备是否支持蓝牙
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBlt() {
        bltManger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bltAdapter = bltManger.getAdapter();
        if (bltAdapter == null) {
            Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @return 蓝牙是否可用
     */
    public boolean isBltEnable() {
        return bltAdapter.isEnabled();
    }

    /**
     * 扫描蓝牙设备
     */
    private void scanBlt() {
        //请求用户选择是否使该蓝牙能被扫描
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30); //30s扫描发现时间
        startActivity(intent);

        listMap.clear();

        if (simpleAdapter != null) {
            simpleAdapter.notifyDataSetChanged();
            deviceList.clear();
        }

        //开启蓝牙服务端
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                BltService.getInstance().startBluService();
            }
        });

        //开始扫描
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        if (bltAdapter.isDiscovering()) { //如果蓝牙正在扫描
                            bltAdapter.cancelDiscovery(); //取消扫描
                        }
                        bltAdapter.startDiscovery(); //开始扫描
                    }

                    @Override
                    public void onDenied(List<String> permissions) {

                    }
                });
    }

    /***
     * 连接蓝牙
     */
    private void connect(BluetoothDevice bluetoothDevice) {
        try {
            bltSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BltConstant.SPP_UUID);
            if (bltSocket != null) {
                App.bltSocket = bltSocket;
                if (bltAdapter.isDiscovering()) { //如果蓝牙在扫描，则停止扫描
                    bltAdapter.cancelDiscovery();
                }
                if (!bltSocket.isConnected()) { //如果蓝牙没有连接，才能连接
                    bltSocket.connect();
                }
                EventBus.getDefault().post(new BltBean(connSuccess, bluetoothDevice));
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bltSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * EventBus 异步
     * 1：找到设备
     * 2：扫描完成
     * 3：开始扫描
     * 4：配对成功
     * 11：有设备连接进来
     * 12：连接成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BltBean bltBean) {
        Intent intent = null;
        switch (bltBean.getId()) {
            case 1:
                deviceList.add(bltBean.getBluetoothDevice());
                Map<String, String> map = new HashMap<>();
                map.put("deviceName", bltBean.getBluetoothDevice().getName() + ":" + bltBean.getBluetoothDevice().getAddress());
                if (bltBean.getBluetoothDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    map.put("statue", "未配对");
                } else {
                    map.put("statue", "已配对");
                }
                listMap.add(map);
                simpleAdapter = new SimpleAdapter(BltActivity.this, listMap, R.layout.blt_devices, new String[]{"deviceName", "statue"}, new int[]{R.id.tvDeviceName, R.id.statue});
                lvDevice.setAdapter(simpleAdapter);
                break;
            case 2:
                DialogUtils.dimissloading(alertDialog);
                break;
            case 3:
                alertDialog = DialogUtils.dialogloading(BltActivity.this, "正在扫描", false, false);
                break;
            case 11:
            case 12:
                alertDialog.dismiss();
                intent = new Intent(BltActivity.this, BltCommunicate.class);
                intent.putExtra("devicename", bltBean.getBluetoothDevice().getName());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}

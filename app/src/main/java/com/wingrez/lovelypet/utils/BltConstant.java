package com.wingrez.lovelypet.utils;

import java.util.UUID;

/**
 * 蓝牙常量控制类
 */
public class BltConstant {
    //蓝牙UUID
    public static UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //启用蓝牙
    public static final int BLUE_TOOTH_OPEN = 1000;
    //禁用蓝牙
    public static final int BLUE_TOOTH_CLOSE = BLUE_TOOTH_OPEN + 1;
    //搜索蓝牙
    public static final int BLUE_TOOTH_SEARTH = BLUE_TOOTH_CLOSE + 1;
    //被搜索蓝牙
    public static final int BLUE_TOOTH_MY_SEARTH = BLUE_TOOTH_SEARTH + 1;
    //关闭蓝牙连接
    public static final int BLUE_TOOTH_CLEAR = BLUE_TOOTH_MY_SEARTH + 1;


    /**
     * 通讯返回值
     */
    public static final int RECEIVER_MESSAGE = 21; //接收文字消息成功
    public static final int SEND_TEXT_SUCCESS = 50;//发送文字消息成功
}

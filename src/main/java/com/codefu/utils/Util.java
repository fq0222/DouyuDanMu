package com.codefu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    /**
     * 斗鱼弹幕服务器地址
     */
    public static final String SERVER_ADRESS = "wss://danmuproxy.douyu.com:8501/";

    /**
     * 格式输出当前时间
     * @return
     */
    public static String currentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        return format.format(date);
    }
}

package com.codefu.websocket;

import com.codefu.model.douyu.HeartBeat;
import com.codefu.model.douyu.Login;
import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DouyuClient {
    private static Logger logger = Logger.getLogger(DouyuClient.class);

    private static final Pattern CONTENT_PATTERN = Pattern.compile("type@=chatmsg.*?/uid@=(.*?)/nn@=(.*?)/txt@=(.*?)/.*?/level@=(.*?)/");

    private boolean isRun;

    private String mServerUri;

    /**
     * 房间号
     */
    private String mRoomId;

    private WebSocketClient client;

    public DouyuClient() {
        isRun = true;
    }

    public DouyuClient setServerUri(String mServerUri) {
        this.mServerUri = mServerUri;
        return this;
    }

    public DouyuClient setRoomId(String mRoomId) {
        this.mRoomId = mRoomId;
        return this;
    }

    public DouyuClient connect() throws URISyntaxException {
        client = new WebSocketClient(new URI(mServerUri), new Draft_code()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                //连接成功
                logger.info("连接斗鱼弹幕服务器成功。");
                //登录
                Login login = new Login();
                client.send(login.login(mRoomId));
                client.send(login.joinGroup(mRoomId));

                //每隔40秒发送心跳
                new Thread(() -> {
                    while (isRun) {
                        if (client.isOpen()) {
                            client.send(new HeartBeat().keepLive());
                            logger.info("发送心跳===============发送心跳");
                        }
                        try {
                            Thread.sleep(44 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onMessage(String s) {

            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                String result = new String(bytes.array(), StandardCharsets.UTF_8);
//                logger.info("接收到原数据弹幕为：" + result);
                // 原数据

                // 正则解析需要的数据并打印
                analyzeAndPrintBarrage(result);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                logger.info("web socket be close: " + i + " s: " + s + " b: " + b);
            }

            @Override
            public void onError(Exception e) {
                logger.error("onError：" + e.toString());
            }
        };
        client.connect();
        return this;
    }

    /**
     * 正则解析需要的数据并打印
     *
     * @param result 原始数据
     * @author wind (https://blog.csdn.net/com_study)
     * @date @s_2020/9/8
     */
    private void analyzeAndPrintBarrage(String result) {
        Matcher matcher = CONTENT_PATTERN.matcher(result);
        if (matcher.find()) {
            String userId = matcher.group(1);
            String userName = matcher.group(2);
            String content = matcher.group(3);
            String level = matcher.group(4);
            String format = String.format("Lv.%s (%s | %s) 发送弹幕: \"%s\"", level, userId, userName, content);
            logger.info("接收到弹幕为：" + format);
        }
    }
}

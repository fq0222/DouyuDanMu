package com.codefu.model.douyu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Login {

    /**
     * 登录
     *
     * @param roomId 房间Id
     * @author wind (https://blog.csdn.net/com_study)
     * @date @s_2020/9/8
     */
    public byte[] login(String roomId) {
        return encode(String.format("type@=loginreq/roomid@=%s/", roomId));
    }

    /**
     * 加入组
     *
     * @param roomId 房间Id
     * @author wind (https://blog.csdn.net/com_study)
     * @date @s_2020/9/8
     */
    public byte[] joinGroup(String roomId) {
        return encode(String.format("type@=joingroup/rid@=%s/gid@=1/", roomId));
    }

    /**
     * 将数据转换为斗鱼协议要求的格式
     *
     * @param content 发送内容
     * @return bytes数据流
     * @author wind (https://blog.csdn.net/com_study)
     * @date @s_2020/9/8
     */
    private byte[] encode(String content) {
        byte[] dataLenBytes = intToBytesLittle(content.length() + 9);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            bytes.write(dataLenBytes);
            bytes.write(dataLenBytes);
            bytes.write(intToBytesLittle(689));
            bytes.write(content.getBytes(StandardCharsets.UTF_8));
            bytes.write("\0".getBytes());
        } catch (IOException e) {
            throw new RuntimeException("数据格式转换出错");
        }
        return bytes.toByteArray();
    }

    //将整形转化为4位小端字节流
    private byte[] intToBytesLittle(int value) {
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }
}

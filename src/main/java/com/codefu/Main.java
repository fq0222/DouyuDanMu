package com.codefu;

import com.codefu.utils.Util;
import com.codefu.websocket.DouyuClient;
import org.apache.log4j.Logger;

import java.net.URISyntaxException;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    //5720533 猛男绝地求生房间号

    public static void main(String[] args) {
        logger.info("程序开始 -> " + Util.currentTime());
        DouyuClient client = null;
        try {
            client = new DouyuClient()
                    .setRoomId("96291")
                    .setServerUri(Util.SERVER_ADRESS)
                    .connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

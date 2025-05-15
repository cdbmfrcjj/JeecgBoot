package org.jeecg.modules.msgCode.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

public class MsgCodeUtils {

    @Value(value="${jeecg.sms.url}")
    private static String urls;

    @Value(value="${jeecg.sms.username}")
    private static String username;

    @Value(value="${jeecg.sms.password}")
    private static String password;


    // 产生随机字符串
    public static String randString(int length) {
        Random r = new Random();
        String ssource = "0123456789";
        char[] src = ssource.toCharArray();
        char[] buf = new char[length];
        int rnd;
        for (int i = 0; i < length; i++) {
            rnd = Math.abs(r.nextInt()) % src.length;

            buf[i] = src[rnd];
        }
        return new String(buf);
    }

}

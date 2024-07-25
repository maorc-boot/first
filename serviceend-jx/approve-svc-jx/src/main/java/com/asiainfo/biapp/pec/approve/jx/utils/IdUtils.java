package com.asiainfo.biapp.pec.approve.jx.utils;

import cn.hutool.core.date.DatePattern;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author : zhouyang
 * @date : 2021-11-13 10:33:47
 * 获取ID
 */
public class IdUtils {

    /**
     * 17位的时间戳+1位随机小数*10+2位随机整数(10-99)
     * 生成策略ID
     * @return 21位的ID
     */

    @SneakyThrows
    public static synchronized String generateId(){
        Thread.sleep(1);
        String id = LocalDateTime.now().format(DatePattern.PURE_DATETIME_MS_FORMATTER);
        int randomInt=(1+(int)(Math.random()*9))*10;
        int lastInt=(new Random()).nextInt(89)+11;
        id=id+randomInt+lastInt;
        return id;
    }
}

package com.asiainfo.biapp.pec.approve.jx.enterprise.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author zhoulc
 * @title: ToolUtils
 * @date 2019/10/10 15:03
 */
public class ToolUtils {

    private static final Logger  logger = LoggerFactory.getLogger(ToolUtils.class);

    // 日期正则表达式
    public static final String DATE_FORMAT_REG = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$";
    // 日期时间正则表达式
    public static final String DATE_TIME_FORMAT_REG = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /**
     * 验证日期的格式 yyyy-MM-dd
     * @param date
     * @return
     */
    public static boolean isDateFormat(String date) {
        if (Pattern.matches(DATE_FORMAT_REG, date)) {
            return true;
        }
        return false;
    }

    /**
     * 验证日期时间的格式 yyyy-MM-dd HH:mm:ss
     * @param dateTime
     * @return
     */
    public static boolean isDateTimeFormat(String dateTime) {
        if (Pattern.matches(DATE_TIME_FORMAT_REG, dateTime)) {
            return true;
        }
        return false;
    }

    /**
     * 获取固定格式的日期时间
     * @param dateTime
     * @param format
     * @return
     */
    public static Date getFormatDate(String dateTime, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(dateTime);
        } catch (Exception e) {
            logger.error("时间格式转化错误", e);
        }
        return null;
    }

    /**
     * 获取昨天日期
     * @return
     */
    public static String getYesterday() {
        SimpleDateFormat sf = new SimpleDateFormat(RestConstant.DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1); // 天数减1
        return sf.format(calendar.getTime());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat sf = new SimpleDateFormat(RestConstant.DATE_FORMAT);
        return sf.format(new Date());
    }
}

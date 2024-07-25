package com.asiainfo.biapp.pec.eval.jx.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MpmUtil {

	protected static final int[] NUM_DATA = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	protected static String lastCampsegTaskId = "";//记录最新的活动或任务ID值
	protected static String lastTabTime = "";//记录最新的数据表时间戳后缀
	   /** 数据库保存的日期格式 */
    public static final String DATE_DB_FORMAT = "yyyy-MM-dd";
    public static final String DATE_DB_FORMAT_YM = "yyyy-MM";
    /** 当前语言显示的日期格式 */
    public static final String DATE_LOCALE_FORMAT = "zh";
    public static final boolean DATE_FORMAT_IS_DEFAULT = DATE_DB_FORMAT.equals(DATE_LOCALE_FORMAT);

	/**
	 * 判断字符串是不是由数字组�?
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	   /**
     * 将数据库中的日期类型转换为本地语言规定的数据格式； 数据库中的日期格式为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String parseOutDate(String date) {
        if (DATE_FORMAT_IS_DEFAULT) {
            return date;
        }
        String parsed = "";
        try {
			DateFormat df = new SimpleDateFormat(DATE_LOCALE_FORMAT, Locale.US);
			parsed = df.format(DateUtils.parseDateStrictly(date, DATE_DB_FORMAT));
        } catch (Exception e) {
            log.debug("--------date parse to out error, orign date is " + date);
            parsed = date;
        }
        return parsed;
    }
    
	/**
	 * 根据时间戳生成活动和任务编号，默认17位，可通过省份的mpm.properties配置CAMPSEG_TASK_NO_LENGTH的值定制长度
	 * @return YYYYMMDDHHMMSSXXX
	 */
	public synchronized static String generateCampsegAndTaskNo() {
		FastDateFormat df = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
		String dateStr = getFixLenStr(df.format(new Date()));
		if (StringUtils.isNotEmpty(lastCampsegTaskId)) {
			while (Long.parseLong(dateStr) == Long.parseLong(lastCampsegTaskId)) {
				dateStr = getFixLenStr(df.format(new Date()));
			}
		}
		lastCampsegTaskId = dateStr;
		return lastCampsegTaskId;
	}
	public static String getFixLenStr(String data) {
		String dateStr = data;
		//String noLen = MpmConfigure.getInstance().getProperty("CAMPSEG_TASK_NO_LENGTH");
		String noLen="16";
		if (StringUtils.isNotEmpty(noLen)) {
			int len = Integer.valueOf(noLen);
			if (data.length() < len) {
				dateStr += genFixLenNumStr(len - data.length());
			} else if (data.length() > len) {
				dateStr = data.substring(0, len);
			}
		}
		return dateStr;
	}
	/**
	 * 生成指定长度的随机数字
	 * @param len
	 * @return
	 */
	public static String genFixLenNumStr(int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int index = (int) (Math.random() * 10);
			sb.append(NUM_DATA[index]);
		}
		return sb.toString();
	}
	
	/**
	 * 获取绝对唯一的时间戳
	 * @return
	 */
	public synchronized static String convertLongMillsToYYYYMMDDHHMMSSSSS() {
		FastDateFormat df = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
		String dateStr = df.format(new Date());
		if (StringUtils.isNotEmpty(lastTabTime)) {
			while (Long.parseLong(dateStr) == Long.parseLong(lastTabTime)) {
				dateStr = df.format(new Date());
			}
		}
		lastTabTime = dateStr;
		return lastTabTime;
	}
}
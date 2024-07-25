package com.asiainfo.biapp.pec.plan.jx.enterprise.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuotaUtils {
	static Logger logger = LoggerFactory.getLogger(QuotaUtils.class);

	public static void map2Bean(Map<String, Object> data, Object obj)
			throws Exception {
		Field fields[] = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			typeMapper(fields[i], obj, data.get(property2ColumnName(fields[i].getName())));
		}
	}

	private static String property2ColumnName(String propertyName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < propertyName.length(); i++) {
			char flag = propertyName.charAt(i);
			if (Character.isUpperCase(flag)) {
				sb.append("_").append(Character.toLowerCase(flag));
			} else {
				sb.append(flag);
			}
		}
		return sb.toString();
	}

	private static void typeMapper(Field field, Object obj, Object value)
			throws Exception {
		if (value != null) {
			field.setAccessible(true);
			String type = field.getType().getName();
			if (type.equals("java.lang.String")) {
				field.set(obj, value.toString());
			} else if(type.equals("short") || type.equals("java.lang.Short")){
				field.set(obj,Short.valueOf(value.toString()));
			}else if (type.equals("int") || type.equals("java.lang.Integer")) {
				field.set(obj, Integer.valueOf(value.toString()));
			} else if (type.equals("long") || type.equals("java.lang.Long")) {
				field.set(obj, Long.valueOf(value.toString()));
			} else if (type.equals("float") || type.equals("java.lang.Float")) {
				field.set(obj, Float.valueOf(value.toString()));
			} else if (type.equals("double") || type.equals("java.lang.Double")) {
				field.set(obj, Double.valueOf(value.toString()));
			} else if (type.equals("boolean")
					|| type.equals("java.lang.Boolean")) {
				field.set(obj, Boolean.valueOf(value.toString()));
			} else if (type.equals("java.util.Date")) {
				field.set(obj, (Date) value);
			} else {
				field.set(obj, value);
			}
			field.setAccessible(false);
		}
	}

	
	/**
	 * 获得当天所属的月份
	 * @param format 获得的月的格式：如 yyyyMM、yyyy-MM等。
	 * @return
	 */
	public static String getDayMonth(String format) {
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		str = sdf.format(new Date());
		return str;
	}
	
	public static String getFullDate() {
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		str = sdf.format(new Date());
		return str;
	}
	
	/**
	 * 获得n天前的日期
	 * @param n  天数
	 * @param format 日期格式，如：yyyy-MM-dd、yyyyMMdd等等
	 * @return
	 */
	public static String getBeforeDate(int n,String format){
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		Long current = new Date().getTime();
		Long beforeDay = current-(long)n*24*60*60*1000;
		Date date = new Date(beforeDay);
		str = sdf.format(date);
		return str;
	}

	public static String getDayMonth4Show() {
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		str = sdf.format(new Date());
		return str;
	}

	/**
	 * 获得当天的日期字符串
	 * @param format  日期格式，如：yyyyMMdd、yyyy-MM-dd等
	 * @return
	 */
	public static String getDayDate(String format) {
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		str = sdf.format(new Date());
		return str;
	}
	

	/**
	 * 当前时间是几号
	 * 
	 * @return
	 */
	public static int getCurrentDayOfMon() {
		int dayOfMonth;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		return dayOfMonth;
	}

	/**
	 * 获得一个月的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonDays(int year, int month) {
		int days = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);// 月份从0开始算的
		days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	/**
	 * 
	 * @param monthDate
	 *            YYYYMM形式
	 * @return
	 */
	public static int getMonDays(String monthDate) {
		int days = 0;
		int year = Integer.parseInt(monthDate.substring(0, 4));
		int month = Integer.parseInt(monthDate.substring(4, monthDate.length()));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);// 月份从0开始算的
		days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}
	
	public static Date str2Date(String dateStr){
		Date date=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			logger.error("error", e);
		}
		return date;
	}
	/**
	 * 获得昨天的日期
	 * @param format 日期格式，如：yyyyMMdd、yyyy-MM-dd
	 * @return
	 */
	public static String getYesterday(String format) {
		long now =new Date().getTime();
		long yesterday = now-(long)24*60*60*1000;
		Date yesDate = new Date(yesterday);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String str = sdf.format(yesDate);
		return str;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List JsonStr2List(String js, Class c) {
		List list = new ArrayList();
		JSONArray ja = JSONArray.fromObject(js);
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			@SuppressWarnings("static-access")
			Object o = jo.toBean(jo, c);
			list.add(o);
		}
		return list;
	}

	/*public static void updateProp(List<DeptMonthQuota> list,String cityId, String month) {
		for (int i = 0; i < list.size(); i++) {
			DeptMonthQuota temp = list.get(i);
			temp.setCityId(cityId);
			temp.setDataDate(month);
		}
	}*/

}

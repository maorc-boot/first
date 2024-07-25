package com.asiainfo.biapp.pec.plan.jx.utils;


import com.google.common.collect.Lists;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: ranpf
 * Date: 2022-12-16
 * Time: 17:47
 */
public class DateTool {
    private static final Logger LOGGER = Logger.getLogger(DateTool.class);

    /**
     * 把20010101 转换成Date 时 分钟和秒都设置最小
     */
    public static final short MIN = 1;
    /**
     * 把20010102 转换成Date时 分钟和秒都设置最大
     */
    public static final short MAX = 2;


    public static Date getPreDate(Date date, int preDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, -preDays);
        return calendar.getTime();
    }

    public static int getPreDate(int date, int preDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.DAY_OF_WEEK, -preDays);
        return getIntegerDate(calendar.getTime());
    }

    public static Date getPreMonth(Date date, int preMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -preMonths);
        return calendar.getTime();
    }

    public static Date getAfterDate(Date date, int preDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK, preDays);
        return calendar.getTime();
    }

    public static Date getMinDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.getActualMinimum(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        return calendar.getTime();
    }

    public static Date getDate(Object obj) {
        if (obj instanceof Integer) {
            int value = ((Integer) obj).intValue();
            return getDate(value);
        } else if (obj instanceof String) {
            String s = obj.toString();
            return getDate(s);
        } else if (obj instanceof Date) {
            return (Date) obj;
        }
        return null;
    }

    public static Integer getIntegerDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Integer ret = null;
        try {
            ret = Integer.parseInt(sdf.format(date));
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + date + "]");
        }
        return ret;
    }

    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ret = null;
        try {
            ret = sdf.format(date);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + date + "]");
        }
        return ret;
    }

    /**
     * 得到完整的日期，带毫秒
     *
     * @param date
     * @return
     */
    public static String getStringFallDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String ret = null;
        try {
            ret = sdf.format(date);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + date + "]");
        }
        return ret;
    }

    public static String getStringDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ret = null;
        try {
            ret = sdf.format(date);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + date + "]");
        }
        return ret;
    }

    public static String getStringDate(Date date, String method) {
        SimpleDateFormat sdf = new SimpleDateFormat(method);
        String ret = null;
        try {
            ret = sdf.format(date);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + date + "]");
        }
        return ret;
    }

    public static Date getDate(int intDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date ret = null;
        try {
            ret = sdf.parse(String.valueOf(intDate));
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + intDate + "]");
        }
        return ret;
    }

    public static Date getDate(int intDate, short type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date ret = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(String.valueOf(intDate)));
            if (type == MIN) {
                calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
            } else if (type == MAX) {
                calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
            }
            ret = calendar.getTime();
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + intDate + "]");
        }
        return ret;
    }


    public static Date getDate(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date ret = null;
        try {
            String integerDate = stringDate.replaceAll("-", "").replaceAll("年", "").replaceAll("月", "").replaceAll("日", "");
            ret = sdf.parse(integerDate);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + stringDate + "]");
        }
        return ret;
    }

    public static Date getSimpleDate(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date simpledate = null;
        try {
            String integerDate = stringDate.replaceAll("-", "").replaceAll("年", "").replaceAll("月", "").replaceAll("日", "");
            simpledate = sdf.parse(integerDate);
        }
        catch (Exception ex) {

            LOGGER.debug("日期错误[" + stringDate + "]");
        }
        return simpledate;
    }

    public static Date getSimpleDateNoSs(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        Date simpledate = null;
        try {
            String integerDate = stringDate.replaceAll("-", "").replaceAll("年", "").replaceAll("月", "").replaceAll("日", "");
            simpledate = sdf.parse(integerDate);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + stringDate + "]");
        }
        return simpledate;
    }

    public static Date getDateFromCiccDocTemplate(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date ret = null;
        try {
            stringDate = stringDate.replace("年", "-");
            stringDate = stringDate.replace("月", "-");
            stringDate = stringDate.replace("日", "");

            String[] date = stringDate.split("-");
            if (Integer.parseInt(date[1]) < 10) {
                date[1] = "0" + date[1];
            }
            if (Integer.parseInt(date[2]) < 10) {
                date[2] = "0" + date[2];
            }
            String integerDate = date[0] + date[1] + date[2];


//            String integerDate = stringDate.replaceAll("-", "").replaceAll("年", "").replaceAll("月", "").replaceAll("日", "");
            ret = sdf.parse(integerDate);
        }
        catch (Exception ex) {
            LOGGER.debug("日期错误[" + stringDate + "]");
        }
        return ret;
    }

    public static String getStringDate(Integer date) {
        String ret = null;
        if (date != null) {
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
            try {
                ret = yyyy_MM_dd.format(yyyyMMdd.parse(String.valueOf(date)));
            }
            catch (Exception ex) {
                LOGGER.debug("日期错误[" + date + "]");
            }
        }
        return ret;
    }

    public static Integer getIntegerDate(String date) {
        return getIntegerDate(getDate(date));
    }

    public static Integer getIntegerDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        try {
            return getIntegerDate(format.parse(date));
        } catch (ParseException e) {
        	LOGGER.error("error", e);
            return null;
        }
    }

    public static Date getYearStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date getYearStart(Integer date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date getYearEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date getYearEnd(Integer date) {
        return getYearEnd(getDate(date));
    }
    public static Date getYearEndByYear(Integer year) {
          Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,Calendar.DECEMBER);
         return calendar.getTime();
     }

    public static Integer getPreWeek(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreNowMonth(int date, int monthNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.MONTH, -monthNum);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreMonth(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.MONTH, -1);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreThreeMonth(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.MONTH, -3);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreSixMonth(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.MONTH, -6);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreQuarter(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.MONTH, -3);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreHalfYear(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.MONTH, -6);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreYear(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.YEAR, -1);
        return getIntegerDate(calendar.getTime());
    }

    public static Integer getPreYearEnd(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getIntegerDate(calendar.getTime());
    }


    public static long calculateTimeMillisOfDay(int dayAmount) {

        return dayAmount * 24 * 60 * 60 * 1000;

    }

    public static int getDayCount(Date beginDate, Date endDate) {
        int count = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        while (calendar.getTime().before(endDate)) {
            count++;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return count;
    }

    /**
     * 得到两个日期之间相隔的时间，精确到毫秒
     *
     * @param dateFrom
     * @param dateEnd
     * @return 返回单位ms(毫秒)
     */
    public static long getTimeMillisBetweenTwoDates(Date dateFrom, Date dateEnd) {


        long begin = dateFrom.getTime();
        long end = dateEnd.getTime();
        long inter = end - begin;
        if (inter < 0) {
            inter = inter * (-1);
        }


        return inter;
    }


    /**
     * 得到两日期之间的工作日，不足上班时间的小时舍去（Date workEndTime-Date workStartTime）
     *
     * @param dateFrom      开始日期
     * @param dateEnd       结束日期
     * @param workStartTime 上班时间，只有时分秒有效
     * @param workEndTime   下班时间，只有时分秒有效
     * @return
     */
    public static int getWorkgDaysWithTime(Date dateFrom, Date dateEnd, Date workStartTime, Date workEndTime) {
        int workHourAday = (int) (workEndTime.getTime() - workStartTime.getTime()) / (60 * 60 * 1000);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(dateFrom);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(dateEnd);


        /*
        在同一天，直接返回1
         */
        if (dateEqualsIngnoreTime(dateFrom, dateEnd)) {

            return 1;
        }
        /**
         * 头和尾可能是不完整的天，单独处理，按小时算
         */
        startCal.add(Calendar.DAY_OF_YEAR, 1);

        int fullDays = getWorkDaysIgnoreTime(startCal.getTime(), endCal.getTime());

        double hours1 = getWorkHours(dateFrom, workStartTime, workEndTime, workHourAday, true);
        double hours2 = getWorkHours(dateEnd, workStartTime, workEndTime, workHourAday, false);

        double totalHours = hours1 + hours2;
        int result = fullDays + (int) totalHours / workHourAday;


        return result;

    }

    /**
     * 比较两个日期，忽略时分秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean dateEqualsIngnoreTime(Date date1, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1WithoutTime = dateFormat.parse(dateFormat.format(date1));
            Date date2WithoutTime = dateFormat.parse(dateFormat.format(date2));
            if (date1WithoutTime.compareTo(date2WithoutTime) == 0) {

                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
        	LOGGER.error("error", e);
            return false;
        }


    }

    /**
     * '
     *
     * @param targetDate   开始（targetDateIsStart=true或结束（targetDateIsStart=false），工作时间
     *                     targetDate如果为来上班的时间，则用该时间减去下班点，如targetDate为下班时间，则用该时间减去上班时间，取绝对值
     * @param workHourAday 该日期内属于工作时间的小时数
     * @return
     */
    public static double getWorkHours(Date targetDate, Date workStartTime, Date workEndTime, int workHourAday, boolean targetDateIsStart) {

        if (!isWorkDay(targetDate)) {
            return 0;
        }
        double result = 0.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date targetDateWithoutDate = dateFormat.parse(dateFormat.format(targetDate));
            Date workStartTimeWithoutDate = dateFormat.parse(dateFormat.format(workStartTime));
            Date workEndTimeWithoutDate = dateFormat.parse(dateFormat.format(workEndTime));

            if (targetDateIsStart) {
                if (targetDateWithoutDate.compareTo(workEndTimeWithoutDate) > 0) {

                    return 0;
                } else if (targetDateWithoutDate.compareTo(workStartTimeWithoutDate) < 0) {
                    return workHourAday;

                }
                long time = workEndTimeWithoutDate.getTime() - targetDateWithoutDate.getTime();
                result = time / (60 * 60 * 1000.0);
            } else {

                if (targetDateWithoutDate.compareTo(workEndTimeWithoutDate) > 0) {

                    return workHourAday;
                } else if (targetDateWithoutDate.compareTo(workStartTimeWithoutDate) < 0) {
                    return 0;

                }
                long time = targetDateWithoutDate.getTime() - workStartTimeWithoutDate.getTime();
                result = time / (60 * 60 * 1000.0);

            }


        } catch (ParseException e) {
        	LOGGER.error("error", e);
        }
        if (result < 0) {
            return -result;
        }
        return result;

    }


    /**
     * 特殊方法，泰康上班时间，只要时分秒
     *
     * @return
     */
    public static Date getWorkStartTime() {
        try {
            SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date workStart = formator.parse("2011-07-20 08:00");
            return workStart;
        } catch (ParseException e) {
            LOGGER.error("error", e);
            return null;
        }
    }

    /**
     * 特殊方法，泰康下班时间，只要时分秒
     *
     * @return
     */
    public static Date getWorkEndTime() {
        try {
            SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date workEnd = formator.parse("2011-07-20 18:00");
            return workEnd;
        } catch (ParseException e) {
            LOGGER.error("error", e);

            return null;
        }


    }

    /**
     * 判断某日期是否为工作日
     *
     * @param targetDate
     * @return
     */
    public static boolean isWorkDay(Date targetDate) {
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(targetDate);
        if (targetCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || targetCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

            return false;
        } else {
            return true;
        }

    }

    /**
     * 得到两日期相隔的工作日，忽略时分秒
     *
     * @param fromDate
     * @param endDate
     * @return
     */
    public static int getWorkDaysIgnoreTime(Date fromDate, Date endDate) {
        SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDateWithoutTime = null;
        Date endDateWithouTime = null;
        try {
            /*
            去除时分秒
             */
            fromDateWithoutTime = formator.parse(formator.format(fromDate));
            endDateWithouTime = formator.parse(formator.format(endDate));

        } catch (ParseException e) {
            LOGGER.error("error", e);
        }

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(fromDateWithoutTime);
        int count = 0;
        while (fromCalendar.getTime().before(endDateWithouTime)) {
            if (isWorkDay(fromCalendar.getTime())) {
                count++;
            }
            fromCalendar.add(Calendar.DAY_OF_YEAR, 1);

        }

        return count;
    }


    /**
     * 得到两日期相隔的天数，忽略时分秒
     *
     * @param fromDate
     * @param endDate
     * @return
     */
    public static int getWorkDaysIgnoreTimeNew(Date fromDate, Date endDate) {
        SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDateWithoutTime = null;
        Date endDateWithouTime = null;
        try {
            /*
            去除时分秒
             */
            fromDateWithoutTime = formator.parse(formator.format(fromDate));
            endDateWithouTime = formator.parse(formator.format(endDate));

        } catch (ParseException e) {
            LOGGER.error("error", e);
        }

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(fromDateWithoutTime);
        int count = 0;
        while (fromCalendar.getTime().before(endDateWithouTime)) {

                count++;

            fromCalendar.add(Calendar.DAY_OF_YEAR, 1);

        }

        return count;
    }


    public static void main(String args[]) throws ParseException {
       /* SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date startDate = formator.parse("2011-01-15 08:10:00");
        Date endDate = formator.parse("2011-07-18 18:15:00");

        Date workStart = formator.parse("2011-07-20 08:00");
        Date workEnd = formator.parse("2011-07-20 18:00");

        *//* int result = getWorkgDaysWithTime(startDate, endDate, workStart, workEnd);

        Date[] dates = getDateArrByQuarter(2011, 4);

       SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c=Calendar.getInstance();
       c.setTime(formator.parse("2011-08-01"));
        
        Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
*/
        List<String> idList = new ArrayList<>();

        String fullFileName = "/home/data/feilname.txt";
        String fileName  = fullFileName.substring(fullFileName.lastIndexOf("/")+1);

        System.out.println( new SimpleDateFormat("yyyy/M/d").format(new Date()));
        System.out.println( new SimpleDateFormat("yyyy/MM/dd").format(new Date()));


    }

    public static synchronized String generateId(){
        FastDateFormat df = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
        String dateStr=df.format(new Date());
        return dateStr;
    }

    public static String getCurrentYearMonthDay(String dateStr){
        SimpleDateFormat df = new SimpleDateFormat(dateStr);
        return df.format(new Date());
    }

    public  static String getTargetYearMonthDay(String dateStr,int days){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
       return new SimpleDateFormat( dateStr ).format(cal.getTime());
    }
    public static int getMonth(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date date) {
        int year = 0;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR);
        }
        return year;
    }

    public static int getYear(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        return calendar.get(Calendar.YEAR);
    }

    public static Date getMonthStart(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }
    


    public static List<Integer> getMonthEndList(int begin, int end) {
        List<Integer> dateList = new ArrayList<Integer>();
        int beginDate = begin;
        while (beginDate < end) {
            Integer monthEnd = DateTool.getIntegerDate(getMonthEnd(beginDate));
            if (monthEnd < end) {
                dateList.add(monthEnd);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateTool.getDate(beginDate));
            calendar.add(Calendar.MONTH, 1);
            beginDate = DateTool.getIntegerDate(calendar.getTime());
        }
        return dateList;
    }

    public static Date getMonthEnd(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static String getMonthStart(String date) {
        
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(getDate(date));
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),01);  
        return getStringDate2(calendar.getTime());
    }
   

    public static Date getWeekStart(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
        return calendar.getTime();
    }

    public static Date getWeekEnd(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
        return calendar.getTime();
    }

    public static Date getNextMonth(Date date, int next) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, next);
        return calendar.getTime();
    }

    public static int getSubDateDiffMonth(Date beginDate, Date endDate) {
        int dateDiffMonth = 0;
        if (beginDate != null && endDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            int beginMoth = calendar.get(Calendar.MONTH) + 1;

            Calendar calendartwo = Calendar.getInstance();
            calendartwo.setTime(endDate);
            int endMoth = calendartwo.get(Calendar.MONTH) + 1;

            int beginYear = calendar.get(Calendar.YEAR);
            int endYear = calendartwo.get(Calendar.YEAR);

            dateDiffMonth = Math.abs((endYear - beginYear) * 12 + (endMoth - beginMoth));
        }
        return dateDiffMonth;
    }

    public static int getDateQuarter(Date date) {
        int dateQuarter = 0;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dateMoth = calendar.get(Calendar.MONTH) + 1;
            for (int i = 1; i <= 12; i++) {
                if (dateMoth <= 3) {
                    dateQuarter = 1;
                } else if (dateMoth > 3 && dateMoth <= 6) {
                    dateQuarter = 2;
                } else if (dateMoth > 6 && dateMoth <= 9) {
                    dateQuarter = 3;
                } else {
                    dateQuarter = 4;
                }
            }
        }
        return dateQuarter;
    }


    /**
     * * 功能：根据指定时间所在的当前年，获取指定季度的开始时间和结束时间
     * * @param appointDate 指定当前年
     * * @param appointIndex
     * * @return
     */
    public static Date[] getDateArrByQuarter(Date appointDate, int appointIndex) throws IllegalArgumentException {
        Date stime = null;
        Date etime = null;
        Date[] date = new Date[2];
        if (appointDate == null) {
            appointDate = new Date();
        }
        
        Date tempDate = DateUtils.truncate(appointDate, Calendar.YEAR);

        if (appointIndex == 1) {
            stime = tempDate;
        } else if (appointIndex == 2) {
            stime = DateUtils.addMonths(tempDate, 3);
        } else if (appointIndex == 3) {
            stime = DateUtils.addMonths(tempDate, 6);
        } else if (appointIndex == 4) {
            stime = DateUtils.addMonths(tempDate, 9);
        }
        etime = DateUtils.addSeconds(DateUtils.addMonths(stime, 3), -1);

        date[0] = stime;
        date[1] = etime;

        return date;
    }


    /**
     * 得到某年某季度的结束
     *
     * @param year
     * @param quarter
     * @return
     */
    public static int getQuarterEndDate(int year, int quarter) {
        Date[] dates = getDateArrByQuarter(year, quarter);
        return DateTool.getIntegerDate(dates[1]);

    }

    /**
     * * 功能：根据指定年，获取指定季度的开始时间和结束时间
     * * @param appointDate 指定当前年
     * * @param appointIndex
     * * @return
     */
    public static Date[] getDateArrByQuarter(int year, int appointIndex) throws IllegalArgumentException {
        Date stime = null;
        Date etime = null;
        Date[] date = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        Date appointDate = calendar.getTime();
        Date tempDate = DateUtils.truncate(appointDate, Calendar.YEAR);

        if (appointIndex == 1) {
            stime = tempDate;
        } else if (appointIndex == 2) {
            stime = DateUtils.addMonths(tempDate, 3);
        } else if (appointIndex == 3) {
            stime = DateUtils.addMonths(tempDate, 6);
        } else if (appointIndex == 4) {
            stime = DateUtils.addMonths(tempDate, 9);
        }
        etime = DateUtils.addSeconds(DateUtils.addMonths(stime, 3), -1);

        date[0] = stime;
        date[1] = etime;

        return date;
    }

    /**
     * 得到传入日期所属季度结束天
     *
     * @param date
     * @return
     */
    public static int getQuarterEndDate(Date date) {
        int quarter = getDateQuarter(date);
        Date[] dates = getDateArrByQuarter(date, quarter);

        Date targetDate = dates[1];
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Integer result = Integer.parseInt(format.format(targetDate));


        return result;
    }

    /**
     * 得到传入日期所属季度上一季度的结束天
     *
     * @param date
     * @return
     */
    public static int getLastQuarterEndDate(Date date) {
        int quarter = getDateQuarter(date);
        if (quarter == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String targetDateStr = (calendar.get(Calendar.YEAR) - 1) + "1231";
            return Integer.parseInt(targetDateStr);

        } else {
            Date[] dates = getDateArrByQuarter(date, quarter - 1);
            Date targetDate = dates[1];
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Integer result = Integer.parseInt(format.format(targetDate));
            return result;
        }

    }


    /**
     * * 功能：获取指定时间季度结束后的一个月 （季度结束后的一个月为打分月）
     * * @param appointDate 指定当前年
     * * @param appointIndex
     * * @return
     */
    public static Date getDateByQuterLastMonthAddOne(Date appointDate, int appointIndex) throws IllegalArgumentException {
        Date stime = null;
        Date etime = null;
        if (appointDate == null) {
            appointDate = new Date();
        }
        Date tempDate = DateUtils.truncate(appointDate, Calendar.YEAR);

        if (appointIndex == 1) {
            stime = tempDate;
        } else if (appointIndex == 2) {
            stime = DateUtils.addMonths(tempDate, 3);
        } else if (appointIndex == 3) {
            stime = DateUtils.addMonths(tempDate, 6);
        } else if (appointIndex == 4) {
            stime = DateUtils.addMonths(tempDate, 9);
        }
        etime = DateUtils.addSeconds(DateUtils.addMonths(stime, 4), -1);
        return etime;
    }

    public static int getDiffDateQuarter(int beginMoth, int diffMonth) {
        int dateQuarter = 0;
        for (int i = beginMoth; i <= beginMoth + diffMonth; i++) {
            if (i % 3 == 0) {
                dateQuarter++;
            }

        }
        return dateQuarter >= 1 ? dateQuarter - 1 : 0;
    }

    public static int getDateNowYear(Date date, int diffMonth) {
        int year = 0;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, diffMonth);
            year = calendar.get(Calendar.YEAR);
        }
        return year;
    }

    public static List<Integer> getReportDateList(int begin, int end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        List<Integer> ret = new ArrayList<Integer>();
        try {
            Date beginDate = sdf.parse(String.valueOf(begin));
            Date endDate = sdf.parse(String.valueOf(end));
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);
            while (beginDate.before(endDate)) {
                int year = cal.get(Calendar.YEAR);
                Integer quarterEnd1 = Integer.parseInt(String.valueOf(year) + "0331");
                Integer quarterEnd2 = Integer.parseInt(String.valueOf(year) + "0630");
                Integer quarterEnd3 = Integer.parseInt(String.valueOf(year) + "0930");
                Integer quarterEnd4 = Integer.parseInt(String.valueOf(year) + "1231");
                if (Integer.parseInt(sdf.format(cal.getTime())) <= quarterEnd1) {
                    if (!ret.contains(quarterEnd1)) {
                        ret.add(quarterEnd1);
                    }
                }
                if (Integer.parseInt(sdf.format(cal.getTime())) <= quarterEnd2) {
                    if (!ret.contains(quarterEnd2)) {
                        ret.add(quarterEnd2);
                    }
                }
                if (Integer.parseInt(sdf.format(cal.getTime())) <= quarterEnd3) {
                    if (!ret.contains(quarterEnd3)) {
                        ret.add(quarterEnd3);
                    }
                }
                if (Integer.parseInt(sdf.format(cal.getTime())) <= quarterEnd4) {
                    if (!ret.contains(quarterEnd4)) {
                        ret.add(quarterEnd4);
                    }
                }
                cal.add(Calendar.YEAR, 1);
                cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
                beginDate = cal.getTime();
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex, ex);
        }
        return ret;
    }

    public static Integer getYearNextDate(Date date, int year) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, year);
            return getIntegerDate(calendar.getTime());
        }
        return null;
    }


    /**
     * 功能：得到当前月份月初 格式为：xxxx-yy-zz (eg: 2007-12-01)
     * @param year
     * @param month month从1开始
     * @return String
     * @author
     */
    public static String getMonthStart(int year, int month) {
        String strMonth = null;
        strMonth = month >= 10 ? String.valueOf(month) : ("0" + month);
        return year + "-" + strMonth + "-01";

    }

    /**
     * 功能：得到当前月份月底 格式为：xxxx-yy-zz (eg: 2007-12-31)
     *
     * @param year
     * @param month month从1开始
     * @return String
     * @author pure
     */
    public static String getMonthEnd(int year, int month) {
        String strMonth = null;
        String strDay = null;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,(month-1));
        
        int day=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    
        strDay  = day >= 10 ? String.valueOf(day) : ("0" + day);
        strMonth = month >= 10 ? String.valueOf(month) : ("0" + month);
        return year + "-" + strMonth + "-" + strDay;
    }


     /**
     * 得到截止到现在的月份
     * @return
     */
     public static List<Integer> getMonthsTillNow() {
        List<Integer> months=new ArrayList<Integer>();
        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        for(int i=0;i<=month;i++){
             months.add(i+1);
        }
        return months;
    }


    /**
     * * 功能：判断输入年份是否为闰年<br>
     * 　判定公历闰年遵循的一般规律为:四年一闰,百年不闰,四百年再闰.
     *
     * @param year
     * @return 是：true  否：false
     * @author pure
     */
    public static boolean leapYear(int year) {
        boolean leap;
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) leap = true;
                else leap = false;
            } else leap = true;
        } else leap = false;
        return leap;
    }


    /**
     * 得上个月的月末 ，如果当前是1月份，则返回上一年最后一个月月末
     * @param year
     * @param month
     * @return
     */
    public static Date getLastMonthEnd(int year, int month) {
        int lastMonth=month-1;

        /*
        上一年的最后一个月末
         */
        if(lastMonth==0){
            lastMonth=12;
            year=year-1;
        }
          Date result=getDate(getMonthEnd(year,lastMonth));         
        return result;
    }
    
    public static List<String> get7Week(String date){
    	List<String> list = Lists.newArrayList();
    	try {
			Date cdate = DateUtils.parseDate(date,  new String[]{"yyyy-MM-dd"});
			Calendar cal =Calendar.getInstance();
			cal.setTime(cdate);
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		} catch (ParseException e) {
			LOGGER.error("error", e);
		}
    	return list;
    }

    public static String getDateFormatStr(Date date,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String ret = null;
        try {
            ret = sdf.format(date);
        }catch (Exception ex) {
            LOGGER.debug("日期错误[" + date + "]");
        }
        return ret;
    }

    public static String getTargetDateByDate(String strData,int due) {
        String preDate = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strData);
        } catch (Exception e) {

        }
        c.setTime(date);
        int curDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, curDay - due);
        preDate = sdf.format(c.getTime());
        return preDate;
    }

    public static String getPreDateByDate(String strData,int due) {
        String preDate = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        java.util.Date date = null;
        try {
            date = sdf.parse(strData);
        } catch (Exception e) {

        }
        c.setTime(date);
        int curDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, curDay - due);
        preDate = sdf.format(c.getTime());
        return preDate;
    }


}
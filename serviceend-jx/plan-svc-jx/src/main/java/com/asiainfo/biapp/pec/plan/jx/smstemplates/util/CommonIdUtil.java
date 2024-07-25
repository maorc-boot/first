package com.asiainfo.biapp.pec.plan.jx.smstemplates.util;

import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonIdUtil {

	
	private static String lastTime = "";// 记录最新的时间戳
	
	/**
	 * 根据时间戳生成Id，默认17位
	 * 
	 * @return YYYYMMDDHHMMSSXXX
	 */
	public synchronized static String generateId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateStr = sdf.format(new Date());
		// 唯一性保证
		if (StringUtils.isNotEmpty(lastTime)
				&& Long.valueOf(lastTime) >= Long.valueOf(dateStr)) {
			dateStr = generateId();
		} else {
			lastTime = dateStr;
		}
		return dateStr;
	}
	
	public static String getRandom(int max) throws Exception {
		return 1 + (int) (Math.random() * max) + "";
	}
	
	public static void main(String[] args) {
		try {
			for (int i = 0; i < 13; i++) {
			}
		} catch (Exception e) {
			log.error("error", e);
		}
	}
	public static String getDateFormatStr(Date date,String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);	
	}
	
	public static List<String> serializeToList(int heap) throws Exception {
		List<String> list = null;
		if (heap > 0) {
			list = new ArrayList<String>();
			for (int i = 1; i <= heap; i++) {
				list.add(numberAssist(i));
			}
			return list;
		}
		return new ArrayList<String>();
	}
	
	public static List<?> jsonArray2List(String jsonArrayStr,Class<?> clazz){
		JSONArray jsonarray = JSONArray.fromObject(jsonArrayStr);
		List<?> list = (List<?>) JSONArray.toCollection(jsonarray, clazz);
		return list; 
	}
	
	public static Integer pickDigits(String source) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < source.length(); index++) {
			char ch = source.charAt(index);
			if (Character.isDigit(ch)) {
				sb.append(ch);
			}
		}
		return Integer.parseInt(sb.toString());
	}

	private static String numberAssist(int i) {
		return i > 9 ? i + "" : "0" + i;
	}
	
	public static String optString(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if ((value == null) || (String.valueOf(value).trim().length() < 1)){
			return "";
		}else{
			return String.valueOf(value);
		}
	}
	

	/**
	 * 获取指定的年月。
	 * @return
	 */
	public static String getYM(int n){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, n);
		
		// DateFormat
		DateFormat df = new SimpleDateFormat("yyyyMM");
		return df.format(cal.getTime()).toString();
	}
	
	public static String getYMD(int n){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, n);
		// DateFormat
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(cal.getTime()).toString();
	}
	
	public static String getYMDM(int n){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, n);
		// DateFormat
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(cal.getTime()).toString();
	}
	
	
	/**
	 * 获取当前的年月。
	 * @return
	 */
	public static String getYMCurrent(){
		Calendar cal = Calendar.getInstance();
		
		// DateFormat
		DateFormat df = new SimpleDateFormat("yyyyMM");
		return df.format(cal.getTime()).toString();
	}
	
	/**
	 * 返回月的最大天数。
	 * @return 天数
	 */
	public static int getDayOfMonth(){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int day=aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}
	/**
	 * 
	 * @Title: listToString  
	 * @Description: 将list中每条记录的指定字段的值用指定符号链接，并返回结果
	 * @author 汪斌
	 * @date 2017年4月27日 上午9:50:34
	 * @param list
	 * @param filedName
	 * @param separator
	 * @return
	 */
	public static String listToString(List<Map<String,Object>> list, String filedName,char separator){
		StringBuffer res= new StringBuffer();
		if(list==null || list.size()==0)return null;
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=list.get(i);
			if(!map.containsKey(filedName)){
				return null;
			}else{
				String value = map.get(filedName)==null?"": map.get(filedName).toString();
				res.append(value);
				if(i!=list.size()-1){
					res.append(separator);
				}
				
			}
			
		}
		return res.toString();
	}
	
	  /**
     * 
     * @Title: convertNumFiled  
     * @Description: 将list中每条数据的字段字段转化成百分比
     * @author 汪斌
     * @date 2017年4月26日 下午6:02:59
     * @param list
     * @param numFileds
     */
	public static void convertNumFiled(List<Map<String, Object>> list,List<String> numFileds){
		if(CollectionUtils.isEmpty(list))return;
		if(CollectionUtils.isEmpty(numFileds))return;
		DecimalFormat decimalFormat=new DecimalFormat("0.00%");
		for(Map<String, Object> column:list){
			for(String key:numFileds){
				if(column.containsKey(key)){
					String o = column.get(key)==null?"0.00%":decimalFormat.format(Double.parseDouble(column.get(key).toString()));
					column.put(key,o);
				}
			}
		}
	}

	/**
	 * 将list中每条数据的字段字段转化成百分比,不带百分号“%”
	 * @param list
	 * @param numFileds
	 */
	public static void convertNumFiledWithoutSign(List<Map<String, Object>> list,List<String> numFileds){
		if(CollectionUtils.isEmpty(list))return;
		if(CollectionUtils.isEmpty(numFileds))return;
		DecimalFormat decimalFormat=new DecimalFormat("0.0000");
		for(Map<String, Object> column:list){
			for(String key:numFileds){
				if(column.containsKey(key)){
					String o = column.get(key)==null?"0.00":decimalFormat.format(Double.parseDouble(column.get(key).toString()));
					column.put(key,o);
				}
			}
		}
	}
	
	/**
	 * 给路径末尾添加斜线
	 * @param str 路径
	 * @return
	 */
	public static String addSlash(String str){
		return str.endsWith(File.separator) ? str : str + File.separator;
	}
	
	/*
     * 取开始日期与结束日期之间的所有月份
     */
	public static List<String> getAllMonth(String strStartDate, String strEndDate) throws ParseException{
    	List<String> listDate = new ArrayList<String>();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetSdf = new SimpleDateFormat("yyyyMM");  
        
        Date startDate = sdf.parse(strStartDate);  
        Date endDate = sdf.parse(strEndDate);   
        
        listDate.add(targetSdf.format(startDate));// 把开始时间加入集合
        
        Calendar cal = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间  
        cal.setTime(startDate);  
        cal.set(Calendar.DAY_OF_MONTH, 1);
        boolean bContinue = true;
        while (bContinue) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            cal.add(Calendar.MONTH, 1);
            // 测试此日期是否在指定日期之后  
            if (endDate.after(cal.getTime()) || endDate.equals(cal.getTime())) {  
            	listDate.add(targetSdf.format(cal.getTime()));  
            } else {  
                break;  
            }  
        }  
        return listDate;  
    }

	/**
	 * 保存上传的文件
	 * Company: 亚信科技（中国）有限公司
	 * @author wangbin
	 * @date 2018年5月2日 下午5:24:32
	 * @param file
	 * @param localpathInDic 数据字典中配置本地目录的key
	 * @return 文件全路径。如果上传失败返回null
	 */
	public static String saveFile(MultipartFile file, String localpathInDic) {
    	String wholeFilePath=null;
    	try {
			String uploadFilePath = RedisUtils.getDicValue(localpathInDic);
			String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")) + "_"+CommonIdUtil.generateId().substring(12) + "."
					+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			String osName = System.getProperty("os.name");
			String filePath = null;
			if (osName.toLowerCase().startsWith("win")) {
				filePath = uploadFilePath.replace("/", File.separator).replace("\\", File.separator);
			} else {
				filePath = uploadFilePath;
			}
			//配置的目录路径不存在则创建目录
			File f = new File(filePath);
			if(!f.exists()){
				f.mkdirs();
			}
			wholeFilePath = CommonIdUtil.addSlash(filePath)+fileName;
			log.info("start to upload file：{}",wholeFilePath);
			File dest = new File(wholeFilePath);
			if (!dest.exists()) {
				dest.createNewFile();
			}
			file.transferTo(dest);
		} catch (Exception e) {
			log.error("上传失败", e);
		}
		return wholeFilePath;
    }

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(Object obj) {
		T cloneObj = null;
		ObjectInputStream ois = null;
		ObjectOutputStream obs = null;
		try {
			// 写入字节流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			obs = new ObjectOutputStream(out);
			obs.writeObject(obj);
			// 分配内存，写入原始对象，生成新对象
			ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
			ois = new ObjectInputStream(ios);
			// 返回生成的新对象
			cloneObj = (T) ois.readObject();
		} catch (Exception e) {
			log.error("对象反序列化方式clone失败", e);
		}finally {
			if(ois != null){
				try{
					ois.close();
				}catch (Exception e){
					log.error("流关闭异常", e);
				}
			}
			if(obs != null){
				try{
					obs.close();
				}catch (Exception e){
					log.error("流关闭异常", e);
				}
			}
		}
		return cloneObj;
	}

	public static BigDecimal getBigDecimal(Object value ) {
		BigDecimal ret = null;
		if( value != null ) {
			if( value instanceof BigDecimal ) {
				ret = (BigDecimal) value;
			} else if( value instanceof String ) {
				ret = new BigDecimal( (String) value );
			} else if( value instanceof BigInteger) {
				ret = new BigDecimal( (BigInteger) value );
			} else if( value instanceof Number ) {
				ret = BigDecimal.valueOf(((Number)value).doubleValue() );
			} else {
				throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
			}
		}
		return ret;
	}
}

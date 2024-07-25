package com.asiainfo.biapp.pec.plan.jx.hmh5.contant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yantao
 * @version : v1.0
 * @className : CityMappingMap
 * @description : [地市映射]
 * @createTime : [2023-12-27 上午 11:36:48]
 */
public class CityMappingMap {
	private Map<String, String> cityCodeMap;

	public CityMappingMap() {
		cityCodeMap = new HashMap<>();
		// cityCodeMap.put("省公司", "999");
		cityCodeMap.put("萍乡", "799");
		cityCodeMap.put("景德镇", "798");
		cityCodeMap.put("赣州", "797");
		cityCodeMap.put("吉安", "796");
		cityCodeMap.put("宜春", "795");
		cityCodeMap.put("抚州", "794");
		cityCodeMap.put("上饶", "793");
		cityCodeMap.put("九江", "792");
		cityCodeMap.put("南昌", "791");
		cityCodeMap.put("新余", "790");
		cityCodeMap.put("鹰潭", "701");
	}

	public String getCode(String countyName) {
		return cityCodeMap.get(countyName);
	}
}

package com.asiainfo.biapp.pec.eval.jx.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.Map;


/**
 * 系统用户
 * @author hjn
 *
 */
@Data
public class User {

	/**
	 * 用户ID
	 */
	private String id;
	
	/**
	 * 用户姓名
	 */
	private String name;
	
	/**
	 * 用户密码
	 */
	private String pwd;
	
	/**
	 * 用户所属地市
	 */
	private String cityId;

	private String cityName;
	
	/**
	 * 用户所属部门ID
	 */
	private String departmentId;


	private String departmentName;
	
	/**
	 * 用户状态0禁用1启用
	 */
	private String status;
	
	/**
	 * 用户创建时间
	 */
	private Date createTime;
	
	/**
	 * 用户手机号码
	 */
	private String mobilePhone;

		/**
	 * 用户归属区县
	 */
	private String county;

	/**
	 * 用户归属网格
	 */
	private String gridId;


	/**
	 * 用户扩展信息
	 */
	private Map<String,Object> extendInfo;

}

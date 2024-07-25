package com.asiainfo.biapp.pec.plan.jx.link.vo;

import java.io.Serializable;
import java.util.List;

public class Respond implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态：
	 * 200-表示成功；</br>
	 * 201-表示失败</br>
	 */
	private String status;
	
	/**
	 * 返回给前段的成功或者失败描述信息
	 */
	private String message;
	
	/**
	 * 数据
	 */
	private List<?> data;

	/**
	 * 获取状态：
	 * 200-表示成功；</br>
	 * 201-表示失败</br>
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置状态：
	 * 200-表示成功；</br>
	 * 201-表示失败</br>
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取返回给前段的成功或者失败描述信息
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置返回给前段的成功或者失败描述信息
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 获取数据
	 * @return
	 */
	public List<?> getData() {
		return data;
	}

	/**
	 * 设置数据
	 * @param data
	 */
	public void setData(List<?> data) {
		this.data = data;
	}

}

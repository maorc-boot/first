package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用信息
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/3 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseInfo {
	//子节点ID
	private String labelId;
	//标签名称
	private String labelName;
	//标签属性值
	private String labelValue;

}

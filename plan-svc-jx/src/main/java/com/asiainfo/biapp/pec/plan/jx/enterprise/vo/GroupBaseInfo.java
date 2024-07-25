package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 集团画像-集团基础信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupBaseInfo {
	//集团编码
	private String groupCode = "-";
	//集团名称
	private String groupName = "-";
	//客户经理名称
	private String managerName = "-";
	//关键人
	private String keyPerson = "-";
	//集团价值等级(A、 B、 C、 D)
	private String valueGrade = "-";
	//地址
	private String address = "-";
	//行业属性
	private String industryAttr = "-";

}

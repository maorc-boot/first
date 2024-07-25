package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户通+网格通任务类型查询
 *
 * @author admin
 * @version 1.0
 * @date 2023-01-03 下午 15:11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskTypeDetail {
	@ApiModelProperty(value = "类型名称")
	private String typeName;
	@ApiModelProperty(value = "类型排序")
	private String typeOrder;
	@ApiModelProperty(value = "类型编码")
	private String typeCode;
	@ApiModelProperty(value = "父类型编码")
	private String pcode;
	@ApiModelProperty(value = "是否可用，0可用，1不可用")
	private String isInuse;
	@ApiModelProperty(value = "带维系用户")
	private String uncontact;
	@ApiModelProperty(value = "已接触用户")
	private String hasContact;
	@ApiModelProperty(value = "全部用户")
	private String allAll;

}

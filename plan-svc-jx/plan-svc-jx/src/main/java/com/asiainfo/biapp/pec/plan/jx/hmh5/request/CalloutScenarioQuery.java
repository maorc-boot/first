package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : yantao
 * @version : v1.0
 * @className : CalloutScenarioQuery
 * @description : [描述说明该类的功能]
 * @createTime : [2024-07-12 上午 11:31:09]
 */
@ApiModel(value = "江西客户通外呼场景查询入参")
@Data
public class CalloutScenarioQuery {
	@ApiModelProperty(value = "每页大小，默认值10")
	private Integer size = 10;

	@ApiModelProperty(value = "当前页码，默认1")
	private Integer current = 1;

	@ApiModelProperty(value = "场景,1-所有场景，")
	private String scenarioCode;

	@ApiModelProperty(value = "地市,1-所有地市")
	private String cityId;

	@ApiModelProperty(value = "个性化规则已接通 次数/周期:0-日，1-月")
	private String frequencyNumberOne;

	@ApiModelProperty(value = "个性化规则已接通 次数/周期:0-日，1-月")
	private String frequencyNumberTwo;

	@ApiModelProperty(value = "个性化规则未接通 次数/周期:0-日，1-月")
	private String frequencyNumberOneNo;

	@ApiModelProperty(value = "个性化规则未接通 次数/周期:0-日，1-月")
	private String frequencyNumberTwoNo;

	@ApiModelProperty(value = "审批状态，0-全部，1-审批中，3-草稿，4-已驳回，5-审批完成")
	private String approvalType;


}

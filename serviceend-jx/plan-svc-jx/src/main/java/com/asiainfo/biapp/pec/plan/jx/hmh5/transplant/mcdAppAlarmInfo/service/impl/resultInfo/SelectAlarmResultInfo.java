package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.service.impl.resultInfo
 * @className: SelectAlarmResultInfo
 * @author: chenlin
 * @description: 自定义预警页面查询结果模型
 * @date: 2023/6/28 15:30
 * @version: 1.0
 */
@Data
@ApiModel("自定义预警页面查询结果模型")
public class SelectAlarmResultInfo {

    @ApiModelProperty(value = "预警alarmId，当查询预警详情时传入")
    private Integer alarmId;

    @ApiModelProperty(value = "预警名称")
    private String alarmName;

    @ApiModelProperty(value = "预警类型名称")
    private String typeName;

    @ApiModelProperty(value = "来源表")
    private String sourceTable;


    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(value = "审批状态")
    private String approveStatus;

    @ApiModelProperty(value = "创建人")
    private String userName;

    @ApiModelProperty(value = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "创建人地市id")
    private String cityId;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

}

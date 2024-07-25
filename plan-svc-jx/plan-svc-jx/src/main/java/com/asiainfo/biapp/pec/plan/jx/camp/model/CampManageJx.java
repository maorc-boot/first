package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.enums.TransEnum;
import com.asiainfo.biapp.pec.core.enums.TransPropEnum;
import com.asiainfo.biapp.pec.plan.model.McdCampStepMonitor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author : ranpf
 * @date : 2023-4-09 20:20:36
 */
@ApiModel(value = "江西营销策划管理实体", description = "江西营销策划管理实体")
@Data
@TransEnum(clz = CampStatus.class)
public class CampManageJx  {

    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("策略名称")
    private String campsegName;
    @ApiModelProperty("策划id")
    private String campsegId;
    @ApiModelProperty("开始时间")
    private Date startDate;
    @ApiModelProperty("结束时间")
    private Date endDate;
    @ApiModelProperty("创建人")
    private String createUsername;
    @ApiModelProperty("策略状态")
    @TransPropEnum(type = "CampStatus", keyCol = "campsegStatName")
    private Integer campsegStatId;
    @ApiModelProperty("策略状态名称")
    private String campsegStatName;
    @ApiModelProperty("审批流程id")
    private String approveFlowId;
    @ApiModelProperty("归属")
    private String isMy;
    @ApiModelProperty("归属地市")
    private String cityId;
    @ApiModelProperty("DQ_1")
    private String dq1;
    @ApiModelProperty("DQ_2")
    private String dq2;
    @ApiModelProperty("DQ_3")
    private String dq3;
    @ApiModelProperty("DQ_D_1")
    private String dqd1;
    @ApiModelProperty("DQ_D_2")
    private String dqd2;
    @ApiModelProperty("DQ_D_3")
    private String dqd3;
    @ApiModelProperty("暂停原因")
    private String pauseComment;
    @ApiModelProperty("预演策略 1是0否")
    private String previewCamp;
    @ApiModelProperty("渠道ID列表")
    private List<String> channelIds;
    @ApiModelProperty("是否有下级")
    private Boolean hasChild = true;
    @ApiModelProperty("参与类型")
    private String taskType;
    @ApiModelProperty("活动监控步骤")
    private List<McdCampStepMonitor> campStepMonitors;
//    @ApiModelProperty("子策略列表")
//    private List<CampManageChildren> children;
}

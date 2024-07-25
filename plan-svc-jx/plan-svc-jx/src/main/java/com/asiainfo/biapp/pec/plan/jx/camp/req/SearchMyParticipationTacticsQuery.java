package com.asiainfo.biapp.pec.plan.jx.camp.req;


import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author : ranpf
 * @date : 2023-4-4 15:01:53
 */
@Data
@ApiModel(value = "营销管理我参与的策略请求实体", description = "营销管理我参与的策略请求实体")
@ToString
public class SearchMyParticipationTacticsQuery   {

    @ApiModelProperty(value = "策略编号或名称")
    private String campsegIdOrName;
    @ApiModelProperty(value = "创建人" )
    private String createUserId;
    @ApiModelProperty(value = "渠道编码")
    private String channelId;
    @ApiModelProperty(value = "活动状态")
    private Short campsegStatId;
    @ApiModelProperty(value = "参与类型：0审批,1阅知")
    private String taskType;
    @ApiModelProperty(value = "是否是我的策略：2我参与的策略")
    private Integer isSelectMy;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "活动开始时间")
    private String campStartDay;
    @ApiModelProperty(value = "活动结束时间")
    private String campEndDay;
    @ApiModelProperty(value = "操作开始时间")
    private String operateStartDay;
    @ApiModelProperty(value = "操作结束时间")
    private String operateEndDay;
    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;
    @ApiModelProperty(value = "当前登录人")
    private String loginUserId;


}

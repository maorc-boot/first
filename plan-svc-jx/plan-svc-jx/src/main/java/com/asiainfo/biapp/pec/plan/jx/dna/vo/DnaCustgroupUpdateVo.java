package com.asiainfo.biapp.pec.plan.jx.dna.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 需要更新的周期性客群对象
 *
 * @author: lvchaochao
 * @date: 2023/12/19
 */
@Data
@ApiModel("需要更新的周期性客群对象")
public class DnaCustgroupUpdateVo {

    @ApiModelProperty("活动id")
    private String campsegId;

    @ApiModelProperty(value = "客户群ID")
    private String customGroupId;

    @ApiModelProperty(value = "客户群名称")
    private String customGroupName;

    @ApiModelProperty(value = "客户群描述")
    private String customGroupDesc;

    @ApiModelProperty(value = "客户群数量")
    private Integer customNum;

    @ApiModelProperty(value = "客户群实际数量")
    private Integer actualCustomNum;

    @ApiModelProperty(value = "客户群状态0：无效；1：有效；2：删除；3：提取处理中；4：提取失败；9：客户群导入失败；10：入库时异常；")
    private Integer customStatusId;

    @ApiModelProperty(value = "数据来源1:coc 2:dna 3:多波次")
    private String customSourceId;

    @ApiModelProperty(value = "创建人ID")
    private String createUserId;

    @ApiModelProperty(value = "推送用户ID")
    private String pushTargetUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "生效时间")
    private Date effectiveTime;

    @ApiModelProperty(value = "失效时间")
    private Date failTime;

    @ApiModelProperty(value = "客户群周期1：一次性，2：月周期，3：日周期")
    private Integer updateCycle;

    @ApiModelProperty(value = "客户群表名")
    private String listTableName;

    @ApiModelProperty(value = "数据日期")
    private Integer dataDate;

    @ApiModelProperty(value = "规则描述")
    private String ruleDesc;

    @ApiModelProperty(value = "同步次数")
    private Integer syncTimes;

    @ApiModelProperty(value = "客户群类型,状态 0:普通; 1:外呼专用客户群; 2:分箱客户群; 3:政企客户群; 4:网格系统客户群; 5:事件累计客户群")
    private Integer custType;

    @ApiModelProperty(value = "是否推送其他")
    private Integer isPushOther;

    @ApiModelProperty(value = "清单文件名")
    private String fileName;

}

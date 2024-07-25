package com.asiainfo.biapp.pec.approve.jx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApprovalProcessCmpDef对象", description = "审批策略活动信息")
public class ApprovalProcessCampDef {

    @ApiModelProperty(value = "根id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String rootId;

    @ApiModelProperty(value = "客户群ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String custgroupId;

    @ApiModelProperty(value = "活动ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String campsegId;

    @ApiModelProperty(value = "活动ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String channelId;

    @ApiModelProperty(value = "活动ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String channelAdivId;

    @ApiModelProperty(value = "活动ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String planId;

    @ApiModelProperty(value = "事件ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private String cepEventId;

    @ApiModelProperty(value = "活动开始时间")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyyMMddHHmmss",timezone ="GMT+8")
    @JsonSerialize(using = ToStringSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "活动结束时间")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyyMMddHHmmss",timezone ="GMT+8")
    @JsonSerialize(using = ToStringSerializer.class)
    private Date endDate;
}

package com.asiainfo.biapp.pec.plan.jx.link.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("链接库VO")
public class McdAllLink {
    @ApiModelProperty("链接id")
    private String linkId;
    @ApiModelProperty("链接name")
    private String linkName;
    @ApiModelProperty("开始时间")
    private String createDateS;
    @ApiModelProperty("结束时间")
    private String createDateE;
    @ApiModelProperty("当前登录人账号")
    private String linkCreator;
    @ApiModelProperty("链接url")
    private String linkUrl;
    @ApiModelProperty("产品id")
    private String planId;
    @ApiModelProperty("产品name")
    private String planName;
    @ApiModelProperty("当前页")
    private String pageNum;
    @ApiModelProperty("每页大小")
    private String pageSize;
    @ApiModelProperty("开关 链接库页面传 1 ,渠道应用页面传 2")
    private String aswitch;
    @ApiModelProperty("链接,说明（描述）")
    private String linkDescription;
    @ApiModelProperty("状态 0新增 ,2删除,3 修改")
    private String status;
}

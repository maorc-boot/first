package com.asiainfo.biapp.pec.plan.jx.camp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/28
 */
@Data
@ApiModel(value = "活动详情:执行信息", description = "活动详情:执行信息")
public class CampExecInfo {

    @ApiModelProperty(value = "根活动ID")
    private String campsegRootId;
    @ApiModelProperty(value = "子活动ID")
    private String campsegId;
    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "运营位ID")
    private String channelAdivId;
    @ApiModelProperty(value = "运营位名称")
    private String channelAdivName;
    @ApiModelProperty(value = "执行时间")
    private String dataDate;
    @ApiModelProperty(value = "当日展示政策次数")
    private String showNumDay = "0";
    @ApiModelProperty(value = "累计展示政策次数")
    private String showNumAll = "0";

    @ApiModelProperty(value = "当日点击政策次数")
    private String clickNumDay = "0";
    @ApiModelProperty(value = "累计点击政策次数")
    private String clickNumAll = "0";

    @ApiModelProperty(value = "当日办理成功量")
    private String orderNumDay = "0";
    @ApiModelProperty(value = "累计办理成功量")
    private String orderNumAll = "0";

    @ApiModelProperty(value = "当日短信发送量")
    private String smsNumDay = "0";
    @ApiModelProperty(value = "累计短信发送量")
    private String smsNumAll = "0";

}

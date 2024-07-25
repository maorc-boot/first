package com.asiainfo.biapp.pec.approve.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * 策略渠道相关信息
 * </p>
 *
 * @author ranpf
 * @since 2023-11-16
 */
@Data
@ApiModel(value="McdCampChannelDetail对象", description="策略渠道基础属性信息")
public class McdCampChannelDetailVo   {

    @ApiModelProperty(value = "子策略ID")
    private String campsegId;

    @ApiModelProperty(value = "父策略ID")
    private String campsegPid;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    @ApiModelProperty(value = "运营位ID")
    private String adivId;

    @ApiModelProperty(value = "运营位名称")
    private String adivName;

    @ApiModelProperty(value = "推荐用语")
    private String execContent;

    @ApiModelProperty(value = "实时事件ID")
    private String cepEventId;

    @ApiModelProperty(value = "实时场景名称")
    private String cepSceneName;

    @ApiModelProperty(value = "频次控制参数，格式：几天_几次")
    private String frequency;

    @ApiModelProperty(value = "短信用语")
    private String smsContent;

    @ApiModelProperty(value = "执行周期")
    private String updateCycle;

    @ApiModelProperty("渠道扩展信息")
    private List<McdCampChannelExtVo> mcdChannelDefExtList;

}

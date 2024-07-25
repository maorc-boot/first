package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * description: 5G应用号密钥映射
 *
 * @author lvcc
 * @date 2022/12/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("FIVEG_APPID_APPSECRET_REL")
@ApiModel(value = "5G应用号密钥映射对象", description = "5G应用号密钥映射表")
public class FiveGAppIdAppSecretRelVo extends Model<FiveGAppIdAppSecretRelVo> {

    @ApiModelProperty(value = "应用号名称")
    @TableField("APPLICATION_NAME")
    private String applicationName;

    @ApiModelProperty(value = "应用号id")
    @TableField("APPLICATION_ID")
    private String applicationId;

    @ApiModelProperty(value = "app密钥")
    @TableField("APPSECRET")
    private String appSecret;

    @ApiModelProperty(value = "下发号id")
    @TableField("ID")
    private String id;

    @ApiModelProperty(value = "渠道id")
    @TableField("CHANNEL_ID")
    private String channelId;

}

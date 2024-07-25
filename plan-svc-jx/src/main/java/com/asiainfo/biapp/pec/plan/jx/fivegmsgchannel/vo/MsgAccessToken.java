package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 5G消息token存放表实体
 *
 * @author lvcc
 * @date 2023/02/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("msg_access_token")
@ApiModel(value = "5G消息token存放表", description = "5G消息token存放表")
public class MsgAccessToken {

	@ApiModelProperty(value = "应用号对应的appId")
	@TableField("username")
	private String userName;

	@ApiModelProperty(value = "获取的token")
	@TableField("token")
	private String token;

	@ApiModelProperty(value = "更新时间")
	@TableField("update_time")
	private Date updateTime;

}
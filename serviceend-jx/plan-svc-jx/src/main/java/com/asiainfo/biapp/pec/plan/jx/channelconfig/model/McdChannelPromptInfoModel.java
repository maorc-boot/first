package com.asiainfo.biapp.pec.plan.jx.channelconfig.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("江西渠道文字提示和附件配置信息")
@TableName("mcd_channel_prompt_info")
public class McdChannelPromptInfoModel {

    @ApiModelProperty("渠道ID")
    @TableId("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty("提示语")
    @TableField("PROMPT_WORD")
    private String promptWord;

    @ApiModelProperty("创建人")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty("附件名称")
    @TableField("FILE_NAME")
    private String fileName;

    @ApiModelProperty("附件上传地址")
    @TableField("FILE_UPLOAD_URL")
    private String fileUploadUrl;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private String createTime;

    @ApiModelProperty("更新时间")
    @TableField("UPDATE_TIME")
    private String updateTime;

}

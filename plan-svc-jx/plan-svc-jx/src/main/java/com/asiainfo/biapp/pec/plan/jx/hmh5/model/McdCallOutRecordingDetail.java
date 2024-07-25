package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author suny3
 * @date 2024/06/17
 */
@Data
@ApiModel(value = "外呼详情:通话明细", description = "外呼详情:通话明细")
@TableName("MCD_CALL_OUT_RECORDING_DETAIL")
public class McdCallOutRecordingDetail {
    @ApiModelProperty(value = "通话唯一标识")
    @TableId("CALLIDENTIFIER")
    private String callidentifier;

    @ApiModelProperty(value = "主叫号码")
    @TableField("CALLER")
    private String caller;

    @ApiModelProperty(value = "被叫号码")
    @TableField("CALLEE")
    private String callee;

    @ApiModelProperty(value = "外显号码")
    @TableField("DISPLAY")
    private String disPlay;
    @ApiModelProperty(value = "通话开始时间")
    @TableField("START_TIME")
    private Date startTime;

    @ApiModelProperty(value = "通话结束时间")
    @TableField("END_TIME")
    private Date endTime;

    @ApiModelProperty(value = "外呼结果：1 成功；2 失败")
    @TableField("CALL_RESULT")
    private Integer callResult;

    @ApiModelProperty(value = "客户通话时长，单位秒")
    @TableField("TALK_DURATION")
    private Integer talkDuration;

    @ApiModelProperty(value = "客户振铃时长，单位秒")
    @TableField("ALERT_DURATION")
    private Date alertDuration;

    /**
     * 挂断类型:HangUp：正常挂机，包括通话后被删除;CallerBusy：主叫忙;CallerNoAnswer：
     * 主叫无应答;CallerFailure：主叫其它原因失败;CallerAbandon：呼叫被叫过程中主叫挂机;CalleeBusy：
     * 被叫忙;CalleeNoAnswer：被叫无应答;CalleeFailure：被叫其它原因失败;Other：其它原因失败;
     */
    @ApiModelProperty(value = "挂断类型:")
    @TableField("END_TYPE")
    private String endType;

    @ApiModelProperty(value = "录音文件地址")
    @TableField("AUDIO_URL")
    private String audioUrl;

    @ApiModelProperty(value = "2：wav  默认;3：mp3;")
    @TableField("AUDIO_TYPE")
    private Integer audioType;

    @ApiModelProperty(value = "录音文件产生时间")
    @TableField("AUDIO_TIME")
    private Date audioTime;

    @ApiModelProperty(value = "EXT1")
    @TableField("EXT1")
    private String ext1;

    @ApiModelProperty(value = "EXT2")
    @TableField("EXT2")
    private String ext2;

    @ApiModelProperty(value = "EXT3")
    @TableField("EXT3")
    private String ext3;

    @ApiModelProperty(value = "URL_STATUS")
    @TableField("URL_STATUS")
    private String urlStatus;
}

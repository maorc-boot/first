package com.asiainfo.biapp.pec.preview.jx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 营销活动预演日志
 * </p>
 *
 * @author mamp
 * @since 2022-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdCampPreviewLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预演日志表唯一键
     */
    @TableId(value = "LOG_ID", type = IdType.ID_WORKER_STR)
    @ApiModelProperty("预演日志表唯一键")
    private String logId;

    /**
     * 策略ID
     */
    @TableField("CAMPSEG_ID")
    @ApiModelProperty(value = "子活动ID")
    private String campsegId;

    /**
     * 根活动ID
     */
    @TableField("CAMPSEG_ROOT_ID")
    @ApiModelProperty(value = "根活动ID")
    private String campsegRootId;

    /**
     * 预演状态  1：预演中，2：预演完成，3：预演异常
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "预演状态", notes = "预演状态  1：预演中，2：预演完成，3：预演异常")
    private Integer status;

    /**
     * 目标用户数
     */
    @TableField("TARGET_CUSTOM_NUM")
    @ApiModelProperty("目标用户数")
    private Integer targetCustomNum;

    /**
     * 预演用户数
     */
    @TableField("PREVIEW_CUSTOM_NUM")
    @ApiModelProperty("预演用户数")
    private Integer previewCustomNum;

    /**
     * 预演发起人
     */
    @ApiModelProperty("预演发起人")
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 预演开始时间
     */
    @TableField("START_TIME")
    @ApiModelProperty("预演开始时间")
    private Date startTime;

    /**
     * 描述信息
     */
    @TableField("DESCRIPTION")
    @ApiModelProperty("描述信息")
    private String description;

    /**
     * 渠道ID
     */
    @TableField("CHANNEL_ID")
    @ApiModelProperty("渠道ID")
    private String channelId;

    /**
     * 客户群ID
     */
    @TableField("CUSTGROUP_ID")
    @ApiModelProperty("客户群ID")
    private String custgroupId;

    /**
     * 预演用户清单
     */
    @TableField("USER_TABLE_NAME")
    @ApiModelProperty("预演用户清单")
    private String userTableName;

    /**
     * 预演结束时间
     */
    @TableField("END_TIME")
    @ApiModelProperty("预演结束时间")
    private Date endTime;


    /**
     * 已订购剔除用户数
     */
    @TableField("FILTER_COUNT_1")
    @ApiModelProperty("已订购剔除用户数")
    private Integer filterCount1;

    /**
     * 已订购互斥剔除用户数
     */
    @TableField("FILTER_COUNT_2")
    @ApiModelProperty("已订购互斥剔除用户数")
    private Integer filterCount2;

    /**
     * 已推荐剔除用户数
     */
    @TableField("FILTER_COUNT_3")
    @ApiModelProperty("已推荐剔除用户数")
    private Integer filterCount3;

    /**
     * 频次过滤用户数
     */
    @TableField("FILTER_COUNT_4")
    @ApiModelProperty("频次过滤用户数")
    private Integer filterCount4;

    /**
     * 渠道偏好过滤用户数
     */
    @TableField("FILTER_COUNT_5")
    @ApiModelProperty("渠道偏好过滤用户数")
    private Integer filterCount5;

    /**
     * 其它特殊名单过滤
     */
    @TableField("OTHER_FILTER_COUNT")
    @ApiModelProperty("其它特殊名单过滤")
    private Integer otherFilterCount;

    /**
     * 原始客户群清单数量
     */
    @TableField("ORIGIN_CUST_NUM")
    @ApiModelProperty("原始客户群清单数量")
    private Integer originCustNum;


    @TableField("CHANNEL_SUCCESS_RATE")
    @ApiModelProperty("渠道转化率预测")
    private Double channelSuccessRate;

    @TableField("PLAN_SUCCESS_RATE")
    @ApiModelProperty("产品转化率预测")
    private Double planSuccessRate;

    @TableField("SUCCESS_RATE")
    @ApiModelProperty("转化率预测")
    private Double successRate;


}

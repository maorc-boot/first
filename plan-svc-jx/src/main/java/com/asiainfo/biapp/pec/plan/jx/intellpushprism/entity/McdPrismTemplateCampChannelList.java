package com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity;

import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 智推棱镜我的模板-策略执行基础属性表实体
 *
 * @author lvcc
 * @date 2024/05/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_prism_template_camp_channel_list")
@ApiModel(value="McdPrismTemplateCampChannelList对象", description="智推棱镜我的模板-策略执行基础属性表实体")
public class McdPrismTemplateCampChannelList extends Model<McdCampChannelList> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "策略ID")
    @TableId("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "父策略ID")
    @TableField("CAMPSEG_PID")
    private String campsegPid;

    @ApiModelProperty(value = "根策略ID")
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;

    @ApiModelProperty(value = "未分箱客户群ID")
    @TableField("P_CUSTGROUP_ID")
    private String pCustgroupId;

    @ApiModelProperty(value = "客户群ID")
    @TableField("CUSTGROUP_ID")
    private String custgroupId;

    @ApiModelProperty(value = "产品ID")
    @TableField("PLAN_ID")
    private String planId;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "运营位ID")
    @TableField("CHANNEL_ADIV_ID")
    private String channelAdivId;

    @ApiModelProperty(value = "策略实际含义 1：活 动基本信息 2：规则 3：波次")
    @TableField("CAMP_CLASS")
    private Integer campClass;

    @ApiModelProperty(value = "上一波策略ID")
    @TableField("WAVES_CAMP_PID")
    private String wavesCampPid;

    @ApiModelProperty(value = "上一波次渠道ID")
    @TableField("WAVES_CHANNEL_PID")
    private String wavesChannelPid;

    @ApiModelProperty(value = "触发规则ID")
    @TableField("WAVES_RULE_ID")
    private String wavesRuleId;

    @ApiModelProperty(value = "波次编码")
    @TableField("WAVES_NO")
    private String wavesNo;

    @ApiModelProperty(value = "分箱次数:从1开始")
    @TableField("BOX_NO")
    private Integer boxNo;

    @ApiModelProperty(value = "规则：0_1:第一个表示使用百分比规则分箱，第一个表示分箱百分比")
    @TableField("BOX_RULE_EXP")
    private String boxRuleExp;

    @ApiModelProperty(value = "规则：0_1:第一个分箱开始百分比，第一个分箱结束百分比")
    @TableField("CUSTGROUP_EXP")
    private String custgroupExp;

    @ApiModelProperty(value = "是否分箱标识  1:分箱")
    @TableField("IS_SEPARATE_BOX")
    private Integer isSeparateBox;

    @ApiModelProperty(value = "是否外呼多产品  1:是")
    @TableField("IS_OUT_CALL_PLANS")
    private Integer isOutCallPlans;

    @ApiModelProperty(value = "是否渠道偏好,1为是")
    @TableField("IS_CHANNEL_PREDILECTION")
    private String isChannelPredilection;

    @ApiModelProperty(value = "与mcd_camp_def.campseg_stat_id保持一致")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "状态变更原因")
    @TableField("REASON")
    private String reason;

    @ApiModelProperty(value = "是否有替换符")
    @TableField("IF_HAVE_VAR")
    private Boolean ifHaveVar;

    @ApiModelProperty(value = "推荐用语")
    @TableField("EXEC_CONTENT")
    private String execContent;

    @ApiModelProperty(value = "办理地址")
    @TableField("HANDLE_URL")
    private String handleUrl;

    @ApiModelProperty(value = "实时事件ID")
    @TableField("CEP_EVENT_ID")
    private String cepEventId;

    @ApiModelProperty(value = "实时事件参数")
    @TableField("EVENT_PARAM_JSON")
    private String eventParamJson;

    @ApiModelProperty(value = "实时场景名称")
    @TableField("CEP_SCENE_NAME")
    private String cepSceneName;

    @ApiModelProperty(value = "营业员营销用语")
    @TableField("MARKETING_TERM")
    private String marketingTerm;

    @ApiModelProperty(value = "PCC策略编码")
    @TableField("PCC_ID")
    private String pccId;

    @ApiModelProperty(value = "时机")
    @TableField("TIME_ID")
    private String timeId;

    @ApiModelProperty(value = "时机描述")
    @TableField("TIME_DISTINDES")
    private String timeDistindes;

    @ApiModelProperty(value = "频次控制参数，格式：几天_几次")
    @TableField("FREQUENCY")
    private String frequency;

    @ApiModelProperty(value = "客户群周期 1：一次性，2：月周期，3：日周期")
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    @ApiModelProperty(value = "短信用语")
    @TableField("SMS_CONTENT")
    private String smsContent;

    @ApiModelProperty(value = "流处理事件ID")
    @TableField("STREAM_EVENT_ID")
    private String streamEventId;

    @ApiModelProperty(value = "图片地址（完整的资源地址）")
    @TableField("MATERIAL_DISPLAY_URL")
    private String materialDisplayUrl;

    @ApiModelProperty(value = "图片跳转地址（素材点击跳转地址）")
    @TableField("MATERIAL_CLICK_URL")
    private String materialClickUrl;

    @ApiModelProperty(value = "策略优先级0-100，0最高，重复按创建时间排")
    @TableField("PRIORITY_ORDER")
    private Integer priorityOrder;

    @ApiModelProperty(value = "月执行时间")
    @TableField("EXECUTE_TIME_MONTH")
    private String executeTimeMonth;

    @ApiModelProperty(value = "周执行时间")
    @TableField("EXECUTE_TIME_WEEK")
    private String executeTimeWeek;

    @ApiModelProperty(value = "日执行时间")
    @TableField("EXECUTE_TIME_DAY")
    private String executeTimeDay;

    @ApiModelProperty(value = "开始时间")
    @TableField("START_DATE")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @TableField("END_DATE")
    private Date endDate;

    @ApiModelProperty(value = "客群来源 0-coc客群 1-DNA客群")
    @TableField("CUSTGROUP_SOURCE")
    private Integer custgroupSource;

    @Override
    protected Serializable pkVal() {
        return this.campsegId;
    }

}

package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model;

import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 预警信息表
 * </p>
 *
 * @author chenlin
 * @since 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@DataSource("khtmanageusedb")
@ApiModel("预警信息表实体对象")
public class McdAppAlarmInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警ID
     */
    @TableId(value = "ALARM_ID")
    private Integer alarmId;

    /**
     * 预警名称
     */
    @TableField("ALARM_NAME")
    private String alarmName;

    /**
     * 预警类型
     */
    @TableField("ALARM_TYPE")
    private String alarmType;

    /**
     * 数据来源表
     */
    @TableField("SOURCE_TABLE")
    private String sourceTable;

    /**
     * 预警内容
     */
    @TableField("ALARM_INFO")
    private String alarmInfo;

    /**
     * 关怀短信
     */
    @TableField("ALARM_MESSAGE")
    private String alarmMessage;

    /**
     * 数据周期：0 小时；1 每天；2 每月
     */
    @TableField("ALARM_CYCLE")
    private Integer alarmCycle;

    /**
     * 变量属性：变量名1@字段名1,变量名2@字段名2...
     */
    @TableField("ALARM_ATTR")
    private String alarmAttr;

    /**
     * 状态：1 有效 ；0 无效；2 终止
     */
    @TableField("DATA_STATUS")
    private Integer dataStatus;

    /**
     * 创建日期
     */
    @TableField("CREATE_TIME")
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建人所在城市
     */
    @TableField("USER_CITY_ID")
    private String userCityId;

    /**
     * 审批状态：0 草稿；1 审批完成；2 审批中；3 终止;4 审批驳回
     */
    @TableField("APPROVE_STATUS")
    private Integer approveStatus;

    /**
     * 周期更新时间点
     */
    @TableField("ALARM_CYCLE_TIME")
    private String alarmCycleTime;

    /**
     * 更新日期
     */
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endTime;

    /**
     * 是否已过期清理： 1 已清理 0 未清理
     */
    @TableField("IS_CLEAR")
    private Integer isClear;

    /**
     * 1:集市库1; 2:集市库2; 3:COC客户群;
     */
    @TableField("SOURCE_DB")
    private String sourceDb;

    /**
     * 是否预演过滤
     */
    @TableField("IS_PREVIEW_FILTER")
    private String isPreviewFilter;

    /**
     * 审批流程ID
     */
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;

    @TableField("AUTO_CLEAR_ALARM_STATUS")
    @ApiModelProperty(value = "是否选择自动解警，0：否，1：是", example = "0")
    private String autoClearAlarmStatus;
    /**
     * 预警下发查询条件字段
     */
    @TableField("EXECUTE_CONDITION")
    private String executeCondition;

    /**
     * 预警下发条件属性字段
     */
    @TableField("EXECUTE_CONDITION_VAL")
    private String executeConditionVal;

    /**
     * 预警执行期限
     */
    @TableField("ALARM_EXEC_DEALTIME")
    private String alarmExecDealtime;

    /**
     * 预警执行期限单位
     */
    @TableField("ALARM_EXEC_DEALTIME_UNIT")
    private String alarmExecDealtimeUnit;

    /**
     * 预警执行期限是否自选标识
     */
    @TableField("IS_DEFINE_TIME")
    private String isDefineTime;
    public void setAutoClearAlarmStatus(boolean empty, String s, String autoClearAlarmStatus) {

    }
}

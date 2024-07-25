package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户通预警报表（明细）
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportYjDetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键id
     */
    @TableId(value = "warn_id", type = IdType.AUTO)
    private Integer warnId;

    /**
     * 客户隐藏手机号（最多14个字符，原字段PRODUCT_NO_1）
     */
    @TableField("PHONE_NO_HIDE")
    private String phoneNoHide;

    /**
     * 客户手机号（最多11个字符，原字段PRODUCT_NO）
     */
    @TableField("PHONE_NO")
    private String phoneNo;

    /**
     * 预警类型（最多32个字符）
     */
    @TableField("ALARM_NAME")
    private String alarmName;

    /**
     * 创建人id（最多32个字符，原字段ALARM_CREATE_BY）
     */
    @TableField("ALARM_CREATE_USER_ID")
    private String alarmCreateUserId;

    /**
     * 创建人姓名（最多32个字符，新增字段）
     */
    @TableField("ALARM_CREATE_USERNAME")
    private String alarmCreateUsername;

    /**
     * 城市id（最多4个字符）
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 城市名（最多16个字符）
     */
    @TableField("CITY_NAME")
    private String cityName;

    /**
     * 区县id（最多8个字符）
     */
    @TableField("COUNTY_ID")
    private String countyId;

    /**
     * 区县名称（最多16个字符）
     */
    @TableField("COUNTY_NAME")
    private String countyName;

    /**
     * 渠道id（最多16个字符）
     */
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 渠道名称（最多64个字符）
     */
    @TableField("CHANNEL_NAME")
    private String channelName;

    /**
     * 网格编号（最多16个字符）
     */
    @TableField("GRID_CODE")
    private String gridCode;

    /**
     * 网格名称（最多64个字符）
     */
    @TableField("GRID_NAME")
    private String gridName;

    /**
     * 工号id（最多16个字符）
     */
    @TableField("STAFF_ID")
    private String staffId;

    /**
     * 员工姓名（最多64个字符）
     */
    @TableField("STAFF_NAME")
    private String staffName;

    /**
     * 接触时间
     */
    @TableField("RETURN_VISIT_TIME")
    private LocalDateTime returnVisitTime;

    /**
     * 接触情况（最多16个字符，原字段XX）
     */
    @TableField("JC_STATU")
    private String jcStatu;

    /**
     * 接触结果（最多16个字符，原字段XXX）
     */
    @TableField("JC_RESULT")
    private String jcResult;

    /**
     * 接触反馈（最多64个字符）
     */
    @TableField("RETURN_VISIT_NOTES")
    private String returnVisitNotes;

    /**
     * 预警创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 已办理：是，否（字符长度为3，原始数据包含空格）
     */
    @TableField("YBL")
    private String ybl;

    /**
     * 活动id（最长32个字符，应当是对应于策略id)
     */
    @TableField("CAMPSEG_ID")
    private String campsegId;

    /**
     * 活动名称（最长300个字符，应当是对应于策略名称）
     */
    @TableField("CAMPSEG_NAME")
    private String campsegName;

    /**
     * 不知道实际意义，可能为导出日期
     */
    @TableField("STAT_DATE")
    private LocalDate statDate;


}

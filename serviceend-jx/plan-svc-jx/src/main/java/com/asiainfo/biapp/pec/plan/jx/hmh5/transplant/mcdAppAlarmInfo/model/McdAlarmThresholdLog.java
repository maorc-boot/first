package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.model;

import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 预警阈值用户操作日志表
 * </p>
 *
 * @author chenlin
 * @since 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@DataSource("khtmanageusedb")
public class McdAlarmThresholdLog {
    private static final long serialVersionUID = 1L;

    /**
     * 预警ID
     */
    @TableId(value = "ALARM_ID")
    private Integer alarmId;

    /**
     * 阈值修改人
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 阈值修改人地市ID
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 预警阈值地市编号
     */
    @TableField("ALARM_CITY")
    private String alarmCity;

    /**
     * 修改后的阈值数据
     */
    @TableField("NEW_ALARM_THRESHOLD")
    private String newAlarmThreshold;

    /**
     * 修改前的阈值数据
     */
    @TableField("OLD_ALARM_THRESHOLD")
    private String oldAlarmThreshold;

    /**
     * 用户操作时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

}

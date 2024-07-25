package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 客户通预警报表（个人）
 * </p>
 *
 * @author chenlin
 * @since 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportYjStaffDetailNew implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键id
     */
    @TableId(value = "warn_id", type = IdType.AUTO)
    private Integer warnId;

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
     * 工号名称（最多64个字符）
     */
    @TableField("STAFF_NAME")
    private String staffName;

    /**
     * 预警类型（最多32个字符）
     */
    @TableField("ALARM_NAME")
    private String alarmName;

    /**
     * 近30天任务总量
     */
    @TableField("RW")
    private Integer rw;

    /**
     * 当日接触量
     */
    @TableField("DR_JC")
    private Integer drJc;

    /**
     * 当日解决量
     */
    @TableField("DR_JJ")
    private Integer drJj;

    /**
     * 近30天接触量
     */
    @TableField("JC_NUM")
    private Integer jcNum;

    /**
     * 近30天解决量
     */
    @TableField("JJ_NUM")
    private Integer jjNum;

    /**
     * 不知道实际意义，可能为导出日期
     */
    @TableField("STAT_DATE")
    private LocalDate statDate;

    /**
     * 业务办理量
     */
    @TableField("YBL")
    private Integer ybl;


}

package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 客户通客户接触维系表
 * </p>
 *
 * @author chenlin
 * @since 2023-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportSmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "SMS_ID", type = IdType.AUTO)
    private Integer smsId;

    /**
     * 维系日期
     */
    @TableField("OP_DATE")
    private LocalDate opDate;

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
     * 区县名（最多16个字符）
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
     * 网格id（最多16个字符）
     */
    @TableField("GRID_ID")
    private String gridId;

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
     * 员工名字（最多64个字符）
     */
    @TableField("STAFF_NAME")
    private String staffName;

    /**
     * 看护客户数
     */
    @TableField("KH_USER_NUM")
    private Integer khUserNum;

    /**
     * 当日短信发送量
     */
    @TableField("DX_NUM_D")
    private Integer dxNumD;

    /**
     * 当日外呼量
     */
    @TableField("WH_NUM_D")
    private Integer whNumD;

    /**
     * 当日服务客户（短信+外呼剔重量）
     */
    @TableField("DR_FW_D")
    private Integer drFwD;

    /**
     * 当月短信发送量
     */
    @TableField("DX_FS_M")
    private Integer dxFsM;

    /**
     * 当月外呼量
     */
    @TableField("WH_NUM_M")
    private Integer whNumM;

    /**
     * 当月服务客户（短信+外呼剔重量）
     */
    @TableField("DY_FW_M")
    private Integer dyFwM;

    /**
     * 客户覆盖率
     */
    @TableField("FG_RATE")
    private Float fgRate;

    /**
     * 客户满意度
     */
    @TableField("MYD_RATE")
    private Float mydRate;


}

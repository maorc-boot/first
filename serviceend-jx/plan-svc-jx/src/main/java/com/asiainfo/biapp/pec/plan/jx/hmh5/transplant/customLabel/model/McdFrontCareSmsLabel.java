package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class McdFrontCareSmsLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水序列号
     */
    @TableField("SERIAL_NUM")
    private Integer serialNum;

    /**
     * 标签名(最长64字符)
     */
    @TableField("LABEL_NAME")
    private String labelName;

    /**
     * 标签编码(最长128字符)
     */
    @TableField("LABEL_CODE")
    private String labelCode;

    /**
     * 标签描述(最长128字符)
     */
    @TableField("LABEL_DESC")
    private String labelDesc;

    /**
     * 地市编码(最长32字符)
     */
    @TableField("CITY_CODE")
    private String cityCode;

    /**
     * 数据状态(新增时设定为1, 没有找到此字段的实际意义)
     */
    @TableField("DATA_STATE")
    private Integer dataState;

    /**
     * 标签目标表名(最长64字符)
     */
    @TableField("DATA_TABLE_NAME")
    private String dataTableName;

    /**
     * 标签条件(最长500字符)
     */
    @TableField("LABEL_CONDITION")
    private String labelCondition;

    /**
     * 创建人(最长32字符)
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 新增字段，(保存用户id，也可能不使用)
     */
    // @TableField("CREATE_USER_ID")
    // private String createUserId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人(最长32字符)
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 新增字段，(保存用户id，也可能不使用)
     */
    // @TableField("UPDATE_USER_ID")
    // private String updateUserId;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;


}

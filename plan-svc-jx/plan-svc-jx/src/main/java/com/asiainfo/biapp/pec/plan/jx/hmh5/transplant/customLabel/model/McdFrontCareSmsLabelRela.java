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
public class McdFrontCareSmsLabelRela implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键字段，自增
     */
    // @TableId(value = "id", type = IdType.AUTO)
    // private Integer id;

    /**
     * 流水序列号(最长20个字符)
     */
    @TableField("SERIAL_NUM")
    private String serialNum;

    /**
     * 标签编码(最长128个字符)
     */
    @TableField("LABEL_CODE")
    private String labelCode;

    /**
     * 短信模板编码(最长64个字符)
     */
    @TableField("SMS_TEMPLATE_CODE")
    private String smsTemplateCode;

    /**
     * 数据状态(新增时为1,不知道实际意义)
     */
    @TableField("DATA_STATE")
    private Integer dataState;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;


}

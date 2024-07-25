package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelRel.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 标签模块关系表
 * </p>
 *
 * @author chenlin
 * @since 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class McdCustomLabelRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市ID
     */
    @TableId("CITY_ID")
    private String cityId;

    /**
     * 模块ID： 1.客户分布； 2.关怀短信；3.重点营销
     */
    @TableField("MODULE_ID")
    private String moduleId;

    /**
     * 标签编号
     */
    @TableField("CUSTOM_LABEL_ID")
    private String customLabelId;

    /**
     * 排序号（不知道有多少标签，暂定为int)
     */
    @TableField("SORT_NUM")
    private Integer sortNum;


}

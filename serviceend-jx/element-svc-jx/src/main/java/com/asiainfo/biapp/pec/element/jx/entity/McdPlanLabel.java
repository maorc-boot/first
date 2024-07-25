package com.asiainfo.biapp.pec.element.jx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品标签表
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdPlanLabel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 标签数据类型
     */
    @TableField("LABEL_KEY")
    private String labelKey;

    /**
     * 标签编号
     */
    @TableField("LABEL_ID")
    private String labelId;

    /**
     * 标签值
     */
    @TableField("LABEL_VALUE")
    private String labelValue;

    /**
     * 统计日期
     */
    @TableField("STAT_DATE")
    private Date statDate;


}

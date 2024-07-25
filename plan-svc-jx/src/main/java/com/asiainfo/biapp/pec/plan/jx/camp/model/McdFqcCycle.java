package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mamp
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdFqcCycle implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 周期ID
     */
    @TableField("CYCLE_ID")
    private String cycleId;

    /**
     * 周期名称
     */
    @TableField("CYCLE_NAME")
    private String cycleName;

    /**
     * 周期值（日）
     */
    @TableField("CYCLE")
    private Integer cycle;

    /**
     * 优先级（值小的优先级高）
     */
    @TableField("PRIORITY")
    private Integer priority;


}

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
 * 频次规则定义表
 * </p>
 *
 * @author mamp
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdFqcRule implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 规则ID
     */
    @TableId(value = "RULE_ID", type = IdType.AUTO)
    private Long ruleId;

    /**
     * 地市ID
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 渠道ID
     */
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 策略ID
     */
    @TableField("CAMPSEG_ID")
    private String campsegId;

    /**
     * 场景ID，资费产品规则设置为“TariffProduct”
     */
    @TableField("SCENE_ID")
    private String sceneId;

    /**
     * 周期ID;定义规则的周期
     */
    @TableField("CYCLE_ID")
    private String cycleId;

    /**
     * 规则类型;1=总控规则，2=渠道级，3=活动级别，4=特殊活动级别、5=场景级别
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 频次值
     */
    @TableField("FREQUENCY")
    private Integer frequency;


}

package com.asiainfo.biapp.pec.plan.jx.cep.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 事件共享表
 * </p>
 *
 * @author mamp
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "事件共享信息", description = "事件共享信息")
public class CepRuleShare implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件编码
     */
    @ApiModelProperty("事件编码")
    private String eventId;

    /**
     * 共享者类型: 1-角色 ,2 人员
     */
    @ApiModelProperty("共享者类型: 1-角色 ,2 人员")
    private Integer shareType;

    /**
     * 共享者ID(用户ID或者角色ID)
     */
    @ApiModelProperty("共享者ID(用户ID或者角色ID)")
    private String shareId;

    /**
     * 共享者名称(用户名称或者角色名称)
     */
    @ApiModelProperty("共享者名称(用户名称或者角色名称)")
    private String shareName;

    /**
     * 最近更新时间
     */
    private Date updateTime;


}

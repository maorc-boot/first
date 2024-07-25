//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.asiainfo.biapp.pec.plan.jx.enterprise.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class McdSysEnum {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键")
    private String enumId;
    @ApiModelProperty("枚举值类型")
    private String enumType;
    @ApiModelProperty("枚举键")
    private String enumKey;
    @ApiModelProperty("枚举值")
    private String enumValue;
    @ApiModelProperty("顺序")
    private BigDecimal enumOrder;
    @ApiModelProperty("父ID，从ENUM_ID字段取值")
    private String parentId;

}

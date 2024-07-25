package com.asiainfo.biapp.pec.client.jx.cmn.feign.vo;

import com.asiainfo.biapp.client.cmn.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户表扩展对象
 *
 * @author lvcc
 * @date 2023/12/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "返回的User对象", description = "用户表")
public class UserJxVO extends UserDTO {

    @ApiModelProperty(value = "区县ID")
    private String countyId;

    @ApiModelProperty(value = "区县名称")
    private String countyName;

    @ApiModelProperty(value = "网格ID")
    private String gridId;

    @ApiModelProperty(value = "网格名称")
    private String gridName;

    // @ApiModelProperty(value = "密码")
    // private String pwd;

    // @ApiModelProperty(value = "用户需要操作的角色ID多个以','分隔")
    // private String roleIds;

    @ApiModelProperty(value = "用户归属的角色名称多个以','分隔")
    private String roleNames;
}

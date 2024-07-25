package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * Description : 转至沟通人员
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-10
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "江西转至沟通人员信息")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class McdCommunicationUsers {


    @ApiModelProperty(value = "用户账号")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "地市ID" )
    private String cityId;

    @ApiModelProperty(value = "地市名称")
    private String cityName;

    @ApiModelProperty(value = "部门ID" )
    private  String departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "用户手机号")
    private String mobilePhone;


}

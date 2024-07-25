package com.asiainfo.biapp.pec.approve.jx.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * <p>
 * h5下级审批人用户信息
 * </p>
 *
 * @author ranpf
 * @since 2023-05-22
 */
@Data
@ApiModel(value="H5审批User对象", description="H5审批User对象")
public class H5ApproveUser  {


    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户密码")
    private String pwd;

    @ApiModelProperty(value = "状态，0正常")
    private Integer status;

    @ApiModelProperty(value = "归属地市")
    private String cityId;

    @ApiModelProperty(value = "部门ID")
    private String departmentId;

    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

    @ApiModelProperty(value = "归属区县")
    private String county;

    @ApiModelProperty(value = "归属网格")
    private String gridId;


}

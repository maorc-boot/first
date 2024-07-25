package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("江西用户角色关系数据表")
@TableName("user_user_role_relation")
public class McdUserRoleRelationModel {


    @ApiModelProperty("账号ID")
    @TableField("USER_ID")
    private String userId;
    @ApiModelProperty("角色ID")
    @TableField("ROLE_ID")
    private String roleId;




}

package com.asiainfo.biapp.pec.approve.jx.dto;

import com.asiainfo.biapp.pec.approve.jx.model.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysSysUserDTO extends SysUser {

    @ApiModelProperty(value = "用户需要操作的角色ID多个以','分隔")
    private String roleIds;

    @ApiModelProperty(value = "用户需要操作的部门ID多个以','分隔")
    private String deptIds;


    @ApiModelProperty(value = "每页数据量")
    private Integer size;


    @ApiModelProperty(value = "当前页数")
    private Integer page;


    public void checkParam() {
        if (this.page == null || this.page < 0) {
            //默认第一页
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            //默认一页30条
            setSize(30);
        }
    }

}
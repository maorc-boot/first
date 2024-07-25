package com.asiainfo.biapp.pec.plan.jx.custgroup.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("客户群管理入参模板")
public class McdCustGroupHttpVo extends McdPageQuery {

    @ApiModelProperty("客群类型,所有ALL-CUSTOM,我的MY-CUSTOM,异常ABNORMAL-CUSTOM")
    private String contentType;
    @ApiModelProperty("当前用户账号,前台不用传")
    private String userId;
    @ApiModelProperty("当前用户名称,前台不用传")
    private String userName;

}

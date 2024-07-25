package com.asiainfo.biapp.pec.plan.jx.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("江西:系统使用情况指标详情请求入参")
public class SystemUsageDetailReqQuery {

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;
   @ApiModelProperty("开始时间")
    private String beginTime;
   @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("必填:指标标识: 0 访问次数,1 客户群创建次数,2 营销活动创建次数")
    private String flag;
    @ApiModelProperty("地市ID,区域时必填")
    private String cityId;
    @ApiModelProperty("部门ID,部门时必填")
    private String deptId;
    @ApiModelProperty("账号,个人时必填")
    private String userId;



}

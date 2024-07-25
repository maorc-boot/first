package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author mamp
 * @date 2022/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "江西:单用户查询请求参数", description = "江西:单用户查询请求参数")
@ToString
public class SingleUserReq   {

    @ApiModelProperty(value = "号码")
    private String phoneNo;
    @ApiModelProperty(value = "活动状态 不限 -1, 执行中 54,执行完成90")
    private int  campsegStatus;
    @ApiModelProperty(value = "营销结果 不限-1 ,0办理失败,1办理成功,2政策曝光,100 待推荐")
    private int  marketResult;
    @ApiModelProperty(value = "营销月份   0 本月,1,上月,2上上月")
    private int  queryMonth;

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

}

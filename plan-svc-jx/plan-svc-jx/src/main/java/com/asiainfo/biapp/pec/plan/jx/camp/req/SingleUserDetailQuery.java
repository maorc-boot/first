package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ranpf
 * @date 2023/6/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SingleUserDetailQuery {

    @ApiModelProperty(value = "号码")
    private String phoneNo;
    @ApiModelProperty(value = "营销渠道")
    private String channelId;
    @ApiModelProperty(value = "策略ID")
    private String campsegId;
    @ApiModelProperty(value = "活动状态 不限 -1, 执行中 54,执行完成90")
    private String  campsegStatus;
    @ApiModelProperty(value = "营销结果 不限-1 ,0办理失败,1办理成功,2政策曝光,100 待推荐")
    private int  marketResult;
    @ApiModelProperty(value = "营销月份   0 本月,1,上月,2上上月")
    private int  queryMonth;


}

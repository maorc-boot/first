package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author : RANPF
 * @date : 2022-12-18
 * 活动信息查询入参
 */
@Data
@ApiModel(value = "江西:集团活动信息查询入参", description = "活动信息查询入参")
public class QueryActivityJxQuery extends McdPageQuery {

    @ApiModelProperty(value = "活动模板id")
    private String activityTemplateId;
    @ApiModelProperty(value = "活动id或名称")
    private String activityId;
    @ApiModelProperty(value = "活动类型")
    private String activityType;
    @ApiModelProperty(value = "活动状态")
    private String activityStatus;
    @ApiModelProperty(value = "活动开始时间")
    private String activityStartTime;
    @ApiModelProperty(value = "活动结束时间")
    private String activityEndTime;
    @ApiModelProperty(value = "用户id", hidden = true)
    private String userId;
    @ApiModelProperty(value = "用户信息", hidden = true)
    private String userInfo;

    @ApiModelProperty(value = "活动所属流程")
    private String flow;

}

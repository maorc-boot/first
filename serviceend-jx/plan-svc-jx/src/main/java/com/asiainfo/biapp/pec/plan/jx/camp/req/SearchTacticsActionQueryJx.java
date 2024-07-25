package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.plan.vo.req.SearchTacticsActionQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author mamp
 * @date 2023/4/12
 */
@Data
@ApiModel(value = "江西:营销策划管理请求实体", description = "江西:营销策划管理请求实体")
@ToString
public class SearchTacticsActionQueryJx extends SearchTacticsActionQuery {
    @ApiModelProperty(value = "创建方式: 1-手动, 2-导入")
    private String campCreateType;
    @ApiModelProperty(value = "事件ID")
    private String cepEventId;
    @ApiModelProperty(value = "策略类型: 0,全部策略 1 全网统一策略")
    private String unityCampsegId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "活动开始时间的开始", hidden = true)
    private String campStartDayStart;

    @ApiModelProperty(value = "活动开始时间的结束", hidden = true)
    private String campStartDayEnd;

    @ApiModelProperty(value = "活动结束时间的开始", hidden = true)
    private String campEndDayStart;

    @ApiModelProperty(value = "活动结束时间的结束", hidden = true)
    private String campEndDayEnd;

}

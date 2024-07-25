package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description: 策略统筹提交审批入参对象
 *
 * @author: lvchaochao
 * @date: 2023/7/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CoordinationSubmitProcessQuery extends SubmitProcessQuery {

    @ApiModelProperty("子业务流程ID 多个逗号隔开")
    private String childBusinessId;

}

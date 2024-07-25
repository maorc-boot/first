package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description: 江西客户通关怀短信模板查询入参对象
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "江西客户通关怀短信模板查询入参对象",description = "江西客户通关怀短信模板查询入参对象")
@Data
public class McdCareSmsTemplateQuery extends McdPageQuery {

    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    @ApiModelProperty(value = "创建人名")
    private String userName;

    @ApiModelProperty(value = "模板状态：0-未引用 1-已引用 2-已弃用")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}

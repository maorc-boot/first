package com.asiainfo.biapp.pec.element.jx.query;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : zhouyang
 * @date : 2021-11-25 11:24:46
 */
@Data
@ApiModel(value = "产品查询实体", description = "产品查询实体")
public class RequestPlanQuery extends McdPageQuery {


    @ApiModelProperty(value = "产品类别, 1流量,2新增,3终端,4两网,5宽带,6存量,7政企,10 手机应用,101个人基本策划,102普通增值策划,999虚拟类政策,9其他")
    private String planTypeId;


    @ApiModelProperty(value = "产品类型,1单产品,2政策,6综合")
    private String planSrvType;






}

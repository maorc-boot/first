package com.asiainfo.biapp.pec.plan.jx.cep.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/5/5
 */
@Data
@ApiModel(value = "事件查询请求实体", description = "事件查询请求实体")
public class SearchEventActionQuery extends McdPageQuery {
    /**
     * 事件分类
     */
    @ApiModelProperty("事件分类ID")
    private String classifyId;
    /**
     * 状态
     */
    @ApiModelProperty("状态 0：不可用 1：可用 ")
    private String status;

    /**
     * 当前登录用户ID
     */
    private String userId;


}

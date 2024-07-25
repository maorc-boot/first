package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author ranpf
 * @date 2023/4/19 14:38
 * @Description 接收前入参只有ID的请求
 */
@Data
@ApiModel(value = "江西根据根策略ID查询子策略信息入参")
public class McdSubCampByIdQuery {

    @NotEmpty(message = "传入id不能为空")
    @ApiModelProperty(value = "数据唯一标识",required = true)
    private String campsegRootId;

    @ApiModelProperty(value = "当前用户" )
    private String userId;

}

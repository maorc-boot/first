package com.asiainfo.biapp.pec.element.jx.material.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description : 素材分页(或根据条件)查询
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-9
 */

@Data
@ApiModel(value = "江西素材审批条件查询接口入参",description = "素材审批条件查询接口入参")
public class DimMaterialApproveJxQuery {


    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "素材类型: 0: 图片, 1: 文字, 2: 视频")
    private String materialType;

    @ApiModelProperty(value = "业务对象名称")
    private String materialName;

    @ApiModelProperty(value = "渠道")
    private String channelId;

    @ApiModelProperty(value = "创建人")
    private String creator;


}

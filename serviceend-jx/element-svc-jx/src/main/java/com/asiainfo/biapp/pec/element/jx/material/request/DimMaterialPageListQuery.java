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
 * @date : Created in 2023-1-3
 */

@Data
@ApiModel(value = "江西素材条件查询接口入参",description = "素材条件查询接口入参")
public class DimMaterialPageListQuery {

    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;


    @ApiModelProperty(value = "素材类型: 0: 图片, 1: 文字, 2: 视频")
    private String materialType;
    /**
     * 素材名称
     */
    @ApiModelProperty(value = "素材名称")
    private String materialName;

    /**
     * 产品名称或编号
     */
    @ApiModelProperty(value = "产品名称或编号")
    private String planIdOrName;

    /**
     * 素材状态
     */
    @ApiModelProperty(value = "素材状态")
    private String materialStatus;

    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "渠道")
    private String channelId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endDate;

}

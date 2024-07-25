package com.asiainfo.biapp.pec.element.jx.material.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description : 素材按条件导出
 * </p>
 *
 * @author : ranpf
 * @date : Created in 2023-1-4
 */

@Data
@ApiModel(value = "江西素材条件导出入参",description = "素材条件导出入参")
public class ExportMaterialInfoQuery {


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

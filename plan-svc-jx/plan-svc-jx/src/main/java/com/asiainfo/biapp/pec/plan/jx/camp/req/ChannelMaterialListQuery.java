package com.asiainfo.biapp.pec.plan.jx.camp.req;

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
@ApiModel(value = "江西标准化素材查询接口入参",description = "标准化素材查询接口入参")
public class ChannelMaterialListQuery {

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

    /**
     * 素材名称
     */
    @ApiModelProperty(value = "素材名称")
    private String materialName;

    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String planId;


    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "当前渠道ID")
    private String channelId;

    /**
     * 运营位ID
     */
    @ApiModelProperty(value = "当前运营位id")
    private String positionId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;

}

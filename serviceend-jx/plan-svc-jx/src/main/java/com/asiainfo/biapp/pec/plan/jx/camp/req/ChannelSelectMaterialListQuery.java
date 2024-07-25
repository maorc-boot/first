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
@ApiModel(value = "江西选择素材查询接口入参",description = "选择素材查询接口入参")
public class ChannelSelectMaterialListQuery {


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


}

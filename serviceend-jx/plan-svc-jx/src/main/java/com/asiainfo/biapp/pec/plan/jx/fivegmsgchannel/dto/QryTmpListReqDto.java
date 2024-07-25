package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description: 5G云卡查询模板列表入参对象
 *
 * @author: lvchaochao
 * @date: 2023/9/12
 */
@Data
@ApiModel("5G云卡查询模板列表入参对象")
public class QryTmpListReqDto {

    @ApiModelProperty("模板ID,可批量查询(最大20条)。不传时则查出apId和appId对应的所有模板")
    private List<String> templateIds;

    @ApiModelProperty("当前分页")
    private Integer page = 1;

    @ApiModelProperty("分页条数，默认一页十条")
    private Integer pageSize = 10;
}

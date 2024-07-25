package com.asiainfo.biapp.pec.plan.jx.channelconfig.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("渠道敏感客群配置")
public class McdsettingHttpVo {
    @ApiModelProperty("查询客群/查询渠道敏感客户群配置")
    private String pageSize;
    @ApiModelProperty("查询客群/查询渠道敏感客户群配置")
    private String pageNum;
    @ApiModelProperty("查询渠道敏感客户群配置/保存敏感配置/删除敏感客群配置, 渠道id")
    private String channelId;
    @ApiModelProperty("保存敏感配置/删除敏感客群配置,  客群id  多个用,分割 案例 1,2")
    private String khqList; //字符串数组 客群id
    @ApiModelProperty("查询客群, 模糊查询")
    private String keyWords;
}

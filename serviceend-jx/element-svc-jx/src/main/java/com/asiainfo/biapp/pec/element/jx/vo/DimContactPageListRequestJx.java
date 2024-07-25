package com.asiainfo.biapp.pec.element.jx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description : 触点(或根据条件)分页查询接口入参
 * </p>
 *
 * @author : wuhq6
 * @date : Created in 2021/11/25  15:50
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "触点(或根据条件)分页查询入参",description = "触点(或根据条件)分页查询入参")
public class DimContactPageListRequestJx {
    
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
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    private String keywords;
    
    /**
     * 日期范围,开始时间
     */
    @ApiModelProperty(value = "日期范围,开始时间(yyyy-MM-dd)")
    private String startDate;
    
    /**
     * 日期范围,结束时间
     */
    @ApiModelProperty(value = "日期范围,结束时间(yyyy-MM-dd)")
    private String endDate;
    
    /**
     * 触点来源
     */
    @ApiModelProperty(value = "来源: 0：省级触点信息 1：全网触点信息")
    private Integer contactSource;
    
    /**
     * 归属渠道
     */
    @ApiModelProperty(value = "渠道ID",dataType = "string")
    private String channelId;

    /**
     * 假的过滤 前端传参后端不做处理
     */
    @ApiModelProperty(value = "免打扰时段开始 如：0:00-6:00,22:00-24:00")
    private String contactAvoidTimeStart;

    /**
     * 假的过滤 前端传参后端不做处理
     */
    @ApiModelProperty(value = "免打扰时段结束 如：22:00-24:00")
    private String contactAvoidTimeEnd;
}

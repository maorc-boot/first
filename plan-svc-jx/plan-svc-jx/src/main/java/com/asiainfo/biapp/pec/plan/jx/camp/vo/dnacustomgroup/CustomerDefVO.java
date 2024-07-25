package com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DNA方返回客群信息实体
 *
 * @author lvcc
 * @date 2023/12/12
 */
@Data
@ApiModel(value = "DNA方返回客群信息实体", description = "DNA方返回客群信息实体")
public class CustomerDefVO {

    @ApiModelProperty(value = "客户群规模")
    private int count;

    @ApiModelProperty(value = "创建时间（unix时间戳）")
    private long createTime;

    @ApiModelProperty(value = "创建人ID")
    private String createUser;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "客户群类型")
    private String customerType;

    @ApiModelProperty(value = "客群描述")
    private String desc;

    @ApiModelProperty(value = "失效时间（unix时间戳）")
    private long failTime;

    @ApiModelProperty(value = "客户群ID")
    private String id;

    @ApiModelProperty(value = "客户群名称 ")
    private String name;

    @ApiModelProperty(value = "客户群状态（1-正常，2-未开始，3-已过期，4-已删除）")
    private int status;

    @ApiModelProperty(value = "更新周期")
    private Integer updateCycle;

    @ApiModelProperty(value = "最后更新时间（unix时间戳）")
    private long updateTime;

    @ApiModelProperty(value = "数据时间")
    private long dataTime;


}

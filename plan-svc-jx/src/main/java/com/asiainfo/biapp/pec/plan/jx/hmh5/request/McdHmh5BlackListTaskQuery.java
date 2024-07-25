package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("黑名单批量导入任务查询")
public class McdHmh5BlackListTaskQuery {

    @ApiModelProperty("任务编码")
    private String taskId;
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("开始时间:yyyyMMddHHmmss")
    private String startTime;
    @ApiModelProperty("结束时间:yyyyMMddHHmmss")
    private String endTime;
    @ApiModelProperty("任务状态: 0-草稿，1-审批中，2-审批驳回，3-导入中，4-导入完成")
    private Integer taskStatus;
    @ApiModelProperty("每页大小")
    private int pageSize = 10;
    @ApiModelProperty("当前页")
    private int currentPage =1 ;

}

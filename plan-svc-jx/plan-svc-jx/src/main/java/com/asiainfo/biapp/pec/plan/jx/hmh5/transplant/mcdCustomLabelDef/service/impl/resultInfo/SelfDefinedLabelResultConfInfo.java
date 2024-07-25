package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo
 * @className: SelfDefinedLabelResultInfo
 * @author: chenlin
 * @description: 客户通自定义标签详情查询结果
 * @date: 2023/6/26 15:58
 * @version: 1.0
 */
@Data
@ApiModel("客户通自定义标签详情查询结果")
public class SelfDefinedLabelResultConfInfo {

    @ApiModelProperty(value = "主键自增id，修改客群规则时传入")
    private Integer labelId;

    @ApiModelProperty(value = "客群编号")
    private String customLabelId;

    @ApiModelProperty(value = "客群名称")
    private String customLabelName;

    @ApiModelProperty(value = "创建者id")
    private String createUserId;

    @ApiModelProperty(value = "地市名称")
    private String cityName;

    @ApiModelProperty(value = "审批状态")
    private String approveStatus;

    @ApiModelProperty(value = "上线状态")
    private String onlineStatus;

    @ApiModelProperty(value = "所属模块")
    private String moduleName;

    @ApiModelProperty(value = "排序")
    private String sortNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "客群规则")
    private String customLabelDes;
}

package com.asiainfo.biapp.pec.approve.jx.controller.reqParam;

import com.asiainfo.biapp.pec.approve.dto.ApproverProcessDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.approve.jx.controller.reqParam
 * @className: ApproverProcessDTONew
 * @author: chenlin
 * @description: 审批流程状态查询缺少审批类型字段，故继承ApproverProcessDTONew，添加enumKey字段，用于区分审批类型
 * @date: 2023/7/10 16:38
 * @version: 1.0
 */
@Data
public class ApproverProcessDTONew extends ApproverProcessDTO {

    /**
     *  添加审批类型区分
     */
    //2023-07-10 16:40:25
    @ApiModelProperty("审批类型的枚举值，根据不同的枚举值查询不同的审批类型")
    private String enumKey;

}

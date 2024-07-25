package com.asiainfo.biapp.pec.approve.jx.dto;

import com.asiainfo.biapp.pec.approve.model.CmpApproveProcessRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description: 江西审批流程记录对象
 *
 * @author: lvchaochao
 * @date: 2023/8/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CmpApproveProcessJxRecord extends CmpApproveProcessRecord {

    @ApiModelProperty(value = "子业务流程ID 多个逗号隔开")
    private String childBusinessId;

    @ApiModelProperty(value = "渠道id")
    private String channelId;
}

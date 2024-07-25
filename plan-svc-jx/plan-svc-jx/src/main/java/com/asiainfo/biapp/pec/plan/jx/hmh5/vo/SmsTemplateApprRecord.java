package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import com.asiainfo.biapp.client.pec.approve.model.CmpApproveProcessRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * description: 短信模板审批列表实体
 *
 * @author: lvchaochao
 * @date: 2023/3/15
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "短信模板审批列表实体",description = "短信模板审批列表实体")
@Data
public class SmsTemplateApprRecord extends CmpApproveProcessRecord {

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("短信模板创建人ID")
    private String createUserId;

    @ApiModelProperty("短信模板创建人姓名")
    private String userName;

    @ApiModelProperty("短信模板创建时间")
    private Date createTime;

    @ApiModelProperty("类型")
    private String type;

}

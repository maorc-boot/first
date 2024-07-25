package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam
 * @className: SelfDefinedLabelParam
 * @author: chenlin
 * @description: 修改客户通自定义标签配置参数
 * @date: 2023/6/26 14:25
 * @version: 1.0
 */
@Data
@ApiModel("修改客户通自定义标签配置参数")
public class ModifySelfDefinedLabelConfParam {

    @ApiModelProperty(value = "首页-客户分布对应的labelId值，数组类型", example = "[1]")
    @NotNull(message = "customerDistributes属性不可为空！")
    private List<String> customerDistributes;

    @ApiModelProperty(value = "关怀短信对应的labelId值，数组类型", example = "[1,2]")
    @NotNull(message = "careMessages属性不可为空！")
    private List<String> careMessages;

    @ApiModelProperty(value = "重点营销对应的labelId值，数组类型", example = "[1,2,3]")
    @NotNull(message = "majorMarketings属性不可为空！")
    private List<String> majorMarketings;

    @ApiModelProperty(value = "代维短信对应的labelId值，数组类型", example = "[1,2,3]")
    @NotNull(message = "daiweiMessages属性不可为空！")
    private List<String> daiweiMessages;

    public void setCustomerDistributes(List<String> customerDistributes) {
        if (customerDistributes.stream().distinct().count() != customerDistributes.size())
            throw new BaseException("不可包含重复的labelId值");
        this.customerDistributes = customerDistributes;
    }

    public void setCareMessages(List<String> careMessages) {
        if (careMessages.stream().distinct().count() != careMessages.size())
            throw new BaseException("不可包含重复的labelId值");
        this.careMessages = careMessages;
    }

    public void setMajorMarketings(List<String> majorMarketings) {
        if (majorMarketings.stream().distinct().count() != majorMarketings.size())
            throw new BaseException("不可包含重复的labelId值");
        this.majorMarketings = majorMarketings;
    }
}

package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam
 * @className: SelfDefinedLabelParam
 * @author: chenlin
 * @description: 查询客户通自定义标签参数
 * @date: 2023/6/26 14:25
 * @version: 1.0
 */
@Data
@ApiModel("查询客户通自定义标签参数")
public class SelectSelfDefinedLabelParam {

    @ApiModelProperty(value = "查询当前页，默认1", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "查询页大小，默认10", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "标签编号，默认null查询全部")
    @Pattern(regexp = "\\S+", message = "标签编号不能含有空字符！")
    private String customLabelCode; //值可以为null，但不可为空字符串或者空白字符串

    @ApiModelProperty(value = "标签名称，默认null查询全部")
    @Pattern(regexp = "\\S+", message = "标签名称不能含有空字符！")
    private String customLabelName;

    @ApiModelProperty(value = "地市ID")
    @Pattern(regexp = "\\w{3,}", message = "地市ID只能包含数字！")
    private String cityId;

    @ApiModelProperty(value = "要查询的开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeStart;

    @ApiModelProperty(value = "要查询的结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeEnd;

    @ApiModelProperty(value = "审批状态：50 待提审； 51 审核中；52 通过；53 未通过", example = "52")
    @Pattern(regexp = "(50|51|52|53)", message = "不合法的参数类别！")
    private String approveStatus; //数据表中此字段类型为int

    @ApiModelProperty(value = "上下线状态：0.下线 1.上线", example = "1")
    @Pattern(regexp = "(0|1)", message = "不合法的参数类别！")
    private String isOnline;

    @ApiModelProperty(value = "模块ID： 1.客户分布； 2.关怀短信；3.重点营销", example = "1")
    @Pattern(regexp = "(1|2|3)", message = "不合法的参数类别！")
    private String moduleId;

    @ApiModelProperty(value = "类型id", example = "0")
    private String typeId = "0";


    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        //取到查询日期的最后一秒
        this.createTimeEnd = new Date(createTimeEnd.getTime() + (24 * 60 * 59 * 1000));
    }
}

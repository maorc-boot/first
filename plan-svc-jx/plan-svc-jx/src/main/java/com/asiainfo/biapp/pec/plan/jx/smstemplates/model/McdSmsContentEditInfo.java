package com.asiainfo.biapp.pec.plan.jx.smstemplates.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiOperation("推荐用语编辑信息")
@Data
@TableName("mcd_market_terms_template")
@ApiModel(value = "推荐用语编辑信息" ,description = "推荐用语编辑信息")
public class McdSmsContentEditInfo extends Model<McdSmsContentEditInfo> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键,模板编码")
    @TableId("ID")
    private String id;
    @ApiModelProperty("产品编码")
    @TableField("PLAN_ID")
    private String planId;
    @ApiModelProperty("渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;
    @ApiModelProperty("地市")
    @TableField("CITY_ID")
    private String cityId;
    @ApiModelProperty("名称")
    @TableField("NAME")
    private String name;
    @ApiModelProperty("模板内容")
    @TableField("CONTENT")
    private String content;
    @ApiModelProperty("模板描述")
    @TableField("DESCRIPTION")
    private String description;
    @ApiModelProperty("创建人")
    @TableField("CREATE_USER")
    private String createUser;
    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;
    @ApiModelProperty("营销用语类型")
    @TableField("TYPE")
    private int type;
    @ApiModelProperty("审批结果")
    @TableField("APPROVE_RESULT")
    private int approveResult;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

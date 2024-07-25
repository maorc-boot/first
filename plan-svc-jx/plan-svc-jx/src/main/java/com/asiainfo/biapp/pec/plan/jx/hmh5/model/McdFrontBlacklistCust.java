package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "mcd_front_blacklist_cust")
@ApiModel(value = "江西客户通黑名单数据对象信息",description = "江西客户通黑名单数据对象信息")
public class McdFrontBlacklistCust extends Model<McdFrontBlacklistCust> {

    @ApiModelProperty(value = "主键")
    @TableId(value = "PRODUCT_NO")
    private String procuctNo;
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;
    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY")
    private String createBy;
    @ApiModelProperty(value = "原因编号")
    @TableField(value = "REASON_CODE")
    private String reasonCode;
    @ApiModelProperty(value = "数据状态 1正常,0失效")
    @TableField(value = "DATA_STATE")
    private int dataState;
    @ApiModelProperty(value = "截止时间")
    @TableField(value = "EFFECT_END_TIME")
    private Date effectEndTime;
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;
    @ApiModelProperty(value = "备注")
    @TableField(value = "REMARKS")
    private String remarks;
    @ApiModelProperty(value = "加入类型（1:黑名单,2:退订）")
    @TableField(value = "JOIN_TYPE")
    private int joinType;
    @ApiModelProperty(value = "地市编码")
    @TableField(value = "CITY_ID")
    private String cityId;
    @ApiModelProperty(value = "扩展字段1")
    @TableField(value = "EXT1")
    private String ext1;
    @ApiModelProperty(value = "扩展字段2")
    @TableField(value = "EXT2")
    private String ext2;
    @ApiModelProperty(value = "扩展字段3")
    @TableField(value = "EXT3")
    private String ext3;


}

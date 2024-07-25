package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ranpf
 * @date 2023/2/17
 * @description 主套餐配置实体类
 */
@Data
@TableName("mcd_main_offer_info")
@ApiModel("江西客户通主套餐配置对象")
public class McdFrontMainOfferInfoModel extends Model<McdFrontMainOfferInfoModel> {
    // 主套餐编码
    @ApiModelProperty("主套餐编码")
    @TableId("OFFER_ID")
    private String offerId;

    // 套餐实例ID
    @ApiModelProperty("套餐实例ID")
    @TableField("INS_OFFER_ID")
    private String insOfferId;

    // 主套餐名称
    @ApiModelProperty("主套餐名称")
    @TableField("OFFER_NAME")
    private String offerName;

    // 套餐描述
    @ApiModelProperty("套餐描述")
    @TableField("OFFER_DESC")
    private String offerDesc;

    // 备注
    @ApiModelProperty("备注")
    @TableField("OFFER_REMARKS")
    private String offerRemarks;

    // 品牌ID
    @ApiModelProperty("品牌ID")
    @TableField("BRAND_ID")
    private String brandId;

    // 品牌名称
    @ApiModelProperty("品牌名称")
    @TableField("BRAND_NAME")
    private String brandName;

    // 生效时间
    @ApiModelProperty("生效时间")
    @TableField("EFFECT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date effectTime;

    // 失效时间
    @ApiModelProperty("失效时间")
    @TableField("INVALID_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date invalidTime;

    // 状态（1：上线，:0：下线）
    @ApiModelProperty("状态（1：上线，:0：下线")
    @TableField("STATE")
    private int state;

    // 创建人ID（后台默认保存）
    @ApiModelProperty("创建人ID")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty("创建人名称")
    @TableField("CREATE_USER_NAME")
    private String createUserName;

    // 创建时间
    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    // 修改时间（后台默认保存）
    @ApiModelProperty("修改时间")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    // 删除状态（0否，1是）
    @ApiModelProperty("删除状态")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    // 地市id
    @ApiModelProperty("地市id")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty("地市名称")
    @TableField("CITY_NAME")
    private String cityName;

}

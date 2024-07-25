package com.asiainfo.biapp.pec.preview.jx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author mamp
 * @date 2022/6/29
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MCD_CHN_PRE_USER_NUM")
@ApiModel(value = "McdChnPreUserNum对象", description = "客户群渠道偏好用户数统计表")
public class McdChnPreUserNum extends Model<McdChnPreUserNum> {

    @ApiModelProperty(value = "客户群ID")
    @TableField("CUSTGROUP_ID")
    private String custgroupId;

    @ApiModelProperty(value = "数据日期")
    @TableField("DATA_DATE")
    private String dataDate;

    @ApiModelProperty(value = "清单文件名")
    @TableField("CUST_FILE_NAME")
    private String custFileNme;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "渠道ID")
    @TableField("USER_NUM")
    private Integer userNum;

    @ApiModelProperty(value = "占比")
    @TableField("USER_RATIO")
    private Double userRatio;

    @ApiModelProperty(value = "偏好级别")
    @TableField("PRE_LEVEL")
    private Integer preLevel;

    @ApiModelProperty(value = "渠道名称")
    @TableField(exist = false)
    private String channelName;

}

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
@TableName(value = "MCD_CUSTOMIZE_TITLE_CONFIG")
@ApiModel(value = "江西客户通-客户个性化称谓信息",description = "江西客户通-客户个性化称谓信息")
public class McdCustTitleList extends Model<McdCustTitleList> {

    @ApiModelProperty(value = "主键-号码")
    @TableId(value = "PHONE_NUM")
    private String phoneNum;

    @ApiModelProperty(value = "个性化称谓")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "地市编码")
    @TableField(value = "CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "地市名称")
    @TableField(value = "CITY_NAME")
    private String cityName;

    @ApiModelProperty(value = "数据状态 1:正常，2：异常")
    @TableField(value = "DATA_STATUS")
    private int dataStatus;

    @ApiModelProperty(value = "上传时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "上传人ID")
    @TableField(value = "CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty(value = "上传人名称")
    @TableField(value = "CREATE_USER_NAME")
    private String createUserName;

    @ApiModelProperty(value = "备注：异常状态提示信息，如：手机号位数不符合、地市错误、称谓超长、无法识别的特殊字符等")
    @TableField(value = "REMARKS")
    private String remarks;

}

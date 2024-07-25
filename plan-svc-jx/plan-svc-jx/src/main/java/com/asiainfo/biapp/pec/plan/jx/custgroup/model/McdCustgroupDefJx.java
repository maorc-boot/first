package com.asiainfo.biapp.pec.plan.jx.custgroup.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author imcd
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_custgroup_def")
@ApiModel(value="McdCustgroupDef对象", description="")
public class McdCustgroupDefJx extends Model<McdCustgroupDefJx> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户群ID")
    @TableId("CUSTOM_GROUP_ID")
    private String customGroupId;

    @ApiModelProperty(value = "客户群名称")
    @TableField("CUSTOM_GROUP_NAME")
    private String customGroupName;

    @ApiModelProperty(value = "客户群描述")
    @TableField("CUSTOM_GROUP_DESC")
    private String customGroupDesc;

    @ApiModelProperty(value = "客户群数量")
    @TableField("CUSTOM_NUM")
    private Integer customNum;

    @ApiModelProperty(value = "客户群实际数量")
    @TableField("ACTUAL_CUSTOM_NUM")
    private Integer actualCustomNum;

    @ApiModelProperty(value = "客户群状态0：无效；1：有效；2：删除；3：提取处理中；4：提取失败；9：客户群导入失败；10：入库时异常；")
    @TableField("CUSTOM_STATUS_ID")
    private Integer customStatusId;

    @ApiModelProperty(value = "数据来源1:coc 2:dna 3:多波次")
    @TableField("CUSTOM_SOURCE_ID")
    private String customSourceId;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty(value = "推送用户ID")
    @TableField("PUSH_TARGET_USER")
    private String pushTargetUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "生效时间")
    @TableField("EFFECTIVE_TIME")
    private Date effectiveTime;

    @ApiModelProperty(value = "失效时间")
    @TableField("FAIL_TIME")
    private Date failTime;

    @ApiModelProperty(value = "客户群周期1：一次性，2：月周期，3：日周期")
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    @ApiModelProperty(value = "客户群表名")
    @TableField("LIST_TABLE_NAME")
    private String listTableName;

    @ApiModelProperty(value = "数据日期")
    @TableField("DATA_DATE")
    private Integer dataDate;

    @ApiModelProperty(value = "规则描述")
    @TableField("RULE_DESC")
    private String ruleDesc;

    @ApiModelProperty(value = "同步次数")
    @TableField("SYNC_TIMES")
    private Integer syncTimes;

    @ApiModelProperty(value = "客户群类型,状态 0:普通; 1:外呼专用客户群; 2:分箱客户群; 3:政企客户群; 4:网格系统客户群; 5:事件累计客户群")
    @TableField("CUST_TYPE")
    private Integer custType;

    @ApiModelProperty(value = "是否推送其他")
    @TableField("IS_PUSH_OTHER")
    private Integer isPushOther;

    @ApiModelProperty(value = "清单文件名")
    @TableField("FILE_NAME")
    private String fileName;

    @ApiModelProperty(value = "客群更新状态，0/1：不更新/更新")
    @TableField("UPDATE_STATUS")
    private Integer updateStatus;

    @ApiModelProperty(value = "是否同步coc客群清单至dna 0-否 1-是")
    @TableField("SYNC_DNA")
    private Integer syncDna;

    @ApiModelProperty(value = "DNA返回的客户群ID")
    @TableField("COLUMN_NUM")
    private String columnNum;


    @Override
    protected Serializable pkVal() {
        return this.customGroupId;
    }

}

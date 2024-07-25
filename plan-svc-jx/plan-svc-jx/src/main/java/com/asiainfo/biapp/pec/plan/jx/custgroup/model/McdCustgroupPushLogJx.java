package com.asiainfo.biapp.pec.plan.jx.custgroup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ranpf
 * @since 2023-2-7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "mcd_custgroup_push_log")
@ApiModel(value="jxMcdCustgroupPushLog对象", description="江西客群推送日志记录表")
public class McdCustgroupPushLogJx implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户群ID")
    @TableField("CUSTOM_GROUP_ID")
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

    @ApiModelProperty(value = "数据来源")
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

    @ApiModelProperty(value = "生效时间")
    @TableField("EFFECTIVE_TIME")
    private Date effectiveTime;

    @ApiModelProperty(value = "失效时间")
    @TableField("FAIL_TIME")
    private Date failTime;

    @ApiModelProperty(value = "客户群周期1：一次性，2：月周期，3：日周期")
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    @ApiModelProperty(value = "数据日期")
    @TableField("DATA_DATE")
    private Integer dataDate;

    @ApiModelProperty(value = "推送报文")
    @TableField("XML_DESC")
    private String xmlDesc;

    @ApiModelProperty(value = "同步次数")
    @TableField("SYNC_TIMES")
    private Integer syncTimes;

    @ApiModelProperty(value = "客户群类型,状态 0:普通; 1:外呼专用客户群; 2:分箱客户群; 3:政企客户群;")
    @TableField("CUST_TYPE")
    private Integer custType;

    @ApiModelProperty(value = "是否推送其他")
    @TableField("IS_PUSH_OTHER")
    private Integer isPushOther;

    @ApiModelProperty(value = "客群清单文件")
    @TableField("FILE_NAME")
    private String fileName;

    @ApiModelProperty(value = "日志时间")
    @TableField("LOG_TIME")
    private Date logTime;


}

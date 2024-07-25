package com.asiainfo.biapp.pec.plan.jx.enterprise.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "mcd_cust_group_push_log")
public class McdCustgroupPushLog {


  @TableId(value = "ID",type = IdType.AUTO)
  private long id;

  @ApiModelProperty(value = "客户群ID")
  @TableField("CUSTOM_GROUP_NAME")
  private String customGroupId;

  @ApiModelProperty(value = "客户群名称")
  @TableField("CUSTOM_GROUP_NAME")
  private String customGroupName;

  @ApiModelProperty(value = "客户群描述")
  @TableField("CUSTOM_GROUP_DESC")
  private String customGroupDesc;

  @ApiModelProperty(value = "客户群数量")
  @TableField("CUSTOM_NUM")
  private long customNum;

  @ApiModelProperty(value = "客户群实际数量")
  @TableField("ACTUAL_CUSTOM_NUM")
  private long actualCustomNum;

  @ApiModelProperty(value = "客户群状态0：无效；1：有效；2：删除；3：提取处理中；4：提取失败；9：客户群导入失败；10：入库时异常；")
  @TableField("CUSTOM_STATUS_ID")
  private long customStatusId;

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
  private java.sql.Timestamp createTime;

  @ApiModelProperty(value = "生效时间")
  @TableField("EFFECTIVE_TIME")
  private java.sql.Timestamp effectiveTime;

  @ApiModelProperty(value = "失效时间")
  @TableField("FAIL_TIME")
  private java.sql.Timestamp failTime;

  @ApiModelProperty(value = "客户群周期1：一次性，2：月周期，3：日周期")
  @TableField("UPDATE_CYCLE")
  private long updateCycle;

  @ApiModelProperty(value = "数据日期")
  @TableField("DATA_DATE")
  private long dataDate;

  @ApiModelProperty(value = "推送报文")
  @TableField("XML_DESC")
  private String xmlDesc;

  @ApiModelProperty(value = "同步次数")
  @TableField("SYNC_TIMES")
  private long syncTimes;

  @ApiModelProperty(value = "是否推送其他")
  @TableField("IS_PUSH_OTHER")
  private long isPushOther;

  @ApiModelProperty(value = "客群清单文件")
  @TableField("FILE_NAME")
  private String fileName;

  @ApiModelProperty(value = "客户群类型,状态 0:普通; 1:外呼专用客户群; 2:分箱客户群; 3:政企客户群; 4:多波次客户群;")
  @TableField("CUST_TYPE")
  private long custType;

  @ApiModelProperty(value = "清单表名")
  @TableField("LIST_TABLE_NAME")
  private String listTableName;

  @ApiModelProperty(value = "同步gbase状态 1：已同步 2已清理 3：正在同步")
  @TableField("SYNC_STATUS")
  private long syncStatus;

  @ApiModelProperty(value = "渠道偏好数据生成状态1:已经生成,0:未生成")
  @TableField("CHN_PRE_STATUS")
  private long chnPreStatus;

  @ApiModelProperty(value = "日志记录时间")
  @TableField("LOG_TIME")
  private java.sql.Timestamp logTime;



}

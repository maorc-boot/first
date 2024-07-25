package com.asiainfo.biapp.pec.preview.jx.custgroup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mamp
 * @since 2022-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdCustgroupDef implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 客户群ID
     */
    @TableId(value = "CUSTOM_GROUP_ID", type = IdType.ID_WORKER_STR)
    private String customGroupId;

    /**
     * 客户群名称
     */
    @TableField("CUSTOM_GROUP_NAME")
    private String customGroupName;

    /**
     * 客户群描述
     */
    @TableField("CUSTOM_GROUP_DESC")
    private String customGroupDesc;

    /**
     * 客户群数量
     */
    @TableField("CUSTOM_NUM")
    private Integer customNum;

    /**
     * 客户群实际数量
     */
    @TableField("ACTUAL_CUSTOM_NUM")
    private Integer actualCustomNum;

    /**
     * 客户群状态0：无效；1：有效；2：删除；3：提取处理中；4：提取失败；9：客户群导入失败；10：入库时异常；
     */
    @TableField("CUSTOM_STATUS_ID")
    private Integer customStatusId;

    /**
     * 数据来源1:coc 2:DNA 3:多波次
     */
    @TableField("CUSTOM_SOURCE_ID")
    private String customSourceId;

    /**
     * 创建人ID
     */
    @TableField("CREATE_USER_ID")
    private String createUserId;

    /**
     * 推送用户ID
     */
    @TableField("PUSH_TARGET_USER")
    private String pushTargetUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @TableField("FAIL_TIME")
    private Date failTime;

    /**
     * 客户群周期1：一次性，2：月周期，3：日周期
     */
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    /**
     * 客户群表名:此字段作为同步gbase标识，同步完成之后才有值
     */
    @TableField("LIST_TABLE_NAME")
    private String listTableName;

    /**
     * 数据日期
     */
    @TableField("DATA_DATE")
    private Integer dataDate;

    /**
     * 规则描述
     */
    @TableField("RULE_DESC")
    private String ruleDesc;

    /**
     * 同步次数
     */
    @TableField("SYNC_TIMES")
    private Integer syncTimes;

    /**
     * 是否推送其他
     */
    @TableField("IS_PUSH_OTHER")
    private Integer isPushOther;

    /**
     * 客群清单文件
     */
    @TableField("FILE_NAME")
    private String fileName;

    /**
     * 客户群类型,状态 0:普通; 1:外呼专用客户群; 2:分箱客户群; 3:政企客户群; 4:网格系统客户群
     */
    @TableField("CUST_TYPE")
    private Integer custType;


}

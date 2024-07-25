package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户通黑名单批量导入任务
 * </p>
 *
 * @author mamp
 * @since 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MCD_HMH5_BLACKLIST_TASK")
public class McdHmh5BlacklistTask implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 任务编码
     */
    @TableId("TASK_ID")
    private String taskId;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 黑名单文件名称
     */
    @TableField("TASK_FILE_NAME")
    private String taskFileName;

    /**
     * 黑名单用户数量
     */
    @TableField("USER_NUM")
    private Integer userNum;

    /**
     * 创建人ID
     */
    @TableField("CREATE_USER_ID")
    private String createUserId;

    /**
     * 创建人姓名
     */
    @TableField("CREATE_USER_NAME")
    private String createUserName;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 任务状态. 0-草稿，1-审批中，2-审批驳回，3-导入中，4-导入完成 5-审批完成 6-导入失败
     */
    @TableField("TASK_STATUS")
    private Integer taskStatus;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 审批实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;

    /**
     * 当前审批人
     */
    @TableField("APPROVER_NAME")
    private String approverName;


}

package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 批量审批任务
 * </p>
 *
 * @author mamp
 * @since 2023-11-07
 */
@Data
@TableName(value = "mcd_batch_approve_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdBatchApproveTask implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("batch_id")
    private String batchId;

    /**
     * 批量任务记录数量
     */
    @TableField("batch_size")
    private Integer batchSize;

    /**
     * 审批意见
     */
    @TableField("deal_opinion")
    private String dealOpinion;

    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;

    @TableField("create_time")
    private Date createTime;

    /**
     * 0-待执行,1-执行中，2-执行完成, 3-执行完成,但有失败
     */
    @TableField("statuas")
    private Integer statuas;

    /**
     * 执行结果
     */
    @TableField("result")
    private String result;


}

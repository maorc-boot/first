package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 标签定义表
 * </p>
 *
 * @author chenlin
 * @since 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class McdCustomLabelDef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键ID
     */
    // @TableId(value = "LABEL_ID", type = IdType.AUTO)
    // private Integer labelId;

    /**
     * 标签编号，最多32个字符，原字段CUSTOM_LABEL_ID
     */
    @TableId("CUSTOM_LABEL_ID")
    private String customLabelId;

    /**
     * 标签名称，最多32个字符
     */
    @TableField("CUSTOM_LABEL_NAME")
    private String customLabelName;

    /**
     * 标签描述，最多128个字符，但页面修改客群规则时最多20个字符
     */
    @TableField("CUSTOM_LABEL_DESC")
    private String customLabelDesc;

    /**
     * 创建人ID，最多32个字符
     */
    @TableField("CREATE_USER_ID")
    private String createUserId;

    /**
     * 新增字段，用于存储创建人姓名，最多32个字符
     */
    // @TableField("CREATE_USERNAME")
    // private String createUsername;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 用户人数
     */
    @TableField("CUSTOM_NUM")
    private Integer customNum;

    /**
     * 实际用户人数
     */
    @TableField("ACTUAL_CUSTOM_NUM")
    private Integer actualCustomNum;

    /**
     * 更新周期
     */
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    /**
     * 推送文档名，最多128个字符，原字段1024字符
     */
    @TableField("COC_FILE_NAME")
    private String cocFileName;

    /**
     * 用户所在地市ID
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 数据状态：0：无效；1：有效；2：删除；3：提取处理中；9：客户群导入失败；
     */
    @TableField("DATA_STATUS")
    private Integer dataStatus;

    /**
     * 审批状态：50 待提审； 51 审核中；52 通过；53 未通过
     */
    @TableField("APPROVE_STATUS")
    private Integer approveStatus;


    /**
     * 2023-07-10，新增字段，用于记录审批流程ID
     */
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;


}

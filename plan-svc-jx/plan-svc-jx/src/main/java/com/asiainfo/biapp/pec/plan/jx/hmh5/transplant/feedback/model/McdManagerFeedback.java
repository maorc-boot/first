package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class McdManagerFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键，自增
     */
    // @TableId(value = "id", type = IdType.AUTO)
    // private Integer id;

    /**
     * 业务id，（最长32位字符）
     */
    @TableId("BUSINESS_ID")
    private String businessId;

    /**
     * 问题反馈者id，（最长32位字符）
     */
    @TableField("MANAGER_ID")
    private String managerId;

    /**
     * 反馈类型（暂不知道有哪些类型，已由字符串改为int)
     */
    @TableField("FEEDBACK_TYPE")
    private String feedbackType;

    /**
     * 反馈描述（最长1024个字符）
     */
    @TableField("FEEDBACK_DESCRIPTION")
    private String feedbackDescription;

    /**
     * 附加图片信息（图片文件名称长度累计不超过1024个字符）
     */
    @TableField("PICTIRE_INFO")
    private String pictireInfo;

    /**
     * 反馈日期
     */
    @TableField("CREATE_TIME")
    private Date createTime;


}

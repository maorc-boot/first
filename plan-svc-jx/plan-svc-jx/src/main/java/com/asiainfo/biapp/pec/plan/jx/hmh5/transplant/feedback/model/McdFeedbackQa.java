package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.feedback.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class McdFeedbackQa implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键，自增
     */
    // @TableId(value = "id", type = IdType.AUTO)
    // private Integer id;

    /**
     * 流水号（没看明白有何用意，长度已改为19个字符）
     */
    @TableField("SERIAL_NUMBER")
    private String serialNumber;

    /**
     * 业务ID（最长字符32字符）
     */
    @TableField("BUSINESS_ID")
    private String businessId;

    /**
     * 消息内容（最长1024字符）
     */
    @TableField("QA_CONTENT")
    private String qaContent;

    /**
     * 消息类型（0：文字回复 1：图片回复，已由字符串改为int）
     */
    @TableField("QA_MSG_TYPE")
    private Integer qaMsgType;

    /**
     * 消息状态：0 未读，1已读
     */
    @TableField("STATE")
    private Integer state;

    /**
     * 创建者（名字或者id？？，字符限制32位）
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建者名字
     */
    // @TableField("CREATE_USERNAME")
    // private String createUsername;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;


}

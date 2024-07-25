package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 江西客户通公告栏表
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class McdBulletinBoard implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 公告编号，最多64字符
     */
    @TableId("BULLETIN_CODE")
    private String bulletinCode;

    /**
     * 公告标题，最多100字符
     */
    @TableField("BULLETIN_TITLE")
    private String bulletinTitle;

    /**
     * 文字内容，最多2500字符
     */
    @TableField("BULLETIN_CONTENT")
    private String bulletinContent;

    /**
     * 图片内容，文件名长度累计不超过256字符
     */
    @TableField("BULLETIN_IMAGE")
    private String bulletinImage;

    /**
     * 展示规则，最多32字符
     */
    @TableField("BULLETIN_RULE")
    private String bulletinRule;

    /**
     * 创建人ID，（错误，实际存入的是名字，且长度不超过32字符）
     */
    @TableField("CREATE_USERID")
    private String createUserid;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 状态 0-已上线 1-已下线 2-已失效
     */
    @TableField("STATUS")
    private Integer status;


}

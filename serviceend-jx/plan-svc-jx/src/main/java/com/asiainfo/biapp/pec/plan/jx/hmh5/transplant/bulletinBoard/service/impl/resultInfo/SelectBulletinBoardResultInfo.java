package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.service.impl.resultInfo;

import lombok.Data;

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
public class SelectBulletinBoardResultInfo implements Serializable {
    /**
     * 新增主键，实现自增
     */
    
    private Integer bulletinId;

    /**
     * 公告编号，最多64字符
     */
    
    private String bulletinCode;

    /**
     * 公告标题，最多100字符
     */
    
    private String bulletinTitle;

    /**
     * 文字内容，最多2500字符
     */
    
    private String bulletinContent;

    /**
     * 图片内容，文件名长度累计不超过256字符
     */
    
    private String bulletinImage;

    /**
     * 展示规则，最多32字符
     */
    
    private String bulletinRule;

    /**
     * 创建人ID，（错误，实际存入的是名字，且长度不超过32字符）
     */
    
    private String createUserid;

    /**
     * 新增字段，创建人姓名，（一旦创建了公告，姓名应当不随用户姓名的改变而改变）
     */
    
    private String createUsername;

    /**
     * 新增字段，用于保存创建人id，或许无用
     */
    private Integer createUserId2;

    /**
     * 创建时间
     */
    
    private Date createTime;

    /**
     * 开始时间
     */
    
    private Date startTime;

    /**
     * 结束时间
     */
    
    private Date endTime;

    /**
     * 状态 0-已上线 1-已下线 2-已失效
     */
    
    private Integer status;


}

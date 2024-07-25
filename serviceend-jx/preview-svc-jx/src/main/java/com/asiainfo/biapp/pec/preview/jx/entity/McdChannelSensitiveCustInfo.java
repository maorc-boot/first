package com.asiainfo.biapp.pec.preview.jx.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 渠道敏感客户群配置表
 * </p>
 *
 * @author mamp
 * @since 2022-10-12
 */
@Data
public class McdChannelSensitiveCustInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 渠道ID
     */
    private String channelId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 客户群ID
     */
    private String custgroupId;

    /**
     * 客户群名称
     */
    private String custgroupName;
    /**
     * 客户群描述
     */
    private String custgroupDesc;

    /**
     * 客户群创建人ID
     */
    private String createUserId;
    /**
     * 客户群创建人
     */
    private String createUserName;

    /**
     * 客户群创建时间
     */
    private Date createTime;

    /**
     * 客户群用户数量
     */

    private Integer custNum;

    /**
     * 客户群类型
     */
    private String custgroupType;

}

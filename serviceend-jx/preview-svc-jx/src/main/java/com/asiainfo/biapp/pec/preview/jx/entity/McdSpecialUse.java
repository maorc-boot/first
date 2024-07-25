package com.asiainfo.biapp.pec.preview.jx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 特殊客户群表
 * </p>
 *
 * @author mamp
 * @since 2022-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdSpecialUse implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 渠道id
     */
    private String channelId;

    /**
     * 特殊客户群类型
     */
    private Integer custType;

    /**
     * 手机号码
     */
    private String productNo;

    /**
     * 描述
     */
    private String desc;

    /**
     * 入库时间
     */
    private Date enterTime;

    /**
     * 创建用户id
     */
    private String createUserId;

    /**
     * 数据源 1:手工导入 2:系统导入
     */
    private Integer dataSource;

    /**
     * 生效时间
     */
    private Date startTime;

    /**
     * 失效时间
     */
    private Date endTime;


}

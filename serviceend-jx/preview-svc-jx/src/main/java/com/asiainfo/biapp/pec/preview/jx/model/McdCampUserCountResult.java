package com.asiainfo.biapp.pec.preview.jx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IOP策略用户统计结果表
 * @TableName mcd_camp_user_count_result
 */
@TableName(value ="mcd_camp_user_count_result")
@Data
@Accessors(chain = true)
public class McdCampUserCountResult implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据日期:yyyyMMdd
     */
    private String dataDate;

    /**
     * 渠道ID
     */
    private String channelId;

    /**
     * 覆盖用户级别, 1~n
     */
    private Integer coverUserRank;

    /**
     * 当前覆盖级别用户数量
     */
    private Integer userNum;

    /**
     * 中高端用户数量
     */
    private Integer highEndUserNum ;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
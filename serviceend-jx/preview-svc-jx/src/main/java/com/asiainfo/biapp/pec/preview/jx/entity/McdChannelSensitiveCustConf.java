package com.asiainfo.biapp.pec.preview.jx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 渠道敏感客户群配置表
 * </p>
 *
 * @author mamp
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdChannelSensitiveCustConf implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "渠道ID")
    private String channelId;

    /**
     * 客户群ID
     */
    @ApiModelProperty(value = "客户群ID")
    private String custgroupId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private String createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新用户ID
     */
    @ApiModelProperty(value = "更新用户ID")
    private String updateUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 是否删除: 0-未删除，1-已删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

    /**
     * 客户群类型
     */
    @ApiModelProperty(value = "敏感客群类型:1黑名单、2红名单、3主动要求屏蔽客户、4敏感投诉客户")
    private String custgroupType;

    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;


}

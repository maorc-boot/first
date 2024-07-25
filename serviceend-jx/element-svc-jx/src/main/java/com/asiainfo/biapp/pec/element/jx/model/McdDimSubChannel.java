package com.asiainfo.biapp.pec.element.jx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 子渠道定义
 * </p>
 *
 * @author mamp
 * @since 2022-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdDimSubChannel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 渠道ID
     */
    @TableId(value = "CHANNEL_ID", type = IdType.ID_WORKER_STR)
    private String channelId;

    /**
     * 渠道名称
     */
    @TableField("CHANNEL_NAME")
    private String channelName;

    /**
     * 渠道编码
     */
    @TableField("CHANNEL_CODE")
    private String channelCode;

    /**
     * 父渠道ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 显示序号
     */
    @TableField("DISPLAY_ORDER")
    private Integer displayOrder;


}

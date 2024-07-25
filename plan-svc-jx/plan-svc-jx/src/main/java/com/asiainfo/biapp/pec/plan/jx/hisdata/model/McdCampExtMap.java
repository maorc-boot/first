package com.asiainfo.biapp.pec.plan.jx.hisdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 7.0扩展字段与4.0字段映射关系
 * </p>
 *
 * @author mamp
 * @since 2023-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdCampExtMap implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 渠道ID
     */
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 7.0字段
     */
    private String ext7;

    /**
     * 4.0字段
     */
    private String ext4;


}

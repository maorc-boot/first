package com.asiainfo.biapp.pec.element.jx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * <p>
 * 渠道说明表
 * </p>
 *
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_dim_channel_desc")
public class McdDimChannelDesc implements Serializable {

    private static final long serialVersionUID = 1L;

    private String channelId;

    private String channelName;

    private String managerId;

    private String managerName;

    private String operateScene;

    private String approveProcessImgPath;

    private String demoImgPath;



}

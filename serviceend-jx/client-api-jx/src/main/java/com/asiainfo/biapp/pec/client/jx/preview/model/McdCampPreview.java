package com.asiainfo.biapp.pec.client.jx.preview.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 营销活动预演
 * </p>
 *
 * @author mamp
 * @since 2022-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdCampPreview implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 批次ID
     */
    @TableId(value = "ID", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 策略ID
     */
    @TableField("CAMPSEG_ID")
    private String campsegId;

    /**
     * 策略ID
     */
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;

    /**
     * 渠道ID
     */
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 预演状态  0：未预演，1：预演中，2：预演完成，3：预演中止
     */
    @TableField("PREVIEW_STATUS")
    private Integer previewStatus;

    /**
     * 客户群ID
     */
    @TableField("CUSTGROUP_ID")
    private String custgroupId;

    /**
     * 目标用户数
     */
    @TableField("TARGET_CUSTOM_NUM")
    private Integer targetCustomNum;
    /**
     * 预演发起人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 预演时间
     */
    @TableField("PREVIEW_TIME")
    private Date previewTime;


    /**
     * 删除标识
     */
    @TableField("IS_DELETE")
    private Integer isDelete;

}

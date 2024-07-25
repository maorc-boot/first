package com.asiainfo.biapp.pec.preview.jx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author mamp
 * @date
 * IOP策略用户统计任务表
 * @TableName mcd_camp_user_count_task
 */
@TableName(value ="mcd_camp_user_count_task")
@Data
public class McdCampUserCountTask implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 子活动ID
     */
    private String campsegId;

    /**
     * 活动rootID
     */
    private String campsegRootId;

    /**
     * 渠道ID
     */
    private String channelId;

    /**
     * 客户群ID
     */
    private String custgroupId;

    /**
     * 清单文件名
     */
    private String custgroupFile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 任务状态，0- 待执行，1-执行中，2-执行完成，3-执行失败
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.asiainfo.biapp.pec.plan.jx.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author mamp
 * @since 2023-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MCD_SYS_USER_4A")
public class McdSysUser4a implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField("USER_ID")
    private String userId;

    @TableId(value = "LOGIN_NO", type = IdType.ID_WORKER_STR)
    private String loginNo;

    @TableField("USERNAME")
    private String username;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("EMAIL")
    private String email;

    @TableField("MOBILE")
    private String mobile;

    @TableField("PASSWORD")
    private String password;

    @TableField("STATUS")
    private String status;

    @TableField("EFFECT_DATE")
    private Date effectDate;

    @TableField("EXPIRE_DATE")
    private Date expireDate;

    @TableField("REMARK")
    private String remark;

    @TableField("DUTY")
    private String duty;

    @TableField("OPER_TYPE")
    private String operType;

    @TableField("OPER_LEVEL")
    private String operLevel;

    @TableField("DUTY_LEVEL")
    private String dutyLevel;


}

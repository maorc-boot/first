package com.asiainfo.biapp.pec.plan.jx.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("user_crm_om")
public class McdEmisUserCrmOmModel implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("ID")
    private String id;

    @TableField("USER_ID")
    private String userId;

    @TableField("USER_NAME")
    private String userName;

    @TableField("ID_CARD_NO")
    private String idCardNo;

    @TableField("MOBILE")
    private String mobile;

    @TableField("CITY_ID")
    private String cityId;
    @TableField("COUNTY_ID")
    private String countyId;
    @TableField("COUNTY_NAME")
    private String countyName;
    @TableField("DTRICT_ID")
    private String dtrictId;
    @TableField("DTRICT_NAME")
    private String dtrictName;
    @TableField("CHANNEL_TYPE")
    private String channelType;

    @TableField("STATUS")
    private String status;
    @TableField("POSTCODE")
    private String postCode;

    @TableField("EFFECT_TIME")
    private String effecttTime;

    @TableField("EXPIRE_TIME")
    private String expireTime;




}

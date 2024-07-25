package com.asiainfo.biapp.pec.eval.jx.report.online.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 中高端客户专席报表
 * </p>
 *
 * @author ranpf
 * @since 2023-5-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("中高端客户专席报表")
@TableName("dw_heigh_10088_city")
public class DWHeigh10088CityModel implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("数据日期")
    private String statDate;
    @ApiModelProperty("地市id")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("台席数")
    private int dTxCount;
    @ApiModelProperty("呼入量")
    private int dHrCount;
    @ApiModelProperty("呼出量")
    private int dHcCount;
    @ApiModelProperty("接通量")
    private int dJtCount;
    @ApiModelProperty("接通超过30s以上")
    @TableField("d_30_dur")
    private int d30Dur;
    @ApiModelProperty("接通率")
    private double dJtRate;

    @ApiModelProperty("台席数 ")
    private int mTxCount;
    @ApiModelProperty("呼入量 ")
    private int mHrCount;
    @ApiModelProperty("呼出量 ")
    private int mHcCount;
    @ApiModelProperty("接通量 ")
    private int mJtCount;
    @ApiModelProperty("接通超过30s以上 ")
    @TableField("m_30_dur")
    private int m30Dur;
    @ApiModelProperty("接通率 ")
    private double mJtRate;

}

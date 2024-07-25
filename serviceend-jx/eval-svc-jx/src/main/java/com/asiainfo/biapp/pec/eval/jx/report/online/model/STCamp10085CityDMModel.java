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
 * 呼出营销情况-分地市报表
 * </p>
 *
 * @author ranpf
 * @since 2023-5-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("呼出营销情况-分地市报表")
@TableName("st_campseg_10085_city_dm")
public class STCamp10085CityDMModel implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("数据日期")
    private String statDate;
    @ApiModelProperty("地市id")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("85导入用户数")
    private int dLoadNum;
    @ApiModelProperty("导入率")
    private double dLoadRate;
    @ApiModelProperty("85平台过滤数据")
    private int dGlNum;
    @ApiModelProperty("外呼用户数")
    private int dCallNum;
    @ApiModelProperty("外呼率")
    private double dCallRate;
    @ApiModelProperty("接通用户数")
    private int dJtNum;
    @ApiModelProperty("接通率")
    private double dJtRate;
    @ApiModelProperty("30s以上通话用户数")
    @TableField(value = "d_30_num")
    private int d30Num;
    @ApiModelProperty("同意办理用户数")
    private int dTyNum;
    @ApiModelProperty("同意办理率")
    private double dTyRate;
    @ApiModelProperty("实际办理客户数")
    private int dSuccessNum;
    @ApiModelProperty("转化率")
    private double dSuccessRate;

    @ApiModelProperty("85导入用户数 ")
    private int mLoadNum;
    @ApiModelProperty("导入率 ")
    private double mLoadRate;
    @ApiModelProperty("85平台过滤数据 ")
    private int mGlNum;
    @ApiModelProperty("外呼用户数 ")
    private int mCallNum;
    @ApiModelProperty("外呼率 ")
    private double mCallRate;
    @ApiModelProperty("接通用户数 ")
    private int mJtNum;
    @ApiModelProperty("接通率 ")
    private double mJtRate;
    @ApiModelProperty("30s以上通话用户数 " )
    @TableField("m_30_num")
    private int m30Num;
    @ApiModelProperty("同意办理用户数 ")
    private int mTyNum;
    @ApiModelProperty("同意办理率 ")
    private double mTyRate;
    @ApiModelProperty("实际办理客户数 ")
    private int mSuccessNum;
    @ApiModelProperty("转化率 ")
    private double mSuccessRate;


}

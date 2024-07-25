package com.asiainfo.biapp.pec.eval.jx.report.online.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 呼入营销情况渠道报表
 * </p>
 *
 * @author ranpf
 * @since 2023-5-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("呼入营销情况渠道报表")
@TableName("st_campseg_10086_city_dm")
public class STCamp10086CityDMModel implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("数据日期")
    private String statDate;
    @ApiModelProperty("地市id")
    private String cityId;
    @ApiModelProperty("地市名称")
    private String cityName;
    @ApiModelProperty("日来活用户数")
    private int dCallNum;
    @ApiModelProperty("目标客户弹窗量")
    private int dTcCnts;
    @ApiModelProperty("呼入弹窗用户数")
    private int dTcNum;
    @ApiModelProperty("弹窗覆盖率")
    private double dTcRate;
    @ApiModelProperty("办理客户数")
    private int dSuccessNum;
    @ApiModelProperty("办理笔数")
    private int dSuccessCnts;
    @ApiModelProperty("转化率")
    private double dSuccessRate;
    @ApiModelProperty("月来活用户数 ")
    private int mCallNum;
    @ApiModelProperty("目标客户弹窗量 ")
    private int mTcCnts;
    @ApiModelProperty("呼入弹窗用户数 ")
    private int mTcNum;
    @ApiModelProperty("弹窗覆盖率 ")
    private double mTcRate;
    @ApiModelProperty("办理客户数 ")
    private int mSuccessNum;
    @ApiModelProperty("办理笔数 ")
    private int mSuccessCnts;
    @ApiModelProperty("转化率2")
    private double mSuccessRate;


}

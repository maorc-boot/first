package com.asiainfo.biapp.pec.eval.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 分策略分地市分渠道报表
 * </p>
 *
 * @author mamp
 * @since 2022-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("分策略分地市分渠道报表")
public class DwKehutongClInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据日期
     */
    @ApiModelProperty("数据日期")
    private String statDate;

    /**
     * 活动类型名称
     */
    @ApiModelProperty("活动名称")
    private String campsegTypeName;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String campsegName;

    /**
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private String planTypeName;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String planName;

    /**
     * 客户群名称
     */
    @ApiModelProperty("客户群名称")
    private String customGroupName;

    /**
     * 营销目标用户数
     */
    @ApiModelProperty("营销目标用户数")
    private Integer targerUserNums;

    /**
     * 地市
     */
    @ApiModelProperty("地市")
    private String cityName;

    /**
     * 渠道
     */
    @ApiModelProperty("渠道")
    private String channelName;

    /**
     * 接触用户数
     * <p>
     * (分策略剃重)
     */
    @ApiModelProperty("接触用户数")
    private Integer dJcNum;

    /**
     * 接触率
     */
    @ApiModelProperty("接触率")
    private BigDecimal dJcRate;

    /**
     * 响应用户数
     * <p>
     * (分策略剃重)
     */
    @ApiModelProperty("响应用户数")
    private Integer dXyNum;

    /**
     * 响应率
     */
    @ApiModelProperty("响应率")
    private BigDecimal dXyRate;

    /**
     * 办理用户数
     * <p>
     * (分策略剃重)
     */
    @ApiModelProperty("办理用户数")
    private Integer dBlNum;

    /**
     * 转化率
     */
    @ApiModelProperty("转化率")
    private BigDecimal dBlRate;

    /**
     * 月：接触用户数(分策略剃重)
     */
    @ApiModelProperty("月：接触用户数")
    private Integer mJcNum;

    /**
     * 月接触率
     */
    @ApiModelProperty("月接触率")
    private BigDecimal mJcRate;

    /**
     * 月办理用户数
     */
    @ApiModelProperty("月办理用户数")
    private Integer mBlNum;

    /**
     * 月响应率
     */
    @ApiModelProperty("月响应率")
    private BigDecimal mXyRate;

    /**
     * 月响应用户数
     */
    @ApiModelProperty("月响应用户数")
    private Integer mXyNum;

    /**
     * 月办理率
     */
    @ApiModelProperty("月办理率")
    private BigDecimal mBlRate;


}

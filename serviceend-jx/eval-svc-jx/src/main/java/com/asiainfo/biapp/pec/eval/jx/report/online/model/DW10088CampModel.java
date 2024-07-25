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
@TableName("dw_10088_camp")
public class DW10088CampModel implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("数据日期")
    private String statDate;
    @ApiModelProperty("子任务编码")
    private String waringType;
    @ApiModelProperty("子任务名称")
    private String warnTypeNm;
    @ApiModelProperty("推荐商品编码")
    private String recGoodsCode;
    @ApiModelProperty("推荐商品名称")
    private String recGoodsName;
    @ApiModelProperty("接触数")
    private int dJcCount;
    @ApiModelProperty("接通数")
    private int dJtNum;
    @ApiModelProperty("接通率")
    private double dJtRate;
    @ApiModelProperty("办理成功数")
    private int dBlsuccNum;
    @ApiModelProperty("转化率")
    private double dSuccReate;

    @ApiModelProperty("接触数")
    private int mJcCount;
    @ApiModelProperty("接通数")
    private int mJtNum;
    @ApiModelProperty("接通率")
    private double mJtRate;
    @ApiModelProperty("办理成功数")
    private int mBlsuccNum;
    @ApiModelProperty("转化率")
    private double mSuccReate;

}

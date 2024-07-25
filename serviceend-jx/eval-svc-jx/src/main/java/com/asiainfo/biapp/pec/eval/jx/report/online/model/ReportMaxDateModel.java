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
 * 中高端客户专席报表
 * </p>
 *
 * @author ranpf
 * @since 2023-5-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("report_maxdate_info")
@ApiModel("在线报表数据日期关系数据")
public class ReportMaxDateModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("表描述")
    private String tableDesc;
    @ApiModelProperty("数据最新日期")
    private String maxDate;



}

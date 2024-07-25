package com.asiainfo.biapp.pec.eval.jx.report.online.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ranpf
 * @date 2023/5/17
 */
@ApiModel(value = "在线报表请求入参")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineReportDataQuery {

    /**
     *  在线报表查询表名:
     *  呼入营销情况:st_campseg_10086_city_dm ; 呼入营销情况-分策略:st_campseg_10086_hd_dm
     * 	呼出营销情况-分地市:st_campseg_10085_city_dm ; 呼出营销情况-分策略:st_campseg_10085_hd_dm
     * 	中高端客户专席明细:dw_10088_camp ; 中高端客户专席:dw_heigh_10088_city
     */
    @ApiModelProperty("在线报表查询表名")
    private String tablename;

    @ApiModelProperty("数据日期,格式:YYYYMMDD")
    private String statDate;

    @ApiModelProperty("当前页数")
    private Integer pageNum = 1;

    @ApiModelProperty("分页大小")
    private Integer pageSize = 10;

}

package com.asiainfo.biapp.pec.eval.jx.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 报表下载入参对象
 *
 * @author: lvchaochao
 * @date: 2023/1/16
 */
@Data
@ApiModel("报表下载入参对象")
public class ReportFormReq extends McdPageQuery {

    @ApiModelProperty("操作时间")
    private String opTime;
}

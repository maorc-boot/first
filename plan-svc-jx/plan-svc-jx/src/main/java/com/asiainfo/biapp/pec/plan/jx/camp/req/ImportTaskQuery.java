package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author mamp
 * @date 2023/4/11
 */
@Data
@ApiModel(value = "批量导入活动任务查询")
public class ImportTaskQuery  extends McdPageQuery {
}

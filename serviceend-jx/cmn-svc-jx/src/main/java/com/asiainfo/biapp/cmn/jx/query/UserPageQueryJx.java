package com.asiainfo.biapp.cmn.jx.query;

import com.asiainfo.biapp.cmn.query.UserPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/12/12
 */
@Data
@ApiModel(value = "用户分页查询入参", description = "用户分页查询入参")
public class UserPageQueryJx extends UserPageQuery {
    @ApiModelProperty(value = "用户归属地市")
    private String cityId;
    @ApiModelProperty(value = "创建时间:开始时间,格式:yyyy-MM-dd")
    private String startTime;
    @ApiModelProperty(value = "创建时间:结束时间,格式:yyyy-MM-dd")
    private String endTime;
}

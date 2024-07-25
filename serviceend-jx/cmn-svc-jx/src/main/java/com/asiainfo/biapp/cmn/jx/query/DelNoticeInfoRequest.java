package com.asiainfo.biapp.cmn.jx.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author ranpf
 * @since 2023-02-15
 */
@Data
@ApiModel(value="删除系统通知入参", description="删除系统通知入参")
public class DelNoticeInfoRequest  {

    @ApiModelProperty(value = "通知ID")
    private Long noticeId;


}

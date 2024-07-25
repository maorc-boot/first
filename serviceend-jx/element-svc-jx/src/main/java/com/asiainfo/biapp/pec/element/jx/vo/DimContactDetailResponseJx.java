package com.asiainfo.biapp.pec.element.jx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * Description : 触点详情 响应对象
 * </p>
 *
 * @author : wuhq6
 * @date : Created in 2021/12/8  9:43
 * //@modified By :
 * //@since :
 */

@Data
@ApiModel(value = "触点详情 响应对象",description = "触点详情 响应对象")
@ToString
public class DimContactDetailResponseJx {
    
    @ApiModelProperty(value = "触点ID")
    private String contactId;
    
    @ApiModelProperty(value = "渠道ID")
    private String channelId;
    
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    
    @ApiModelProperty(value = "来源: 0：省级触点信息 1：全网触点信息")
    private Integer contactSource;
    
    @ApiModelProperty(value = "触点类型: 0：电子渠道 1：实体渠道")
    private Integer contactType;
    
    @ApiModelProperty(value = "触点名称")
    private String contactName;
    
    @ApiModelProperty(value = "触点编码(信息安全)")
    private String contactCode;
    
    @ApiModelProperty(value = "触点状态")
    private Integer contactStatus;
    
    @ApiModelProperty(value = "是否可排期: 0：不可排期 1：可排期")
    private Integer isScheduled;
    
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ApiModelProperty(value = "触点描述")
    private String description;

    @ApiModelProperty(value = "触点客户类型 0-集团 1-成员")
    private Integer contactCustType;

    @ApiModelProperty(value = "执行交互流程 0-任务推送 1-推荐查询 2-更多类型")
    private Integer contactExecFlow;
    
}

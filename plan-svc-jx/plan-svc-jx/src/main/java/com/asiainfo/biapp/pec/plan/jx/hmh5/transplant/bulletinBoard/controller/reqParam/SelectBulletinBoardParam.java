package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 江西客户通公告栏表
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@ApiModel("公告栏条件查询的参数模型")
public class SelectBulletinBoardParam implements Serializable {

    @ApiModelProperty(value = "查询当前页，默认1", dataType = "Integer", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "查询页大小，默认10", dataType = "Integer", example = "10")
    private Integer pageSize;

    /**
     * 公告编号，最多64字符
     */
    @ApiModelProperty(value = "公告的bulletinCode，默认null")
    private String bulletinCode;

    /**
     * 查询内容，会从标题和内容中查找
     */
    @ApiModelProperty(value = "公告的标题或者内容，默认null")
    private String bulletinContent;

    /**
     * 展示规则，最多32字符
     */
    @ApiModelProperty(value = "公告的展示规则，默认null")
    private String bulletinRule;

    /**
     * 新增字段，创建人姓名，（一旦创建了公告，姓名应当不随用户姓名的改变而改变）
     */
    @ApiModelProperty(value = "公告的创建人名字，默认null")
    private String createUsername;

    /**
     * 查询创建的开始时间
     */
    @ApiModelProperty(value = "查询公告创建的开始时间，默认null",dataType = "DateTime", example = "2021-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    /**
     * 查询创建的结束时间
     */
    @ApiModelProperty(value = "查询公告创建的结束时间，默认null",dataType = "DateTime", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "查询公告生效的开始时间，默认null",dataType = "DateTime", example = "2021-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "查询公告生效的结束时间，默认null",dataType = "DateTime", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 状态 0-已上线 1-已下线 2-已失效
     */
    @ApiModelProperty(value = "查询公告的状态 0-已上线 1-已下线 2-已失效，默认null")
    private Integer status;

    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}

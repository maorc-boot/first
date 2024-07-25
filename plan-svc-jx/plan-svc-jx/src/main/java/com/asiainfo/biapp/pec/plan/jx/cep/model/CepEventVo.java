package com.asiainfo.biapp.pec.plan.jx.cep.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mamp
 * @date 2022/5/5
 */
@Getter
@Setter
@ApiModel(value = "事件信息", description = "事件信息")
public class CepEventVo {
    /**
     * 事件ID
     */
    private String eventId;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 事件一级分类ID
     */
    private String firstClassId;
    /**
     * 事件一级分类名称
     */
    private String firstClassName;
    /**
     * 事件二级分类ID
     */
    private String secondClassId;
    /**
     * 事件二级分类名称
     */
    private String secondClassName;
    /**
     * 创建人
     */
    private String createUserName;
    /**
     * 创建时间
     */
    private String createTime;

}

package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: liuyp
 * @Date: 2024/4/19
 */
@Data
@ApiModel(value = "根据主题查询场景与策略入参", description = "根据主题查询场景与策略入参")
public class SceneCampQuery extends McdPageQuery {
	

	
	@ApiModelProperty("排序字段,不传默认按照创建时间排序")
	private String orderColumn;
	
	@ApiModelProperty("排序规则,1:降序; 其他数字:升序。默认是1")
	private int order = 1;
	
}

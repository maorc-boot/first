package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "江西:统一物料搜索信息", description = "统一物料搜索信息")
public class SearchInfo implements Serializable{


	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "搜索分类01：服务; 02：活动 03：业务; 04：手机配件; 05：权益; 06：积分;07：音乐; 08：阅读; 09：扶贫; 10：视频; 11：无忧行")
	private String  searchType;
	@ApiModelProperty(value = "搜索关键词")
	private String  searchKey;
	@ApiModelProperty(value = "搜索图片地址URL")
	private String  searchImgName;


}

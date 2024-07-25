package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "江西:集团统一物料相关实体", description = "集团统一物料相关实体")
public class MaterialJxDataVo implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "物料id")
	private String matId;
	@ApiModelProperty(value = "物料名称")
	private String matName;
	@ApiModelProperty(value = "物料类型")
	private String matType;
	@ApiModelProperty(value = "物料时间")
	private String matTime;
	@ApiModelProperty(value = "物料链接")
	private String matUrl;
	@ApiModelProperty(value = "表示运营位样式 positionStyle")
	private String ext3;
	@ApiModelProperty(value = "是否支持脚本 0支持,1不支持")
	private String ifJb;
	@ApiModelProperty(value = "角标类型,  1文字,2图片,3红点")
	private String jbType;
	@ApiModelProperty(value = "显示方式 0一直显示, 1 点击后显示一次")
	private String jbShow;
	@ApiModelProperty(value = "文字角标内容")
	private String jbText;
	@ApiModelProperty(value = "图片角标图片")
	private String jbPicSrc;

    @ApiModelProperty(value = "客群信息")
	private UGInfo ugInfo;

    @ApiModelProperty(value = "角标信息")
	private CardInfo cardInfo;

    @ApiModelProperty(value = "搜索信息")
	private SearchInfo searchInfo;

    @ApiModelProperty(value = "通用文本信息")
	private GeneralInfo generalInfo;
	
	
	

}

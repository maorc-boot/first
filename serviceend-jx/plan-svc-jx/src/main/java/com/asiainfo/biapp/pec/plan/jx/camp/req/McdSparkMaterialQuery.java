package com.asiainfo.biapp.pec.plan.jx.camp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ranpf
 * @date 2023/11/28 20:43
 */
@Data
@ApiModel(value = "查询星火素材入参模型")
public class McdSparkMaterialQuery {

    @ApiModelProperty(value = "省份编码")
    private String provinceCode;

    @ApiModelProperty(value = "素材分类 Id")
    private Integer categoryId;

    @ApiModelProperty(value = "素材类别 1： 普通 h5 素材 2：文本素材 4：总部商品 5：省份商品 7 图片 14 小程序")
    private Integer materialType;

    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "搜索关键字素材名称")
    private String search;




}

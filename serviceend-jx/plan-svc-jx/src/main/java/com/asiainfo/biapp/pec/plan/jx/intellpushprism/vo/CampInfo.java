package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.vo.req.CampChildrenScheme;
import com.asiainfo.biapp.pec.plan.vo.req.CampScheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description: 智推棱镜: 活动信息保存对象
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@Data
@ApiModel(value = "智推棱镜：活动信息保存对象", description = "智推棱镜：活动信息保存对象")
public class CampInfo {

    @NotNull(message = "江西:主活动基本信息不能为空")
    @ApiModelProperty(value = "主活动基本信息", required = true)
    private CampBaseInfoJxVO baseCampInfo;

    @NotEmpty(message = "子活动策划信息不能为空")
    @ApiModelProperty(value = "子活动策划信息", required = true)
    private List<CampScheme> campSchemes;

    @NotEmpty(message = "单个子策略项列表不能为空")
    @ApiModelProperty(value = "单个子策略项列表", required = true)
    private List<CampChildrenScheme> childrenSchemes;
}

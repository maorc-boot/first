package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.plan.vo.req.CampChildrenScheme;
import com.asiainfo.biapp.pec.plan.vo.req.CampScheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ranpf
 * @date 2023/2/8
 */
@Data
@ApiModel(value = "江西:对外策略详情对象", description = "江西:对外策略详情对象")
public class TacticsBaseInfoJx {
    @ApiModelProperty(value = "策略产品扩展信息：融合产品，同系列产品，互斥产品等")
    private List<PlanExtInfo> planExtInfoList;
    /**
     * 策略信息
     */
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

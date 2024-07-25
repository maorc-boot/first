package com.asiainfo.biapp.pec.plan.jx.kpi.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * description: 指标口径实体
 *
 * @author: lvchaochao
 * @date: 2023/1/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("DIM_EVAL_INDEX_INFO")
@ApiModel(value="DimEvalIndexInfo对象", description="指标口径实体")
public class DimEvalIndexInfo extends Model<DimEvalIndexInfo> {

    @ApiModelProperty(value = "指标ID")
    @TableId("INDEX_ID")
    private String indexId;

    @ApiModelProperty(value = "指标名称")
    @TableField("INDEX_NAME")
    private String indexName;

    @ApiModelProperty(value = "指标描述")
    @TableField("INDEX_DESC")
    private String indexDesc;

    @ApiModelProperty(value = "指标类型 0：客户接触情况 1：营销成功情况 2：营销效益情况 3：客户质量情况 4：资源使用情况")
    @TableField("INDEX_TYPE")
    private String indexType;
}

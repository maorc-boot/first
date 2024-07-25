package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 选择标签
 */
@Data
@ApiModel(value = "选择标签查询实体", description = "选择标签查询实体")
public class ColumnSearchQuery extends McdPageQuery {
    /**
     * 标签名(支持模糊匹配）
     */
    @NotNull(message = "标签名不能为null")
    @ApiModelProperty(value = "标签名(支持模糊匹配)", required = true)
    private String columnName;
    /**
     * 标签大类id
     */
    @ApiModelProperty(value = "标签大类id")
    private List<String> classLevel1;
    /**
     * 更新频率:2日表 3月表
     */
    @ApiModelProperty(value = "更新频率:2日表 3月表")
    private Long updateCycle;

    public ColumnSearchRequestDTO transToColumnSearchRequestDTO() {
        ColumnSearchRequestDTO columnSearchRequestDTO = new ColumnSearchRequestDTO();
        columnSearchRequestDTO.setColumnName(this.getColumnName());
        columnSearchRequestDTO.setUpdateCycle(this.getUpdateCycle());
        columnSearchRequestDTO.setClassLevel1(this.getClassLevel1());
        columnSearchRequestDTO.setPageIndex(this.getCurrent());
        columnSearchRequestDTO.setPageSize(this.getSize());
        return columnSearchRequestDTO;
    }
}

package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 获取标签映射值
 */
@Data
@ApiModel(value = "获取标签映射值查询实体", description = "获取标签映射值查询实体")
public class ColumnValuePageQuery extends McdPageQuery {
    /**
     * 标签dna位
     */
    @NotEmpty(message = "标签dna位不能为空")
    @ApiModelProperty(value = "标签dna位", required = true)
    private String columnNum;
    /**
     * 搜索参数
     */
    @ApiModelProperty(value = "搜索参数")
    private String keyWord;
    /**
     * 分段值1
     */
    @ApiModelProperty(value = "分段值1")
    private String numVal1;
    /**
     * 分段值2
     */
    @ApiModelProperty(value = "分段值2")
    private String numVal2;

    public ColumnValuePageRequestDTO transToColumnValuePageRequestDTO() {
        ColumnValuePageRequestDTO columnValuePageRequestDTO = new ColumnValuePageRequestDTO();
        columnValuePageRequestDTO.setColumnNum(this.getColumnNum());
        columnValuePageRequestDTO.setKeyWord(this.getKeyWord());
        columnValuePageRequestDTO.setNumVal1(this.getNumVal1());
        columnValuePageRequestDTO.setNumVal2(this.getNumVal2());
        columnValuePageRequestDTO.setPageIndex(this.getCurrent());
        columnValuePageRequestDTO.setPageSize(this.getSize());
        return columnValuePageRequestDTO;
    }
}

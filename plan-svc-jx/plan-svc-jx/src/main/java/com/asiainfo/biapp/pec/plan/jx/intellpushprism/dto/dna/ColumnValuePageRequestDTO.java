package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import lombok.Data;

/**
 * 获取标签映射值
 */
@Data
public class ColumnValuePageRequestDTO extends DNAPageQuery {
    /**
     * 标签dna位
     */
    private String columnNum;
    /**
     * 搜索参数
     */
    private String keyWord;
    /**
     * 分段值1
     */
    private String numVal1;
    /**
     * 分段值2
     */
    private String numVal2;
}

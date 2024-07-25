package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import lombok.Data;

import java.util.List;

/**
 * 选择标签
 */
@Data
public class ColumnSearchRequestDTO extends DNAPageQuery {
    /**
     * 标签大类id
     */
    private List<String> classLevel1;
    /**
     * 标签名(支持模糊匹配）
     */
    private String columnName;
    /**
     * 更新频率:2日表 3月表
     */
    private Long updateCycle;
}

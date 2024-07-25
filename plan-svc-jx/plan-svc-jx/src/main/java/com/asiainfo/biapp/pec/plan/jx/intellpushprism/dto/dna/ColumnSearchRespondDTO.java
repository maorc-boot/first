package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ColumnSearchRespondDTO extends DNAPageData {

    /**
     * 标签列表.
     */
    private List<ColumnList> columnList;

    public IPage<ColumnList> wrapPage(McdPageQuery mcdPageQuery) {
        Page<ColumnList> columnListPage = new Page<>(mcdPageQuery.getCurrent(), mcdPageQuery.getSize(), this.getTotal());
        columnListPage.setRecords(columnList);
        return columnListPage;
    }

    /**
     * 标签列表
     */
    @Data
    @ApiModel(value = "标签列表", description = "标签列表")
    public static class ColumnList {
        /**
         * 标签id
         */
        @ApiModelProperty(value = "标签id")
        private Long id;
        /**
         * 标签名称
         */
        @ApiModelProperty(value = "标签名称")
        private String columnName;
        /**
         * 标签DNA位
         */
        @ApiModelProperty(value = "标签DNA位")
        private Long columnNum;
        /**
         * 标签类型 1 枚举型 2数值型
         */
        @ApiModelProperty(value = "标签类型 1 枚举型 2数值型")
        private Long valueType;
        /**
         * 数据来源
         */
        @ApiModelProperty(value = "数据来源")
        private String dataSource;
        /**
         * 一级分类id
         */
        @ApiModelProperty(value = "一级分类id")
        private String classLevel1;
        /**
         * 二级分类id
         */
        @ApiModelProperty(value = "二级分类id")
        private String classLevel2;
        private String columnAtSourceFile;
        /**
         * 完整路径(包含分类信息)
         */
        @ApiModelProperty(value = "完整路径(包含分类信息)")
        private String columnClassPath;
        private String columnField;
        private String createTime;
        private String creatorId;
        private Object creatorName;
        private String dataPeriod;
        private String expiredTime;
        private String fileIndex;
        private String isDeleted;
        private Object isFavorited;
        private String lastUpdateTime;
        private Object lastUpdatorId;
        private Object lastUpdatorName;
        private String remark;
        private String sourceFileName;
        private String status;
        private Object updateCycle;
        private Object updateStatus;
        private Object useCount;
        private String variableFlag;
    }
}

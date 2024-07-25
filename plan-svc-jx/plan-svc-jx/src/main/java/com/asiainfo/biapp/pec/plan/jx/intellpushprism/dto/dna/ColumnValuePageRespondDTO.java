package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ColumnValuePageRespondDTO extends DNAPageData {
    /**
     * 标签名称
     */
    private String columnName;
    /**
     * 标签位
     */
    private Long columnNum;
    /**
     * 标签类型 1 枚举型 2数值型
     */
    private Long valueType;
    /**
     * 值集合
     */
    private List<ValueList> valueList;

    public IPage<ValueList> wrapPage(McdPageQuery mcdPageQuery) {
        Page<ValueList> valueListPage = new Page<>(mcdPageQuery.getCurrent(), mcdPageQuery.getSize(), this.getTotal());
        valueListPage.setRecords(valueList);
        return valueListPage;
    }

    /**
     * 标签映射值
     */
    @Data
    @ApiModel(value = "标签映射值", description = "标签映射值")
    public static class ValueList {
        /**
         * 主键
         */
        @ApiModelProperty(value = "主键")
        private Long id;
        /**
         * 映射值
         */
        @ApiModelProperty(value = "映射值")
        private String value;
        /**
         * 计算方式 字符串:=,数字型：< <= > >= [] (]等
         */
        @ApiModelProperty(value = "计算方式 字符串:=,数字型：< <= > >= [] (]等")
        private String condition;
        /**
         * 用户数
         */
        @ApiModelProperty(value = "用户数")
        private Long count;
        /**
         * 数值型下限
         */
        @ApiModelProperty(value = "数值型下限")
        private String startValue;
        /**
         * 数值型上限
         */
        @ApiModelProperty(value = "数值型上限")
        private String endValue;
        private String remark;
        /**
         * 字符型的数值
         */
        @ApiModelProperty(value = "字符型的数值")
        private String strValue;
    }
}

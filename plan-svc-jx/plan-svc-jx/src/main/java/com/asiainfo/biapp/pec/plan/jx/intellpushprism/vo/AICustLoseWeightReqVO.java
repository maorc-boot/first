package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description: 查询AI客群瘦身分析结果入参对象实体
 *
 * @author: lvchaochao
 * @date: 2024/6/17
 */
@ApiModel("查询AI客群瘦身分析结果入参对象实体")
@Data
public class AICustLoseWeightReqVO {

    @ApiModelProperty("标签集合")
    private List<TreeDetails> treeDetails;

    @ApiModelProperty("产品id")
    private String planId;

    @Data
    public static class TreeDetails {
        private String id;
        private int parentId;
        private String type;
        private String calType;
        private Object columnName;
        private int columnNum;
        private Object updateCycle;
        private int isFission;
        private List<String> selectedValues;
        @ApiModelProperty("标签值条件属性对象")
        private List<SelectedConditions> selectedConditions;
    }

    @ApiModel("标签值条件属性对象")
    @Data
    public static class SelectedConditions {
        private String condition; // 符号 = > <
        private int count;
        private String endValue;
        private int id;
        private String label;
        private String remark; // 果洛  标签中文
        private String startValue;
        private String strValue; // dna标签实际值
        private String value; // dna内部编码无业务意义，计算保存用
    }
}

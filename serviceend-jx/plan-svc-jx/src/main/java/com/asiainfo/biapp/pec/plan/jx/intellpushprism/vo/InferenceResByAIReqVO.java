package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description: AI执行推理任务入参对象
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
@NoArgsConstructor
@ApiModel("AI执行推理任务入参对象")
@Data
public class InferenceResByAIReqVO implements Serializable {
    
    /**
     * channelId :
     * coordinationSwitch : 0
     * executeType : 0
     * hotSwitch : 0
     * marketGoal :
     * prodCustGroupList : [{"custgroupList":[{"fullFileName":"","phoneIndex":0,"customGroupName":"","customNum":0,"customgroupId":"","dataDate":"","isDna":null,"fileColumnsList":[{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"Integer","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"Integer","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"Integer","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}]}],"planId":"","priority":0}]
     * prodPriorityList : [{"peopleCnt":0,"planId":"","priority":0,"planDescribe":"124342","limitScore":0.5,"recommendMode":1,"presetCustName":"预设客群"}]
     * recommendGoal : 0
     * recommendProdNum : 0
     * taskEffectTime :
     * taskFailTime :
     * taskId :
     * taskName :
     * taskType : 0
     * execFlag : 1
     * createUserId : admin
     * createUserName : 管理员
     * downshiftFilter : 1
     */
    @ApiModelProperty("渠道id，可多选，用英文逗号分隔，-1表示自由推荐渠道")
    private String channelId;

    @ApiModelProperty("协同过滤的开关 0：关闭 1：打开 针对客群导向有效")
    private Integer coordinationSwitch;

    @ApiModelProperty("执行方式 0：手动执行 1：定时执行")
    private Integer executeType;

    @ApiModelProperty("选择执行周期类型  0：日   1：周  2：月")
    private Integer dataType;

    @ApiModelProperty("执行日期 针对定时执行")
    private String execWeek;

    @ApiModelProperty("执行时间 时：分")
    private String execTime;

    @ApiModelProperty("热度召回开关 0：关闭 1：打开 针对客群导向有效")
    private Integer hotSwitch;

    @ApiModelProperty("营销目标 多个值用英文逗号分割 1-办理率 2-高价值 1,2-综合收益")
    private String marketGoal;

    @ApiModelProperty("推荐目标 针对客群导向有效 0：产品 1：内容")
    private Integer recommendGoal;

    @ApiModelProperty("推荐产品个数 针对客群导向有效 1-3 注：产品数量=推荐数量")
    private Integer recommendProdNum;

    @ApiModelProperty("任务生效时间 yyyy-MM-dd 当前时间")
    private String taskEffectTime;

    @ApiModelProperty("任务失效时间 yyyy-MM-dd 任务失效时间与推荐客群失效时间一致（2099年）")
    private String taskFailTime;

    @ApiModelProperty("主键 任务id")
    private String taskId;

    @ApiModelProperty("任务名称 task_${时间戳}")
    private String taskName;

    @ApiModelProperty("任务类型 0：产品导向 1：客群导向 2：策略导向")
    private Integer taskType;

    @ApiModelProperty("执行标志 1-立即执行")
    private String execFlag;

    @ApiModelProperty("创建人id 决定推荐客群能被哪些同步选到webservices的推送人")
    private String createUserId;

    @ApiModelProperty("创建人名称 决定推荐客群能被哪些同步选到webservices的推送人")
    private String createUserName;

    @ApiModelProperty("")
    private String downshiftFilter;

    @ApiModelProperty("召回客群集合 ProdCustgrou如果未选客群范围，可以不传这个属性列表")
    private List<ProdCustGroupList> prodCustGroupList;

    @ApiModelProperty("产品集合")
    private List<ProdPriorityList> prodPriorityList;

    @NoArgsConstructor
    @Data
    public static class ProdCustGroupList implements Serializable {
        /**
         * custgroupList : [{"fullFileName":"","phoneIndex":0,"customGroupName":"","customNum":0,"customgroupId":"","dataDate":"","isDna":null,"fileColumnsList":[{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"Integer","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"Integer","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"Integer","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}]}]
         * planId :
         * priority : 0
         */
        @ApiModelProperty("产品编码 -1-表示自由推荐产品")
        private String planId;

        @ApiModelProperty("产品优先级 0-最大 9999-最小")
        private Integer priority;

        @ApiModelProperty("召回客群")
        private List<CustgroupList> custgroupList;

        @ApiModelProperty("标签裂变对应的表达式集合")
        private List<String> ruleList;

        @NoArgsConstructor
        @Data
        public static class CustgroupList implements Serializable {
            /**
             * fullFileName :
             * phoneIndex : 0
             * customGroupName :
             * customNum : 0
             * customgroupId :
             * dataDate :
             * isDna : null
             * fileColumnsList : [{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"Integer","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"Integer","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"Integer","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}]
             */
            @ApiModelProperty("客群清单FTP全路径")
            private String fullFileName;

            @ApiModelProperty("号码 用户列下标")
            private Integer phoneIndex;

            @ApiModelProperty("客群名称")
            private String customGroupName;

            @ApiModelProperty("客群规模")
            private Integer customNum;

            @ApiModelProperty("客群编码")
            private String customgroupId;

            @ApiModelProperty("数据日期")
            private String dataDate;

            @ApiModelProperty("是否dna客群 1-是")
            private Object isDna;

            @ApiModelProperty("清单文件字段说明")
            private List<FileColumnsList> fileColumnsList;

            @NoArgsConstructor
            @Data
            public static class FileColumnsList implements Serializable {
                /**
                 * columnLength : 11
                 * columnType : str
                 * columnCnName : 用户ID
                 * columnIndex : 0
                 * columnName : user_id
                 */
                @ApiModelProperty("字段长度")
                private String columnLength;

                @ApiModelProperty("字段类型")
                private String columnType;

                @ApiModelProperty("字段解释")
                private String columnCnName;

                @ApiModelProperty("字段位置")
                private Integer columnIndex;

                @ApiModelProperty("字段名")
                private String columnName;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class ProdPriorityList implements Serializable {
        /**
         * peopleCnt : 0
         * planId :
         * priority : 0
         * planDescribe : 124342
         * limitScore : 0.5
         * recommendMode : 1
         * presetCustName : 预设客群
         */
        @ApiModelProperty("预设规模")
        private Integer peopleCnt;

        @ApiModelProperty("产品编码 -1：自由推荐产品")
        private String planId;

        @ApiModelProperty("产品优先级 0最大，9999最小")
        private Integer priority;

        @ApiModelProperty("产品描述")
        private String planDescribe;

        @ApiModelProperty("截断分数-左区间 0.01-1.0的两位小数，用于截断用户打分")
        private Double limitScore = null;

        @ApiModelProperty("截断分数-右区间 0.01-1.0的两位小数，用于截断用户打分")
        private Double maxScore = null;

        @ApiModelProperty("推荐客群推送模式 0=每次新增客群，客群id变化; 1=每次更新客群，客群id不变")
        private Integer recommendMode;

        @ApiModelProperty("预设客群名称")
        private String presetCustName;
    }
}

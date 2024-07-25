package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description: 智慧大脑推荐客群回调入参对象
 *
 * @author: lvchaochao
 * @date: 2024/5/25
 */
@NoArgsConstructor
@ApiModel("智慧大脑推荐客群回调入参对象")
@Data
public class CallbackTaskReqVO implements Serializable {


    /**
     * status : SUCCESS
     * info : {"taskId":"123456789","execDate":"2023-08-23","time":"1小时50分钟","custList":[{"planId":"600000334517","channelId":"10086","custId":"KHQ000000002","custInfo":{"custNum":0,"fileColumnsList":[{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"int","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"int","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"int","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}],"filePath":"/xx/x/xx/abc.txt"}}]}
     */

    private String status;
    private Info info;

    @NoArgsConstructor
    @Data
    public static class Info implements Serializable {
        /**
         * taskId : 123456789
         * execDate : 2023-08-23
         * time : 1小时50分钟
         * custList : [{"planId":"600000334517","channelId":"10086","custId":"KHQ000000002","custInfo":{"custNum":0,"fileColumnsList":[{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"int","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"int","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"int","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}],"filePath":"/xx/x/xx/abc.txt"}}]
         */

        private String taskId;
        private String execDate;
        private String time;
        private List<CustList> custList;

        @NoArgsConstructor
        @Data
        public static class CustList implements Serializable {
            /**
             * planId : 600000334517
             * channelId : 10086
             * custId : KHQ000000002
             * custInfo : {"custNum":0,"fileColumnsList":[{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"int","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"int","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"int","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}],"filePath":"/xx/x/xx/abc.txt"}
             */

            private String planId;
            private String channelId;
            private String custId;
            private CustInfo custInfo;

            @NoArgsConstructor
            @Data
            public static class CustInfo implements Serializable {
                /**
                 * custNum : 0
                 * fileColumnsList : [{"columnLength":"11","columnType":"str","columnCnName":"用户ID","columnIndex":0,"columnName":"user_id"},{"columnLength":"10","columnType":"str","columnCnName":"产品ID","columnIndex":1,"columnName":"plan_id"},{"columnLength":"1","columnType":"int","columnCnName":"办理率","columnIndex":2,"columnName":"cvr_score"},{"columnLength":"1","columnType":"int","columnCnName":"价值变化","columnIndex":3,"columnName":"arpu_score"},{"columnLength":"1","columnType":"int","columnCnName":"综合收益","columnIndex":4,"columnName":"mul_score"}]
                 * filePath : /xx/x/xx/abc.txt
                 */

                private int custNum;
                private String filePath;
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

                    private String columnLength;
                    private String columnType;
                    private String columnCnName;
                    private int columnIndex;
                    private String columnName;
                }
            }
        }
    }
}

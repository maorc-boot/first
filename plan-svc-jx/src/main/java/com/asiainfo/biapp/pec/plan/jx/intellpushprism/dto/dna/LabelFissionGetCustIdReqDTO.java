package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description: 标签裂变获取客群编号接口请求参数对象
 *
 * @author: lvchaochao
 * @date: 2024/4/16
 */
@NoArgsConstructor
@Data
public class LabelFissionGetCustIdReqDTO implements Serializable {

    /**
     * sign :
     * requestNo :
     * version :
     * pageIndex : 0
     * pageSize : 0
     * customerCreateVos : [{"mergeOrRejectType":null,"mergeOrRejectCustomerId":null,"treeName":"2727832裂变客群1","businessType":"1","remark":"2727832裂变客群1","startTime":"2024-04-16","endTime":"2024-04-28","executeType":"1","isPushIop":"1","treeDetails":[{"id":1,"parentId":0,"type":"root","calType":"=","columnName":null,"columnNum":0,"selectedValues":["Y"],"updateCycle":null},{"id":2,"parentId":1,"type":"normal","isFission":0,"calType":"=","columnName":"网格地州(日标签)","columnNum":332,"selectedValues":["2","5"],"updateCycle":"日"}]},{"mergeOrRejectType":null,"mergeOrRejectCustomerId":null,"treeName":"3727832裂变客群1","businessType":"1","remark":"3727832裂变客群1","startTime":"2024-04-16","endTime":"2024-04-28","executeType":"1","isPushIop":"1","treeDetails":[{"id":1,"parentId":0,"type":"root","calType":"=","columnName":null,"columnNum":0,"selectedValues":["Y"],"updateCycle":null},{"id":2,"parentId":1,"type":"normal","isFission":0,"calType":"=","columnName":"网格地州(日标签)","columnNum":332,"selectedValues":["2","5"],"updateCycle":"日"}]}]
     */

    private String sign;
    private String requestNo;
    private String version;
    private int pageIndex;
    private int pageSize;
    private List<CustomerCreateVos> customerCreateVos;

    @NoArgsConstructor
    @Data
    public static class CustomerCreateVos implements Serializable {
        /**
         * mergeOrRejectType : null
         * mergeOrRejectCustomerId : null
         * treeName : 2727832裂变客群1
         * businessType : 1
         * remark : 2727832裂变客群1
         * startTime : 2024-04-16
         * endTime : 2024-04-28
         * executeType : 1
         * isPushIop : 1
         * treeDetails : [{"id":1,"parentId":0,"type":"root","calType":"=","columnName":null,"columnNum":0,"selectedValues":["Y"],"updateCycle":null},{"id":2,"parentId":1,"type":"normal","isFission":0,"calType":"=","columnName":"网格地州(日标签)","columnNum":332,"selectedValues":["2","5"],"updateCycle":"日"}]
         */

        private Object mergeOrRejectType;
        private Object mergeOrRejectCustomerId;
        private String treeName;
        private String businessType;
        private String remark;
        private String startTime;
        private String endTime;
        private String executeType;
        private String isPushIop;
        private List<TreeDetails> treeDetails;

        @NoArgsConstructor
        @Data
        public static class TreeDetails implements Serializable {
            /**
             * id : 1
             * parentId : 0
             * type : root
             * calType : =
             * columnName : null
             * columnNum : 0
             * selectedValues : ["Y"]
             * updateCycle : null
             * isFission : 0
             */

            private int id;
            private int parentId;
            private String type;
            private String calType;
            private Object columnName;
            private int columnNum;
            private Object updateCycle;
            private int isFission;
            /**
             * 数值型 左区间
             */
            private Object numVal1;
            /**
             * 数值型 右区间
             */
            private Object numVal2;
            private List<String> selectedValues;
        }
    }
}

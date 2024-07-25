package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description: 根据标签映射值实时获取客群数量请求入参对象
 *
 * @author: lvchaochao
 * @date: 2024/4/22
 */
@NoArgsConstructor
@Data
@ApiModel("根据标签映射值实时获取客群数量请求入参对象")
public class TargetUserCountReqDTO implements Serializable {


    /**
     * sign :
     * requestNo :
     * version :
     * pageIndex : 0
     * pageSize : 0
     * customerVo : {"treeDetails":[{"id":1,"parentId":0,"type":"root","calType":"=","columnName":null,"columnNum":0,"selectedValues":["Y"],"updateCycle":null},{"id":2,"parentId":1,"type":"normal","isFission":0,"calType":"=","columnName":"网格地州(日标签)","columnNum":332,"selectedValues":["2","5"],"updateCycle":"日"}]}
     */

    private String sign;
    private String requestNo;
    private String version;
    private int pageIndex;
    private int pageSize;
    private CustomerVo customerVo;

    @NoArgsConstructor
    @Data
    public static class CustomerVo implements Serializable {
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

package com.asiainfo.biapp.pec.eval.jx.req;

import lombok.Data;

/**
 * @author : shijl8
 * @version : v1.0
 * @className : EvalSaveCaseReq
 * @description : [描述说明该类的功能]
 * @createTime : [2023-07-26]
 */
@Data
public class EvalSaveCaseReq {
    //任务id
    private String taskId;
    //任务名称
    private String caseName;
    //任务描述
    private String caseDes;
}

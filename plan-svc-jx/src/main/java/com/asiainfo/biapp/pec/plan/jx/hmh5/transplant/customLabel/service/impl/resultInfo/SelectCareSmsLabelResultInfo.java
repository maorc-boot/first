package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.impl.resultInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelectCareSmsLabelResultInfo implements Serializable {
    private Integer serialNum;
    private String labelName;
    private String labelCode;
    private String labelDesc;
    private String cityCode;
    private Integer dataState;
    private String dataTableName;
    private String labelCondition;
    private String createBy;
    private Date createTime;
}

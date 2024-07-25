package com.asiainfo.biapp.pec.plan.jx.channelconfig.vo;

import lombok.Data;

@Data
public class McdChannel implements java.io.Serializable {

    private static final long serialVersionUID = -81148059180044511L;

    private String channelId;
    private String channelName;
    private Integer displayOrder;
    private String parentId;
    private String channelCode;
    private String iopChannelName;

    private String campId;
    //  IMCD_ZJ 为了统一前台模板使用  起别名
    private String typeId;
    private String typeName;

    private String Num;//优先级模块使用

    private String channelTypeId;

    private String channelTypeName;

    private String channelClass;

}

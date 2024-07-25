package com.asiainfo.biapp.pec.plan.jx.link.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Link implements Serializable {

    private String id;
    private String name;
    private String url;
    private String planId;
    private String planName;
    //1未使用, 2 已删除, 3 使用中
    private String status;
    private String createDate;
    private String creator;
    private String description;
    private String aswitch;
    private String campSize;

    public Link() {
    }

}

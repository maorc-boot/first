package com.asiainfo.biapp.pec.plan.jx.hmh5.enums;

import lombok.Getter;

/**
 * description: 客户通黑名单审批状态枚举
 *
 * @author: lvchaochao
 * @date: 2024/5/29
 */
@Getter
public enum Hmh5BlacklistEnum {
    DRAFT("0", 0, "草稿"),
    APPROVING("1", 1, "审批中"),
    APPROVE_BACK("2", 2, "审批退回"),
    IMPORTING("3", 3, "导入中"),
    IMPORT_FINISH("4", 4, "导入完成"),
    APPROVE_FINISH("5", 5, "审批完成");


    /**
     * 状态码
     */
    private String code;

    /**
     * 状态码
     */
    private Integer id;

    /**
     * 状态描述
     */
    private String desc;

    Hmh5BlacklistEnum(String code, Integer id, String desc) {
        this.code = code;
        this.id = id;
        this.desc = desc;
    }
}

package com.asiainfo.biapp.pec.plan.jx.camp.enums;

import lombok.Getter;

@Getter
public enum ImportTaskStatus {

    RUNNING("1", "1", "执行中"),
    PREVIEWING("2", "2", "执行完成"),
    PREVIEWED("3", "3", "执行失败");


    /**
     * 状态码
     */
    private String code;

    /**
     * 状态码
     */
    private String id;

    /**
     * 状态描述
     */
    private String desc;

    ImportTaskStatus(String code, String id, String desc) {
        this.code = code;
        this.id = id;
        this.desc = desc;
    }

    /**
     * id映射name
     *
     * @param id
     * @return
     */
    public static String valueOfId(String id) {
        for (ImportTaskStatus campStatusJx :
                ImportTaskStatus.values()) {
            if (campStatusJx.id.equals(id)) {
                return campStatusJx.desc;
            }
        }
        return null;
    }
}


package com.asiainfo.biapp.pec.plan.jx.camp.enums;

import lombok.Getter;

@Getter
public enum CampStatusJx {

    DRAFT("20", 20, "草稿"),
    PREVIEWING("31", 31, "预演中"),
    PREVIEWED("32", 32, "预演完成"),
    PREVIEW_ERROR("33", 33, "预演失败"),
    APPROVING("40", 40, "审批中"),
    APPROVE_BACK("41", 41, "审批退回"),
    PENDING("50", 50, "待执行"),
    PUBLISHING("52", 52, "待发布"),
    EXECUTED("54", 54, "执行中"),
    PAUSE("59", 59, "暂停"),
    DONE("90", 90, "完成"),
    SUSPEND("91", 91, "终止");

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

    CampStatusJx(String code, Integer id, String desc) {
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
    public static String valueOfId(Integer id) {
        for (CampStatusJx campStatusJx :
                CampStatusJx.values()) {
            if (campStatusJx.id.equals(id)) {
                return campStatusJx.desc;
            }
        }
        return null;
    }
}


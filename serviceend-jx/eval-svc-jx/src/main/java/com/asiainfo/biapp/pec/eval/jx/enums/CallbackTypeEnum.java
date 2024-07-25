package com.asiainfo.biapp.pec.eval.jx.enums;

import lombok.Getter;

/**
 * 5G云卡回调类型枚举
 *
 * @author lvcc
 * @date 2023/10/13
 */
@Getter
public enum CallbackTypeEnum {

    CWBBDCS("1", 1, "纯文本必达消息（长时）"),
    H5BDCS("2", 2, "H5必达消息（长时） "),
    APPROVE_BACK("3", 3, "纯文本必达消息（短时）"),
    PENDING("4", 4, "强显消息"),
    PUBLISHING("5", 5, "留空"),
    EXECUTED("6", 6, "短信"),
    PAUSE("7", 7, "H5必达消息（短时）"),
    DONE("8", 8, "语音通知"),
    SUSPEND("9", 9, "强显消息（异网）"),
    DXYW("10", 10, "短信（异网）");

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

    CallbackTypeEnum(String code, Integer id, String desc) {
        this.code = code;
        this.id = id;
        this.desc = desc;
    }

    /**
     * id映射name
     *
     * @param id 类型id
     * @return 类型名称
     */
    public static String valueOfId(Integer id) {
        for (CallbackTypeEnum campStatus :
                CallbackTypeEnum.values()) {
            if (campStatus.id.equals(id)) {
                return campStatus.desc;
            }
        }
        return null;
    }
}


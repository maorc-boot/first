package com.asiainfo.biapp.pec.preview.jx.enums;

import lombok.Getter;

/**
 * 营销策略用户统计任务状态枚举
 * @author: mamp
 * @date: 2024/7/18
 */
@Getter
public enum CampUserCountTaskStatusEnum {
    UNDO(0, "待执行"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "执行成功"),
    ERROR(3, "执行失败");


    /**
     * 类型编码
     */
    private int code;

    /**
     * 类型描述
     */
    private String desc;

    CampUserCountTaskStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}



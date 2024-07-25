package com.asiainfo.biapp.pec.approve.jx.common;

import lombok.Getter;

/**
 * @author : zhouyang
 * @date : 2021-11-09 09:21:47
 * 操作结果枚举类
 */
@Getter
public enum CampOperateResult {

    CAMP_OPERATE_SUCC("成功"),
    CAMP_OPERATE_ERR("失败");

    /** 操作结果 */
    private String result;

    CampOperateResult(String result) {
        this.result = result;
    }

}

package com.asiainfo.biapp.pec.approve.jx.Enum;

import lombok.Getter;

/**
 * @author : zhouyang
 * @date : 2021-11-09 09:21:47
 * 营销策划操作日志类型
 */
@Getter
public enum CampLogType {

    CAMP_CREATE(1, "创建策略"),
    CAMP_MOD(2, "修改策略"),
    CAMP_APPR(3, "提交策略审批"),
    CAMP_APPROVING(4, "策略审批流转"),
    CAMP_APPROVED(44, "审批完成"),
    CAMP_PAUSE(5, "策略暂停"),
    CAMP_DELETE(6, "策略删除"),
    CAMP_DELAY(7, "策略延期"),
    CHL_PAUSE(8, "渠道暂停"),
    CAMP_RELEASE(9, "策略发布"),
    CAMP_STOP(10, "策略停止"),
    CAMP_RESTART(11, "策略重启"),
    CHL_RESTART(12, "渠道重启"),
    CAMP_CANCEL(13, "策略终止"),
    CHL_CANCEL(14, "渠道终止"),
    DOWNLOAD(15, "下载");



    /** 状态码 */
    private Integer type;

    /** 状态描述 */
    private String desc;

    CampLogType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}

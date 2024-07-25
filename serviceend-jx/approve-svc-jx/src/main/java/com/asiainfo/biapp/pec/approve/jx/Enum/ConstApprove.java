package com.asiainfo.biapp.pec.approve.jx.Enum;

/**
 * @author mamp
 * @date 2022/9/20
 */
public interface ConstApprove {
    /**
     * 审批类型: 素材
     */
    String APPROVE_TYPE_MATERIAL = "MATERIAL";
    /**
     * 审批类型: 活动
     */
    String APPROVE_TYPE_CAMP = "IMCD";

    /**
     * 触发条件: 10085外呼类型
     */
    String TRIGGER_CALL_TYPE = "callType";

    /**
     * 10085外呼类型：营销类
     */
    String TRIGGER_CALL_NAME_1 = "营销类";

    /**
     * 10085外呼类型：调研类
     */
    String TRIGGER_CALL_NAME_2 = "调研类";
    /**
     * 10085外呼类型：调研类_地市
     */
    String TRIGGER_CALL_NAME_3 = "调研类_地市";
    /**
     * 触发条件: 10088外呼运营位类型
     */
    String TRIGGER_WH_POSITON = "whPosition";
    /**
     * 触发条件: 活动类型
     */
    String TRIGGER_CAMP_TYPE = "campType";
    /**
     * 触发条件: 渠道ID
     */
    String TRIGGER_CHANNEL_ID = "channelId";

    /**
     * 触发条件值:1
     */
    String TRIGGER_VALUE_1 = "1";
    /**
     * 触发条件值:2
     */
    String TRIGGER_VALUE_2 = "2";

    /**
     * 触发条件值:2
     */
    String TRIGGER_VALUE_3 = "3";
    /**
     * 渠道ID: 802
     */
    String CHN_802 = "802";
    /**
     * 渠道ID: 811
     */
    String CHN_811 = "811";

    /**
     * 客户通渠道id
     */
    String CHN_809 = "809";

    /**
     * 渠道运营位ID: 811
     */
    String CHN_ADIV_811_1 = "8111";
    /**
     * 扩展字段3
     */
    String COLUMN_EXT3 = "COLUMN_EXT3";

    /**
     * 策略统筹任务
     */
    String TASK = "TASK";

    /**
     * 批量审批锁前缀
     */
    String USER_BATCH_APPROVE_LOCK_PRIFIX = "USER_BATCH_APPROVE_LOCK_";


    interface SpecialNumber {
        int NEGATIVE_ONE_NUMBER = -1;

        int ZERO_NUMBER = 0;

        int ONE_NUMBER = 1;

        int TWO_NUMBER = 2;

        int THREE_NUMBER = 3;

        int FOUR_NUMBER = 4;

        int FIVE_NUMBER = 5;

        int SIX_NUMBER = 6;

        String ZERO_STRING = "0";

        String ONE_STRING = "1";

        String THREE_STRING = "3";

        String FOUR_STRING = "4";

        String FIVE_STRING = "5";

        String SIX_STRING = "6";
    }

    interface SpecialCharacter {
        String UNDER_LINE = "_";

        String SEPARATOR_LINE = "-";

        String COMMA = ",";

        String CHINESE_COMMA = "，";

        String DOLLAR = "$";
    }

    interface EnumRedisKey {
        String CEP_STATUS_MAPPING = "CEP_STATUS_MAPPING";

        int DEFAULT_CEP_STATUS = 2;
    }

}

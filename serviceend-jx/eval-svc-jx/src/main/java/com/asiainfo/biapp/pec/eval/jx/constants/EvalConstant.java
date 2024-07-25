package com.asiainfo.biapp.pec.eval.jx.constants;

/**
 * @author mamp
 * @date 2023/5/6
 */
public interface EvalConstant {
    /**
     * 首页评估数据类型： 所有
     */
    String HOME_EVAL_DATA_ALL = "M";
    /**
     * 首页评估数据类型： 当月
     */
    String HOME_EVAL_DATA_MONTH = "D";

    /**
     * 首页评估数组分组字段：STAT_TIME
     */
    //String HOME_EVAL_GROUPBY_TIME = "STAT_TIME";
    String HOME_EVAL_GROUPBY_TIME = "STAT_DATE";
    /**
     * 首页评估数组分组字段：STAT_TIME
     */
    String HOME_EVAL_GROUPBY_CHANNEL = "CHANNEL_NAME";
    /**
     * 首页评估数组分组字段：STAT_TIME
     */
    String HOME_EVAL_GROUPBY_CITY = "CITY";
}

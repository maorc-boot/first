package com.asiainfo.biapp.pec.plan.jx.dna.constant;

/**
 * description: dna相关常量
 *
 * @author: lvchaochao
 * @date: 2023/12/19
 */
public interface ConstantDNA {

    /**
     * 客群来源-dna
     */
    String CUSTOM_SOURCE_DNA = "2";

    /**
     * 客群来源-coc
     */
    String CUSTOM_SOURCE_COC = "1";

    /**
     * 客群来源-多波次
     */
    String CUSTOM_SOURCE_WAVE = "3";

    /**
     * 客群更新-日周期 redis锁前缀
     */
    String CUSTOM_DAY_UPDATE_REDIS_LOCK_PRE = "DNA_CUSTGROUP_DAY_UPDATE";

    /**
     * 客群更新-月周期 redis锁前缀
     */
    String CUSTOM_MONTH_UPDATE_REDIS_LOCK_PRE = "DNA_CUSTGROUP_MONTH_UPDATE";

    /**
     * dna客群文件名更新模板
     */
    String DNA_2_COC_CUSTOMGROUP_FILENAME = "MCD_GROUP_%s_%s.txt";

    /**
     * dna客群周期
     */
    String CUSTGROUP_CYCLE = "day";

    /**
     * dna客群日周期值
     */
    Integer CUSTGROUP_DAY_CYCLE = 3;

    /**
     * dna客群月周期值
     */
    Integer CUSTGROUP_MONTH_CYCLE = 2;

    /**
     * 待更新客群任务表状态-待执行
     */
    Integer DNA_CUSTGROUP_UPDATE_TASK_WAITING = 50;

    /**
     * 待更新客群任务表状态-中间状态
     */
    Integer DNA_CUSTGROUP_UPDATE_TASK_MIDDLE = 505;

    /**
     * 待更新客群任务表状态-执行中
     */
    Integer DNA_CUSTGROUP_UPDATE_TASK_EXECUTING = 51;

    /**
     * 待更新客群任务表状态-异常
     */
    Integer DNA_CUSTGROUP_UPDATE_TASK_EXCEPTION = 53;

    /**
     * 待更新客群任务表状态-完成
     */
    Integer DNA_CUSTGROUP_UPDATE_TASK_COMPLETED = 54;

    /**
     * dna源客群
     */
    Integer CUSTGROUP_SOURCE = 1;
}

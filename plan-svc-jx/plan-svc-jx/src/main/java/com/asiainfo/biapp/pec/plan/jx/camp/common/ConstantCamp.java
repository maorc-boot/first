package com.asiainfo.biapp.pec.plan.jx.camp.common;

/**
 * @author mamp
 * @date 2023/4/11
 */
public interface ConstantCamp {
    /**
     * 批量导入任务状态: 执行中
     */
    String IMPORT_TASK_STATUS_RUNNING = "1";
    /**
     * 批量导入任务状态: 执行完成
     */
    String IMPORT_TASK_STATUS_SUCCESS = "2";
    /**
     * 批量导入任务状态: 执行失败
     */
    String IMPORT_TASK_STATUS_ERROR = "3";


    /**
     * 活动创建类型:手动
     */
    int CAMP_CREATE_TYPE_MANUL = 1;

    /**
     * 活动创建类型:导入
     */
    int CAMP_CREATE_TYPE_IMPORT = 2;
}

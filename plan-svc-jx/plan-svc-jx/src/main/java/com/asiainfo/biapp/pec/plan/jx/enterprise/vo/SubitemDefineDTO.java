package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提供给政企的活动详情DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubitemDefineDTO {
    //原有字段
    private String subitemTarget; // 活动目的
    private String businessClass;//活动类型
    private String opposite;//对端编码(运营位：如1,2,3) --运营位id
    private String adsenseName;//运营位名称
    private String execImageTitle;//图片标题
    private String execImageUrl;//活动图片地址(访问路径)
    private String execImageServerUrl;//活动图片地址(绝对路径)
    private String execText;//活动用语;
    private String dayMask;//月执行时间
    private String weekMask;//周执行时间
    private Integer execSelfInterval;//同客户接触间隔天数
    private Integer execSelfIntervalMaxNum;//同一周期客户最大接触次数
    private Integer execOtherInterval;//其它活动最小间隔天数
    private String minute10Mask;//天执行时间
    private Integer maxExecNum;//活动周期客户最大执行次数
    private Integer onedayMaxNum;//一天内最大执行次数

    //江西扩展字段
    private String campsegId;//
    private String campsegName;//
    private String activityType;//"1",--活动类型 queryIopActivityType  businessClass
    private String activityObjective;//"3",--subitemTarget 活动目的

    private String channelId;//渠道
    private String adivId;//运营位
    private String materialId;//运营位
    private String materialName;//运营位
    private String execContent;//"营销用语--execText
    private String campType;//策略类型 ,1策略类 ,2调研类；
    private String cepEventId;//事件ID
    private String cepEventName;//事件名称
    private String cancelRecommendCycle;//取消推荐时间天数
    private String e_811_TASK_DESCRIPTION;//任务描述及要求
    private String e_811_PROJECT_LEVEL_2;//项目层级二名称
    private String e_811_PROJECT_LEVEL_3;//项目层级三ID
    private String e_811_SUB_TASK_TYPE;//项目层级三KEY
    private String e_811_CUSTGROUP_RULE;//营销(服务)目标客群提取规则
    private String e_811_SERV_FILE_NAME;//外呼需求附件
    private String e_811_SMS_CHANNEL_TYPE;//营销类型；
    private String e_811_CALL_TYPE;//运营位名称
    private String e_811_TASK_TYPE;//任务大类
    private String e_802_MARKETING;//营销用语
    private String e_802_CALL_TYPE;//外呼类型
    private String e_802_CUSTGROUP_RULE;//营销(服务)目标客户及提取原则
    private String e_802_TASK_DESCRIPTION;//任务描述及要求
    private String e_802_SERV_FILE_NAME;//外呼需求附件


}

package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略定义表
 * </p>
 *
 * @author mamp
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdCampDefJx implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 父策略ID
     */
    @TableId(value = "CAMPSEG_ID", type = IdType.ID_WORKER_STR)
    private String campsegId;

    /**
     * 策略名称
     */
    @TableField("CAMPSEG_NAME")
    private String campsegName;

    /**
     * 策略描述
     */
    @TableField("CAMPSEG_DESC")
    private String campsegDesc;

    /**
     * 开始时间
     */
    @TableField("START_DATE")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField("END_DATE")
    private Date endDate;

    /**
     * 状态
     */
    @TableField("CAMPSEG_STAT_ID")
    private Integer campsegStatId;

    /**
     * 策略类型 1:营销类；2:服务类；3:业务类；4:公益类；0：无
     */
    @TableField("CAMPSEG_TYPE_ID")
    private Integer campsegTypeId;

    /**
     * 营销类型：0：普通营销（默认）1：渠道偏好营销 2：智能营销
     */
    @TableField("MARKETING_TYPE")
    private Integer marketingType;

    /**
     * 集团活动模板ID
     */
    @TableField("ACTIVITY_TEMPLATE_ID")
    private String activityTemplateId;

    /**
     * （IOP）活动类型 1：4G产品类 2：终端类 3：流量类 4：数字化服务类 5：基础服务类 6：PCC类 7：宽带类 9：其它类
     */
    @TableField("ACTIVITY_TYPE")
    private String activityType;

    /**
     * （IOP）活动目的 1：新增客户类 2：存量保有类 3：价值提升类 4：离网预警类 9：其它类
     */
    @TableField("ACTIVITY_OBJECTIVE")
    private String activityObjective;

    /**
     * 策划人ID
     */
    @TableField("CREATE_USER_ID")
    private String createUserId;

    /**
     * 策划时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 审批流ID
     */
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;

    /**
     * 审批结果
     */
    @TableField("APPROVE_RESULT")
    private Integer approveResult;

    /**
     * 审批提醒时间
     */
    @TableField("APPROVE_REMIND_TIME")
    private Date approveRemindTime;

    /**
     * 归属区域
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 活动专题ID
     */
    @TableField("TOPIC_ID")
    private String topicId;

    /**
     * （IOP）活动业务类型 1：语音类、2：流量类、3：宽带类、4：终端类、5：数字服务类、6：客户保有类、7：入网类
     */
    @TableField("ACTIVITY_SERVICE_TYPE")
    private String activityServiceType;

    /**
     * 策略业务类型
     */
    @TableField("CAMP_BUSINESS_TYPE")
    private String campBusinessType;

    /**
     * 对应统一策略id
     */
    @TableField("UNITY_CAMPSEG_ID")
    private String unityCampsegId;

    @TableField("OPTION_SIGN")
    private String optionSign;

    /**
     * 策略定义类型含义 1：IOP活动 2:省级模板 3：省级统一策略
     */
    @TableField("CAMP_DEF_TYPE")
    private Integer campDefType;

    /**
     * 预演活动
     */
    @TableField("PREVIEW_CAMP")
    private String previewCamp;

    /**
     * 根策略ID
     */
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;

    /**
     * 多波次图数组
     */
    @TableField("TACTICS_MAP")
    private String tacticsMap;

    /**
     * 内蒙营销目标
     */
    @TableField("CAMP_TARGET")
    private String campTarget;

    /**
     * 内蒙营销跟踪号码
     */
    @TableField("CAMP_TRACK_PHONE")
    private String campTrackPhone;

    /**
     * 营销策划创建类型
     */
    @TableField("CAMP_CREATE_TYPE")
    private Integer campCreateType;

    /**
     * 营销场景id
     */
    @TableField("CAMP_SCENE")
    private String campScene;

    /**
     * 活动执行短信通知标识
     */
    @TableField("RECEIVE_SMS")
    private String receiveSms;

    /**
     * 活动复制来源策略id
     */
    @TableField("COPY_CAMPSEG_ID")
    private String copyCampsegId;

    /**
     * 是否优秀案例
     */
    @TableField("IS_EXCELLENT_CASES")
    private Boolean isExcellentCases;

    /**
     * 一级电子渠道业务类型
     */
    @TableField("ELE_BUSINESS_TYPE")
    private String eleBusinessType;

    /**
     * 一级电子渠道营销活动类型
     */
    @TableField("ELE_ACTIVITY_TYPE")
    private String eleActivityType;

    /**
     * 一级电子渠道营销活动目的
     */
    @TableField("ELE_ACTIVITY_OBJECTIVE")
    private String eleActivityObjective;

    /**
     * 一级电子渠道省审核人
     */
    @TableField("AUDIT_USER")
    private String auditUser;

    /**
     * 一级电子渠道省审核部门
     */
    @TableField("AUDIT_DEPT")
    private String auditDept;

    /**
     * 一级电子渠道省审核意见
     */
    @TableField("AUDIT_OPION")
    private String auditOpion;

    /**
     * 一级电子渠道省审核人手机
     */
    @TableField("AUDIT_PHONE")
    private String auditPhone;

    /**
     * 一级电子渠道省审核人邮箱
     */
    @TableField("AUDIT_EMAIL")
    private String auditEmail;

    /**
     * 一级电渠安全附件文件信息
     */
    @TableField("SAFETY_ACCESSORIES")
    private String safetyAccessories;

    /**
     * 一级电渠安全免责条款
     */
    @TableField("SAFETY_DISCLAIMER")
    private String safetyDisclaimer;

    /**
     * 活动级敏感客户群IDs
     */
    @TableField("SENSITIVE_CUST_IDS")
    private String sensitiveCustIds;


}

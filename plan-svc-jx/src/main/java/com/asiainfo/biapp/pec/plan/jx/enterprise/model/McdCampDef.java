package com.asiainfo.biapp.pec.plan.jx.enterprise.model;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class McdCampDef {

  private String campsegId;
  private String campsegName;
  private String campsegDesc;
  private java.sql.Timestamp startDate;
  private java.sql.Timestamp endDate;
  private long campsegStatId;
  private long campsegTypeId;
  private long marketingType;
  private String activityTemplateId;
  private String activityType;
  private String activityObjective;
  private String createUserId;
  private java.sql.Timestamp createTime;
  private String approveFlowId;
  private long approveResult;
  private java.sql.Timestamp approveRemindTime;
  private String cityId;
  private String topicId;
  private String activityServiceType;
  private String campBusinessType;
  private String unityCampsegId;
  private String optionSign;
  private long campDefType;
  private String previewCamp;
  private String campsegRootId;
  private String tacticsMap;
  private String campTarget;
  private String campTrackPhone;
  private long campCreateType;
  private String campScene;
  private String receiveSms;
  private String copyCampsegId;
  private long isExcellentCases;
  private String eleBusinessType;
  private String eleActivityType;
  private String eleActivityObjective;
  private String auditUser;
  private String auditDept;
  private String auditOpion;
  private String auditPhone;
  private String auditEmail;
  private String safetyAccessories;
  private String safetyDisclaimer;
  private String sensitiveCustIds;
  // @Transient
  @TableField(exist = false)
  private String approveResultDesc;// 审批结果描述
  // @Transient
  @TableField(exist = false)
  private String channelId;//渠道ID

  @TableField(exist = false)
  private String subitemId;//政企使用


}

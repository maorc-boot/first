package com.asiainfo.biapp.pec.plan.jx.link.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author AsiaInfo-jie
 *com.asiainfo.biapp.mcd.tactics.vo.McdCampDef
 */
@Data
public class McdCampDef {

	private String campId; // 活动编号

	private String topicId; // 活动专题ID
	
	private Short statId; // 营销活动状态ID
	private String createUserId; // 活动策划人
	private Short priId;// 活动优先级,见维表:dim_camp_pri
	private String campName;
	private String startDate; // 开始时间
	private String endDate; // 结束时间
	private String pid; // 活动编号父ID
	private Short typeId; // 活动营销类型：老字段，现在不用了；云南自己定义纬表和字段了
	private String campPreview; //是否预演
	private String userExclusion; //用户剔除 敏感用户剔除1是剔除，0是不剔除
	private String excludeGroupID;
	private String campDrvId; //营销活动类型。云南定义的，该字段可参见mcd_dim_camp_drv_id纬表
	private String isAssess; //是否考核，云南的字段
	private String isCalRemun; //是否发酬金，云南的字段

	private String planId; // 产品编码
	private Integer isFileterDisturb;// 是否免打扰
	private Integer deptId; // 策划人部门id
	private Date createTime; // 本活动定义时间
	private String createUserName;
	private String cityId; // 策划人所属地市
	private String approveFlowId; // 默认内部审批流程
	private Integer targerUserNums; // 目标客户数
	private String custListTab;// 策略初始清单表
	private Date lastRemindTime;
	
	private String campNo; // 多规则时，规则序号
	private String cepEventId; // 复杂事件ID
	private String eventRuleDesc; // 实时事件名称
	private Integer campClass; // 活动实际含义1：活动基本信息,2：规则,3：波次
	private String campsegDesc;// 活动描述
	private String deptName;//部门名称
	private String cityName;//地市名称
	private String activityTemplateId;//iop模板id
	private Integer marketingType;
	private String activityTemplateName;//iop模板名称
	private String activityType;//活动类型(针对陕西需求活动类型用于后评估扩展)
	private String mcdCampType;//场景类型(针对陕西需求活动类型用于后评估扩展)
	private String localActivityType;//活动类型(针对各省本地的策略类型)
	private String activityTypeName; //活动类型名称
	private String needUserName; //需求人（针对陕西需求）
	private String removeCampsegIds;//剔除活动的Id
	private String removeCampsegNames;//剔除活动的名称
	private String activityObjective;//活动目的
	private String attachment; //附件
	private String filterConfig; // 营销配置（针对陕西的需求）
	private String optionSign; // 策略创建标志
	private String campCreateType;//策略创建方式 0-手动；1-导入
	private Integer unlimitedCustomerGroup;//不限定客户群，0：限定，1不限定,默认限定
	private String unityCampType;//策略类型  0-全部策略；1-统一策略
	private String activityObjectiveName; //活动目的名称
	private Integer pageNum; // 当前页
	private Integer isSelectMy; // 查询的是否是自己的 0 ：是 1 ：不是
	private Boolean isZJ;// 是否是浙江
	private Integer isMy;// 是否是自己创建的 0 ：是 1 ：不是
	private String channelId;
	private String keywords;// 关键字
	private boolean isFatherNode; // 是否为策略基础信息；true:是基本信息；false:子规则
	private String isApprove;
	private String custgroupId; // 客户群
	private String custCycle; // 客户群周期
	private List<McdCampChannelList> mtlChannelDefList; // 渠道执行
	private List<Map<String, Object>> mtlChannelDefListExt; // 渠道扩展信息
	private List<McdCampMaterial> mcdCampMaterialList; // 策略渠道运营位素材关系
	private List<McdCampAdivInfo> mcdCampAdivInfoList;//渠道多运营位
	private Short approveResult; // 审批结果
	private String approveResultDesc;// 审批结果描述
	private String[] evaluationPlanIds; //效果评估所选中产品id
	private String[] fixPlanIds; //融合产品所选中产品id
	/********* 热点内容营销 针对树形结果回想 在活动信息model添加如下三级********/
	private String productClass;		//0级
	private String firstBusinessClass;		//一级
	private String secondBusinessClass;		//二级
	private String thirdBusinessClass;		//三级
	/********* 热点内容营销 针对树形结果回想 在活动信息model添加如下三级********/
	
	/********* 策略管理列表按活动开始和结束时间段查询********/
	private String campStartBegin;		//活动开始
	private String campStartOver;		//活动开始
	private String campEndBegin;		//活动终止
	private String campEndOver;		//活动终止
	/********* 策略管理列表按活动开始和结束时间段查询********/
	/**波次json信息**/
	private String channleInfoWaves;
	private Integer waveNO;
	private String waveRuleExp;
	private String planWords; //产品id/名称
	//当前用户所属地市，仅用于省级重点策略
	private String loginCityId;
	private String unityCampsegId;//引用的统一策略id   ---统一策略
	private String hasExcluded;//是否剔除客户群    ---统一策略
	private String isUnityStrategy;//是否引用     ---统一策略

	private String sceneId;//引用的场景ID
	private String sceneName;//引用的场景名称
	private String modelId;
	private String modelName;
	private String baseCustcampFlag;

	private String statusName;

	// 是否引用事件
	private String eventType;

	// 事件名称
	private String eventName;

}
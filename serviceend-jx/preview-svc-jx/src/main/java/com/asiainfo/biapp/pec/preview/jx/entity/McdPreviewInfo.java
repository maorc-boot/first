package com.asiainfo.biapp.pec.preview.jx.entity;

import com.asiainfo.biapp.pec.core.model.PlanDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 预演信息
 */
@Data
public class McdPreviewInfo {
    /**
     * 任务id
     */
    private Integer targetUserNum;
    /**
     * 子策略id
     */
    private String campsegId;
    /**
     * 子活动id
     */
    private String campsegPid;
    /**
     * 主活动id
     */
    private String campsegRootId;
    /**
     * 子活动名称
     */
    private String campsegName;
    /**
     * 活动开始时间
     */
    private String startDate;
    /**
     * 活动结束时间
     */
    private String endDate;
    /**
     * 活动创建时间
     */
    private String createTime;
    /**
     * 地市id
     */
    private String cityId;

    /**
     * 活动级敏感客户群IDs
     */
    private String sensitiveCustIds;
    /**
     * 渠道id
     */
    private String channelId;
    /**
     * 渠道运营位id
     */
    private String channelAdivId;
    /**
     * 推荐用语
     */
    private String execContent;
    /**
     * 产品id
     */
    private String planId;
    /**
     * 产品名称
     */
    private String planName;
    /**
     * 产品类型
     */
    private String planType;
    /**
     * 产品类型 单产品
     */
    private String planSrvType;
    /**
     * 产品描述
     */
    private String planDesc;
    /**
     * 最新客户群id
     */
    private String customGroupId;
    /**
     * 活动营销类型
     */
    private Integer marketingType;
    /**
     * 短信用语
     */
    private String smsContent;
    /**
     * 办理地址
     */
    private String handleUrl;
    /**
     * 素材地址/图片地址（完整的资源地址）
     */
    private String materialDisplayUrl;
    /**
     * 素材跳转地址
     */
    private String materialClickUrl;
    /**
     * 是否渠道偏好, 1-是 ,0-否
     */
    private String isChannelPredilection;

    /**
     * 是否有替换符
     */
    private Integer ifHaveVar;

    /**
     * 策略类型 1:营销类；2:服务类；3:业务类；4:公益类；0：无
     */
    private Integer campsegTypeId;
    /**
     * 策略实际含义 1：活 动基本信息 2：规则 3：波次
     */
    private Integer campClass;

    /**
     * PLAN_PID 产品父ID
     */
    private String parentId;
    /**
     * 产品默认推荐用语
     */
    private String planComment;
    /**
     * 产品生效时间
     */
    private Date planStartDate;

    /**
     * 产品失效时间
     */
    private Date planEndDate;

    /**
     * 产品状态 0:未上线 1:已上线 2:待确认 3：待添加 4：已添加  #ONLINE_STATUS
     */
    private Integer status;
    /**
     * 产品创建人  #CREATE_USERID
     */
    private String createUserId;

    /**
     * 产品创建时间 #CREATE_DATE
     */
    private Date createDate;

    /**
     * 优先级
     */
    private Integer priorityOrder;

    /**
     * （IOP）活动类型 1：4G产品类 2：终端类 3：流量类 4：数字化服务类 5：基础服务类 6：PCC类 7：宽带类 9：其它类
     */
    private String activityType;


    /**
     * 扩展信息
     */
    private String ext1;
    /**
     * 扩展信息
     */
    private String ext2;
    /**
     * 扩展信息
     */
    private String ext3;
    /**
     * 扩展信息
     */
    private String ext4;
    /**
     * 扩展信息
     */
    private String ext5;
    /**
     * 扩展信息
     */
    private String ext6;
    /**
     * 扩展信息
     */
    private String ext7;
    /**
     * 扩展信息
     */
    private String ext8;
    /**
     * 扩展信息
     */
    private String ext9;
    /**
     * 扩展信息
     */
    private String ext10;
    /**
     * 扩展信息
     */
    private String ext11;
    /**
     * 扩展信息
     */
    private String ext12;
    /**
     * 扩展信息
     */
    private String ext13;
    /**
     * 扩展信息
     */
    private String ext14;
    /**
     * 扩展信息
     */
    private String ext15;
    /**
     * 扩展信息
     */
    private String ext16;
    /**
     * 扩展信息
     */
    private String ext17;
    /**
     * 扩展信息
     */
    private String ext18;
    /**
     * 扩展信息
     */
    private String ext19;
    /**
     * 扩展信息
     */
    private String ext20;

    private List<PlanDetail> planList;

    /**内蒙专用字段*/
    /**
     * 子活动描述
     */
    private String campsegDesc;
    /**
     * 主活动描述
     */
    private String campsegRootDesc;
    /**
     * 主活动名称
     */
    private String campsegRootName;
    /**
     * 营销场景
     */
    private String campScene;
    /**
     * 营销场景大类
     */
    private String campBigScene;
    /**
     * 营销目标
     */
    private String campTarget;
    /**
     * 营销跟踪号码
     */
    private String campTrackPhone;
    /**
     * 渠道名单过滤，勾选的enumid用逗号拼接，取值enum_type=channel_filter_user_type
     */
    private String userFilterRule;
    /**
     * 预演客户群数量
     */
    private Integer previewCustNum;
    /**
     * 触发成功率，百分号前面的数据
     */
    private Integer previewSuccRate;
    /**
     * 客户群规模
     */
    private Integer custNum;
    /**
     * 子策略营销成功率，百分号前面的数据
     */
    private Integer succRate;
    /**
     * 子策略事件id
     */
    private String eventId;
    /**
     * crm渠道售中售后产品
     */
    private String crmPlanId;
    /**内蒙专用字段*/

    /**海南专用字段*/
    /**
     * 内容标题，业务标题
     */
    private String pushTitle;
    /**
     * 预计营销客户数
     */
    private String expectMarketNum;
    /**
     * 营销缩略语
     */
    private String marketingTerm;
    /**
     * 海南活动类型
     */
    private String campType;
    /**海南专用字段*/


    /**
     * 省份个性化字段通用字段 start
     */
    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private String attr5;
    private String attr6;
    private String attr7;
    private String attr8;
    private String attr9;
    private String attr10;
    private String attr11;
    private String attr12;
    private String attr13;
    private String attr14;
    private String attr15;
    private String attr16;
    private String attr17;
    private String attr18;
    private String attr19;
    private String attr20;
    private String attr21;
    private String attr22;
    private String attr23;
    private String attr24;
    private String attr25;
    private String attr26;
    private String attr27;
    private String attr28;
    private String attr29;
    private String attr30;

}

package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 策略定义表
 * </p>
 *
 * @author imcd
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_camp_def")
@ApiModel(value = "McdCampDef对象", description = "策略定义表")
public class McdCampDef extends Model<McdCampDef> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父策略ID")
    @TableId("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "根策略ID")
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;

    @ApiModelProperty(value = "策略名称")
    @TableField("CAMPSEG_NAME")
    private String campsegName;

    @ApiModelProperty(value = "策略描述")
    @TableField("CAMPSEG_DESC")
    private String campsegDesc;

    @ApiModelProperty(value = "开始时间")
    @TableField("START_DATE")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @TableField("END_DATE")
    private Date endDate;

    @ApiModelProperty(value = "状态")
    @TableField("CAMPSEG_STAT_ID")
    private Integer campsegStatId;

    @ApiModelProperty(value = "策略类型 1:策略类；2:调用类")
    @TableField("CAMPSEG_TYPE_ID")
    private Integer campsegTypeId;

    @ApiModelProperty(value = "营销类型：0：普通营销（默认）1：渠道偏好营销 2：智能营销")
    @TableField("MARKETING_TYPE")
    private Integer marketingType;

    @ApiModelProperty(value = "集团活动模板ID")
    @TableField("ACTIVITY_TEMPLATE_ID")
    private String activityTemplateId;

    @ApiModelProperty(value = "（IOP）活动类型 1：4G产品类 2：终端类 3：流量类 4：数字化服务类 5：基础服务类 6：PCC类 7：宽带类 9：其它类")
    @TableField("ACTIVITY_TYPE")
    private String activityType;

    @ApiModelProperty(value = "（IOP）活动目的 1：新增客户类 2：存量保有类 3：价值提升类 4：离网预警类 9：其它类")
    @TableField("ACTIVITY_OBJECTIVE")
    private String activityObjective;

    @ApiModelProperty(value = "策划人ID")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty(value = "策划时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "审批流ID")
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;

    @ApiModelProperty(value = "审批结果")
    @TableField("APPROVE_RESULT")
    private Integer approveResult;

    @ApiModelProperty(value = "审批提醒时间")
    @TableField("APPROVE_REMIND_TIME")
    private Date approveRemindTime;

    @ApiModelProperty(value = "归属区域")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "活动专题ID")
    @TableField("TOPIC_ID")
    private String topicId;

    @ApiModelProperty(value = "（IOP）活动业务类型 1：语音类、2：流量类、3：宽带类、4：终端类、5：数字服务类、6：客户保有类、7：入网类")
    @TableField("ACTIVITY_SERVICE_TYPE")
    private String activityServiceType;

    @ApiModelProperty(value = "策略业务类型")
    @TableField("CAMP_BUSINESS_TYPE")
    private String campBusinessType;

    @ApiModelProperty(value = "对应统一策略id")
    @TableField("UNITY_CAMPSEG_ID")
    private String unityCampsegId;

    @TableField("OPTION_SIGN")
    private String optionSign;

    @ApiModelProperty(value = "策略定义类型含义 1：IOP活动 2:省级模板 3：省级统一策略")
    @TableField("CAMP_DEF_TYPE")
    private Integer campDefType;

    @ApiModelProperty(value = "预演策略")
    @TableField("PREVIEW_CAMP")
    private String previewCamp;

    @ApiModelProperty(value = "策略地图")
    @TableField("TACTICS_MAP")
    private String tacticsMap;

    @ApiModelProperty(value = "营销策划创建类型")
    @TableField("CAMP_CREATE_TYPE")
    private Integer campCreateType;

    @ApiModelProperty(value = "是否优秀案例")
    @TableField("IS_EXCELLENT_CASES")
    private Boolean isExcellentCases;

    // @Transient
    @TableField(exist = false)
    private String approveResultDesc;// 审批结果描述
    // @Transient
    @TableField(exist = false)
    private String channelId;//渠道ID

    @TableField(exist = false)
    private String subitemId;//政企使用

    @Override
    protected Serializable pkVal() {
        return this.campsegId;
    }

}

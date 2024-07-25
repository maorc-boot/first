package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.enums.TransEnum;
import com.asiainfo.biapp.pec.core.enums.TransPropEnum;
import com.asiainfo.biapp.pec.plan.jx.camp.enums.CampStatusJx;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author mamp
 * @date 2022/10/26
 */
@Data
@ToString
@TransEnum(clz = CampStatusJx.class, type = {"activity_type", "activity_objective"})
public class CampBaseInfoJxVO extends CampBaseInfoVO {

    @ApiModelProperty(value = "根策略ID")
    private String campsegRootId;

    @ApiModelProperty(value = "父策略ID")
    private String campsegId;

    @ApiModelProperty(value = "策略名称")
    private String campsegName;

    @ApiModelProperty(value = "策略描述")
    private String campsegDesc;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "状态")
    @TransPropEnum(type = "CampStatusJx", keyCol = "campsegStatName")
    private Integer campsegStatId;

    @ApiModelProperty(value = "状态名称")
    private String campsegStatName;

    @ApiModelProperty(value = "策略类型 1:营销类；2:服务类；3:业务类；4:公益类；0：无")
    private Integer campsegTypeId;

    @ApiModelProperty(value = "营销类型：0：普通营销（默认）1：渠道偏好营销 2：智能营销")
    private Integer marketingType;

    @ApiModelProperty(value = "集团活动模板ID")
    private String activityTemplateId;

    @ApiModelProperty(value = "（IOP）活动类型 1：4G产品类 2：终端类 3：流量类 4：数字化服务类 5：基础服务类 6：PCC类 7：宽带类 9：其它类")
    @TransPropEnum(type = "activity_type", keyCol = "activityTypeName")
    private String activityType;

    @ApiModelProperty(value = "（IOP）活动目的 1：新增客户类 2：存量保有类 3：价值提升类 4：离网预警类 9：其它类")
    @TransPropEnum(type = "activity_objective", keyCol = "activityObjectiveName")
    private String activityObjective;

    @ApiModelProperty(value = "（IOP）活动类型 1：4G产品类 2：终端类 3：流量类 4：数字化服务类 5：基础服务类 6：PCC类 7：宽带类 9：其它类")
    private String activityTypeName;

    @ApiModelProperty(value = "（IOP）活动目的 1：新增客户类 2：存量保有类 3：价值提升类 4：离网预警类 9：其它类")
    private String activityObjectiveName;

    @ApiModelProperty(value = "策划人ID")
    private String createUserId;

    @ApiModelProperty(value = "策划人")
    private String createUserName;

    @ApiModelProperty(value = "策划时间")
    private Date createTime;

    @ApiModelProperty(value = "审批流ID")
    private String approveFlowId;

    @ApiModelProperty(value = "审批结果")
    private Integer approveResult;

    @ApiModelProperty(value = "审批提醒时间")
    private Date approveRemindTime;

    @ApiModelProperty(value = "归属区域")
    private String cityId;

    @ApiModelProperty(value = "归属区域名称")
    private String cityName;

    @ApiModelProperty(value = "活动专题ID")
    private String topicId;

    @ApiModelProperty(value = "（IOP）活动业务类型 1：语音类、2：流量类、3：宽带类、4：终端类、5：数字服务类、6：客户保有类、7：入网类")
    private String activityServiceType;

    @ApiModelProperty(value = "策略业务类型")
    private String campBusinessType;

    @ApiModelProperty(value = "对应统一策略id")
    private String unityCampsegId;

    @ApiModelProperty(value = "optionSign")
    private String optionSign;

    @ApiModelProperty(value = "策略定义类型含义 1：IOP活动 2:省级模板 3：省级统一策略")
    private Integer campDefType;

    @ApiModelProperty("预演策略 1是0否")
    private String previewCamp;

    @ApiModelProperty("策略地图")
    private String tacticsMap;

    @ApiModelProperty(value = "创建类型", hidden = true)
    private Integer campCreateType;

    @ApiModelProperty(value = "敏感客户群IDs")
    private String  sensitiveCustIds;

    @ApiModelProperty(value = "场景")
    private String campScene;

    @ApiModelProperty(value = "客群来源 0-coc客群 1-DNA客群")
    private Integer custgroupSource;

    @ApiModelProperty(value = "dna客群时对应的执行周期 1：一次性，2：月周期，3：日周期")
    private Integer dnaUpdateCycle;
}

package com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity;

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
 * 智推棱镜AI推理场景我关注的产品表实体对象
 *
 * @author lvcc
 * @date 2024/06/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MCD_PRISM_MY_ATTENTION_PLAN")
@ApiModel(value = "McdPrismMyAttentionPlan对象", description = "")
public class McdPrismMyAttentionPlan extends Model<McdPrismMyAttentionPlan> {

    private static final long serialVersionUID = 1L;

    // @ApiModelProperty(value = "ID")
    // @TableField("ID")
    // private String id;

    @ApiModelProperty(value = "产品编码")
    @TableId("PLAN_ID")
    private String planId;

    @ApiModelProperty(value = "产品名称")
    @TableField("PLAN_NAME")
    private String planName;

    @ApiModelProperty(value = "产品类别 1：流量 2：新增 3：终端 4：两网 5：宽带 6：存量 7：政企 9：其他 10：手机应用 999：虚拟类政策")
    @TableField("PLAN_TYPE")
    private String planType;

    @ApiModelProperty(value = "产品类型 1：单产品 2：政策 3：规则 4：内容")
    @TableField("PLAN_SRV_TYPE")
    private String planSrvType;

    @ApiModelProperty(value = "产品状态 2:待确认  3：待添加    4：已添加")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "产品生效时间")
    @TableField("PLAN_START_DATE")
    private Date planStartDate;

    @ApiModelProperty(value = "产品失效时间")
    @TableField("PLAN_END_DATE")
    private Date planEndDate;

    @ApiModelProperty(value = "默认推荐用语")
    @TableField("PLAN_COMMENT")
    private String planComment;

    @ApiModelProperty(value = "产品描述")
    @TableField("PLAN_DESC")
    private String planDesc;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value = "数据来源 1：全网标准化产品 2：咪咕产品（数字内容产品） 3：互联网产品 4：省公司产品")
    @TableField("DATA_SOURCE")
    private Integer dataSource;

    @ApiModelProperty(value = "计费方式1：CPC 2：CPM")
    @TableField("FEE_MODEL")
    private Integer feeModel;

    @ApiModelProperty(value = "费用描述")
    @TableField("FEE_DESC")
    private String feeDesc;

    @ApiModelProperty(value = "iop省级IOP上报省级营销活动所需要产品类型")
    @TableField("IOP_PLAN_TYPE")
    private String iopPlanType;

    @ApiModelProperty(value = "集团统一编码")
    @TableField("IOP_PREFIX_CODE")
    private String iopPrefixCode;

    @ApiModelProperty(value = "父产品ID")
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "负责人")
    @TableField("ADMIN_USER_ID")
    private String adminUserId;

    @ApiModelProperty(value = "被授权人")
    @TableField("AUTHORIZED_USER_ID")
    private String authorizedUserId;

    @ApiModelProperty(value = "产品促销资源状态  2：待确认，3：已确认，4：已添加资源")
    @TableField("PRO_STATUS")
    private Integer proStatus;

    @ApiModelProperty(value = "资费")
    @TableField("PRODUCT_CHARGES")
    private String productCharges;

    @ApiModelProperty(value = "优先级")
    @TableField("PRODUCT_PRIORITY")
    private String productPriority;

    @ApiModelProperty(value = "是否优选产品 0：否，1：是")
    @TableField("IS_OPTIMIZATION")
    private Integer isOptimization;

    @ApiModelProperty(value = "外部系统ID(营销案ID)")
    @TableField("OUT_SYSTEM_ID")
    private String outSystemId;

    @ApiModelProperty(value = "省份编码")
    @TableField("PROVINCE_CODE")
    private String provinceCode;

    @ApiModelProperty(value = "特殊营销产品类型 0:普通营销产品 1:政企营销产品")
    @TableField("PLAN_CLASS")
    private Integer planClass;

    @ApiModelProperty(value = "产品分类（业务）")
    @TableField("PROD_CLASS_BUSI")
    private String prodClassBusi;

    @ApiModelProperty(value = "商品类型")
    @TableField("OFFER_TYPE")
    private String offerType;

    @ApiModelProperty(value = "产商品类型")
    @TableField("ITEM_TYPE")
    private String itemType;

    @Override
    protected Serializable pkVal() {
        return this.planId;
    }

}

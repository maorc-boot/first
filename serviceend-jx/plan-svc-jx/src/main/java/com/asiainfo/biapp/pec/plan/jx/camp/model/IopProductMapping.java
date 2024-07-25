package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 集团和省级产品映射表，集团提供
 * </p>
 *
 * @author mamp
 * @since 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IopProductMapping implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 集团对应的统一产品编码
     */
    @TableField("GOODS_ID")
    private String goodsId;

    /**
     * 本省对应的产品编码
     */
    @TableField("PRODUCT_ITEM_ID")
    private String productItemId;

    /**
     * 产品系列编码
     */
    @TableField("CHARGESET_CODE")
    private String chargesetCode;


}

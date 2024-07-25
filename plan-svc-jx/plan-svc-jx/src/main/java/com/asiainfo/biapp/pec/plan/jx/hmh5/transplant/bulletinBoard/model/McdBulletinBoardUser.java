package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class McdBulletinBoardUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公告编号(最长64字符)
     */
    @TableField("BULLETIN_CODE")
    private String bulletinCode;

    /**
     * 客户id(最长64字符)
     */
    @TableField("USERID")
    private String userid;

    /**
     * 当前公告用户访问状态 0-未访问 1-已访问
     */
    @TableField("STATUS")
    private Integer status;


}

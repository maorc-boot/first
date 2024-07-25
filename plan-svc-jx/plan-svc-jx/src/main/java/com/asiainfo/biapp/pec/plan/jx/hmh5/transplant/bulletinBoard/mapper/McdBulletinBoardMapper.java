package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.mapper;

import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.SelectBulletinBoardParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.model.McdBulletinBoard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 江西客户通公告栏表 Mapper 接口
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdBulletinBoardMapper extends BaseMapper<McdBulletinBoard> {
    Page selectAllBulletins(Page page,@Param("bulletinParam")SelectBulletinBoardParam bulletinParam);

}

package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.core.enums.KeyWordsEnum;
import com.asiainfo.biapp.pec.plan.jx.camp.model.CampPriorityOrderJx;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampChannelListJx;
import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.asiainfo.biapp.pec.plan.vo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 策略执行基础属性表 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2021-11-12
 */
@Mapper
public interface McdCampChannelListJxDao extends BaseMapper<McdCampChannelListJx> {


    /**
     * 获取优先级策略个数
     *
     * @return
     */
    List<PriorityChannel> queryPriorityChannel(@Param("channelType") String channelType);


    IPage<CampPriorityOrderJx> pagePriorityOrderJx(Page<?> pager,
                                                   @Param("channelId") String channelId,
                                                   @Param("adivId") String adivId,
                                                   @Param("keyWord") @KeyWordsEnum(cols = {"mcd.CAMPSEG_NAME", "mccl.CAMPSEG_ID"}) String keyWord);
}

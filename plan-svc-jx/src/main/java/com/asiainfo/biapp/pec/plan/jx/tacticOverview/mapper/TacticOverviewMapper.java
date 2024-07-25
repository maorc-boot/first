package com.asiainfo.biapp.pec.plan.jx.tacticOverview.mapper;

import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo.Channel;
import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo.CustomerGroup;
import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TacticOverviewMapper {

    List<Product> selectAllProducts(
            @Param("campsegStatId") String campsegStatId,
            @Param("cityId") String cityId
    );

    List<CustomerGroup> selectAllCustomerGroups(
            @Param("campsegStatId") String campsegStatId,
            @Param("cityId") String cityId
    );


    /*
     * @param campsegStatId: 策略的状态id
     * @param cityId: 城市id，空值时默认查全省
     * @return List<Channel>:
     * @author 11437
     * @description 查询策略所属的渠道分类，仅仅查询渠道分类映射表mcd_camp_channel_classify_mapper中相关联的渠道id
     * @date 2023/6/10 22:18
     */
    List<Channel> selectAllChannels(@Param("campsegStatId") String campsegStatId, @Param("cityId") String cityId);
}

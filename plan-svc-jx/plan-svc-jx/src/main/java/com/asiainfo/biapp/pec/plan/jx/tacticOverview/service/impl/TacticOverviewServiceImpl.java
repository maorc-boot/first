package com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl;

import com.asiainfo.biapp.pec.plan.jx.tacticOverview.mapper.TacticOverviewMapper;
import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.TacticOverviewService;
import com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl.resultInfo.*;
import com.asiainfo.biapp.pec.plan.model.McdSysCity;
import com.asiainfo.biapp.pec.plan.service.IMcdSysCityService;
import org.apache.axis2.addressing.AddressingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.tacticOverview.service.impl
 * @className: TacticOverviewServiceImpl
 * @author: chenlin
 * @description: 查询全局业务概览
 * @date: 2023/6/8 12:58
 * @version: 1.0
 */
@Service
public class TacticOverviewServiceImpl implements TacticOverviewService {

    @Autowired
    private IMcdSysCityService cityService;
    @Autowired
    private TacticOverviewMapper tacticOverviewMapper;


    //value = "tacticOverviewCache@1800" @后的数值就是缓存的有效时间，单位：秒，可以为乘法表达式，如：30*60
    @Cacheable(value = "tacticOverviewCache@30*60", key = "'TACTIC_OVERVIEW_'+#campsegStatId+'_'+#cityId")
    @Override
    public SelectTacticOverviewResultInfo selectAllTactics(String campsegStatId, String cityId) {
        McdSysCity city = cityService.getById(cityId);
        //因数据库此字段的默认值为“0”，可能出现省公司父id为“0”的情况，目前为"-1"
        if (String.valueOf(city.getParentId()).equals("-1")  || String.valueOf(city.getParentId()).equals("0")) {
            //将cityId置为null，后续查询当cityId为null时，默认查全部
            cityId = null;
        }
        //查产品分类
        List<Product> products = tacticOverviewMapper.selectAllProducts(campsegStatId, cityId);
        //查客群分类
        List<CustomerGroup> customerGroups = tacticOverviewMapper.selectAllCustomerGroups(campsegStatId, cityId);
        //查渠道分类
        List<Channel> channels = tacticOverviewMapper.selectAllChannels(campsegStatId, cityId);

        return new SelectTacticOverviewResultInfo(
                Arrays.asList(new Ascription(city.getCityName(), products.stream().mapToInt(Product::getProductCount).sum())),
                products,
                customerGroups,
                channels
        );
    }
}

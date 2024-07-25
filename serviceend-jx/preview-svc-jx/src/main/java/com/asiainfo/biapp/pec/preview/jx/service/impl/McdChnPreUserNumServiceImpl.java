package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.asiainfo.biapp.pec.preview.jx.mapper.McdChnPreUserNumMapper;
import com.asiainfo.biapp.pec.preview.jx.service.McdChnPreUserNumService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户群渠道偏好用户数统计表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-09-30
 */
@Service
@Slf4j
public class McdChnPreUserNumServiceImpl extends ServiceImpl<McdChnPreUserNumMapper, McdChnPreUserNum> implements McdChnPreUserNumService {

    @Resource
    private McdChnPreUserNumMapper chnPreUserNumMapper;

    /**
     * 根据客户群ID查询渠道偏好数据
     *
     * @param custgroupId 客户群ID
     * @param preLevel    偏好级别（1：第一偏好，2：第二偏好）
     * @return
     */
    @Override
    public List<McdChnPreUserNum> queryPreData(String custgroupId, Integer preLevel) {

        // 获取偏好数据最新数据日期
        QueryWrapper<McdChnPreUserNum> queryMaxDataDateWrapper = new QueryWrapper<>();
        queryMaxDataDateWrapper.lambda()
                .select(McdChnPreUserNum::getDataDate)
                .eq(McdChnPreUserNum::getCustgroupId, custgroupId)
                .eq(McdChnPreUserNum::getPreLevel, preLevel)
                .groupBy(McdChnPreUserNum::getDataDate)
                .orderByDesc(McdChnPreUserNum::getDataDate);
        List<McdChnPreUserNum> dataDateList = this.list(queryMaxDataDateWrapper);
        if (CollectionUtils.isEmpty(dataDateList)) {
            return new ArrayList<>();
        }
        String maxDataDate = dataDateList.get(0).getDataDate();

        List<McdChnPreUserNum> mcdChnPreUserNums = chnPreUserNumMapper.queryPreData(custgroupId, preLevel, maxDataDate);
        if (CollectionUtil.isEmpty(mcdChnPreUserNums)) {
            return new ArrayList<>();
        }
        return mcdChnPreUserNums.stream().filter(num -> num.getUserNum() != null && num.getUserNum() > 0).collect(Collectors.toList());
    }
}

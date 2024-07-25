package com.asiainfo.biapp.pec.preview.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustConf;
import com.asiainfo.biapp.pec.preview.jx.entity.McdChannelSensitiveCustInfo;
import com.asiainfo.biapp.pec.preview.jx.mapper.McdChannelSensitiveCustConfMapper;
import com.asiainfo.biapp.pec.preview.jx.query.SensitiveCustQuery;
import com.asiainfo.biapp.pec.preview.jx.service.McdChannelSensitiveCustConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 渠道敏感客户群配置表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-10-12
 */
@Service
@Slf4j
public class McdChannelSensitiveCustConfServiceImpl extends ServiceImpl<McdChannelSensitiveCustConfMapper, McdChannelSensitiveCustConf> implements McdChannelSensitiveCustConfService {

    @Resource
    private McdChannelSensitiveCustConfMapper sensitiveCustConfMapper;

    @Override
    public boolean saveChannelSensitiveCustConf(List<McdChannelSensitiveCustConf> confList) {
        String userId = UserUtilJx.getUserId();
        Set<String> custgroupSet = new HashSet<>();
        confList.stream().forEach(conf -> {
            custgroupSet.add(conf.getCustgroupId());
        });
        // 先删除原来的配置
        custgroupSet.stream().forEach(custgroupId -> {
            QueryWrapper<McdChannelSensitiveCustConf> wrapper = new QueryWrapper();
            wrapper.lambda().eq(McdChannelSensitiveCustConf::getCustgroupId, custgroupId);
            this.remove(wrapper);
        });
        // 再保存新的配置
        confList.stream().forEach(conf -> {
            Date now = new Date();
            conf.setCreateUserId(userId);
            conf.setUpdateUserId(userId);
            conf.setUpdateTime(now);
            conf.setCreateTime(now);
        });
        return this.saveBatch(confList);
    }

    /**
     * 查询渠道敏感客户群信息
     *
     * @param query 查询参数
     * @return
     */
    @Override
    public IPage<McdChannelSensitiveCustInfo> querySensitiveCustInfo(SensitiveCustQuery query) {

        Page<McdChannelSensitiveCustInfo> page = new Page<>(query.getCurrent(), query.getSize());
        String keyWords = StrUtil.isEmpty(query.getKeyWords()) ? null : query.getKeyWords();
        return sensitiveCustConfMapper.querySensitiveCustInfo(page, query.getChannelId(), keyWords,query.getCustgroupType());
    }


}

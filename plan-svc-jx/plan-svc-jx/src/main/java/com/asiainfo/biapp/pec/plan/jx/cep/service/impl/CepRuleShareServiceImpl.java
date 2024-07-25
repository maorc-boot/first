package com.asiainfo.biapp.pec.plan.jx.cep.service.impl;

import com.asiainfo.biapp.pec.plan.jx.cep.dao.CepRuleShareMapper;
import com.asiainfo.biapp.pec.plan.jx.cep.model.CepRuleShare;
import com.asiainfo.biapp.pec.plan.jx.cep.service.CepRuleShareService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 事件共享表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2023-02-20
 */
@Service
public class CepRuleShareServiceImpl extends ServiceImpl<CepRuleShareMapper, CepRuleShare> implements CepRuleShareService {

    /**
     * 保存或者更新共享信息
     *
     * @param shareInfos
     * @return
     */
    @Override
    public boolean saveOrUpdate(List<CepRuleShare> shareInfos) {
        QueryWrapper<CepRuleShare> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CepRuleShare::getEventId, shareInfos.get(0).getEventId());
        // 先删除再插入
        this.remove(queryWrapper);
        return this.saveBatch(shareInfos);
    }
}

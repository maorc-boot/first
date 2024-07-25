package com.asiainfo.biapp.pec.plan.jx.hisdata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.plan.jx.hisdata.dao.CmpApproveProcessRecordHisMapper;
import com.asiainfo.biapp.pec.plan.jx.hisdata.model.CmpApproveProcessRecordHis;
import com.asiainfo.biapp.pec.plan.jx.hisdata.service.CmpApproveProcessRecordHisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 4.0策略审批记录 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2023-05-23
 */
@Service
public class CmpApproveProcessRecordHisServiceImpl extends ServiceImpl<CmpApproveProcessRecordHisMapper, CmpApproveProcessRecordHis> implements CmpApproveProcessRecordHisService {
    @Resource
    private CmpApproveProcessRecordHisMapper mapper;

    @Override
    public List<CmpApproveProcessRecordHis> listRecord(String campsegRootId) {
        List<CmpApproveProcessRecordHis> records = new ArrayList<>();
        // 查询开始节点
        CmpApproveProcessRecordHis recordHis = mapper.selectCreateRecord(campsegRootId);
        if (null == recordHis) {
            return records;
        }
        // 活动状态
        String status = recordHis.getSubmitStatus();
        recordHis.setSubmitStatus("1");
        records.add(recordHis);
        CmpApproveProcessRecordHis end = new CmpApproveProcessRecordHis();
        BeanUtil.copyProperties(recordHis, end, true);
        QueryWrapper<CmpApproveProcessRecordHis> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CmpApproveProcessRecordHis::getBusinessId, campsegRootId).orderByAsc(CmpApproveProcessRecordHis::getCreateDate);
        List<CmpApproveProcessRecordHis> list = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            return records;
        }
        for (CmpApproveProcessRecordHis cmpApproveProcessRecordHis : list) {
            recordHis.setNextNodesApprover(cmpApproveProcessRecordHis.getNodeId());
            recordHis = cmpApproveProcessRecordHis;
            records.add(cmpApproveProcessRecordHis);
        }
        if("54,59,90,91".contains(status)){
            recordHis.setNextNodesApprover("node-end");
            end.setNodeId("node-end");
            end.setNodeName("结束");
            end.setDealOpinion("审批完成");
            end.setCreateDate(recordHis.getCreateDate());
            end.setNextNodesApprover("无");
            records.add(end);
        }
        return records;

    }
}

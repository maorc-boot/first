package com.asiainfo.biapp.pec.eval.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.eval.jx.dao.DwKehutongClInfoMapper;
import com.asiainfo.biapp.pec.eval.jx.model.DwKehutongClInfo;
import com.asiainfo.biapp.pec.eval.jx.req.CityCampPageQuery;
import com.asiainfo.biapp.pec.eval.jx.service.DwKehutongClInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分策略分地市分渠道报表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-12-19
 */
@Service
@Slf4j
public class DwKehutongClInfoServiceImpl extends ServiceImpl<DwKehutongClInfoMapper, DwKehutongClInfo> implements DwKehutongClInfoService {

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @Override
    public IPage<DwKehutongClInfo> queryPage(CityCampPageQuery query) {
        QueryWrapper<DwKehutongClInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StrUtil.isNotEmpty(query.getStatDate()), DwKehutongClInfo::getStatDate, query.getStatDate());
        IPage<DwKehutongClInfo> iPage = new Page<>(query.getCurrent(), query.getSize());
        return page(iPage, queryWrapper);
    }

    /**
     * 导出
     *
     * @param query
     * @return
     */
    @Override
    public List<List<String>> export(CityCampPageQuery query) {
        IPage<DwKehutongClInfo> iPage = queryPage(query);
        List<String> list1;
        List<List<String>> list2 = new ArrayList<>();
        for (DwKehutongClInfo record : iPage.getRecords()) {
            list1 = new ArrayList<>();
            list1.add(record.getCampsegTypeName());
            list1.add(record.getCampsegName());
            list1.add(record.getPlanTypeName());
            list1.add(record.getPlanName());
            list1.add(record.getCustomGroupName());
            // 客户群数量
            list1.add(String.valueOf(record.getTargerUserNums()));
            list1.add(record.getCityName());
            list1.add(record.getChannelName());
            list1.add(String.valueOf(record.getTargerUserNums()));
            // 接触用户数
            list1.add(String.valueOf(record.getDJcNum()));
            // 接触率
            list1.add(String.valueOf(record.getDJcRate()));
            // 响应用户数
            list1.add(String.valueOf(record.getDXyNum()));
            // 响应率
            list1.add(String.valueOf(record.getDXyRate()));
            // 办理用户数
            list1.add(String.valueOf(record.getDBlNum()));
            // 转化率
            list1.add(String.valueOf(record.getDBlRate()));

            list2.add(list1);
        }
        return list2;
    }
}

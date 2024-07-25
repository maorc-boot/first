package com.asiainfo.biapp.pec.eval.jx.service.impl;

import com.asiainfo.biapp.pec.core.utils.DateUtil;
import com.asiainfo.biapp.pec.eval.jx.dao.SmartGridEvalMapper;
import com.asiainfo.biapp.pec.eval.jx.req.SmartGridEvalReq;
import com.asiainfo.biapp.pec.eval.jx.service.SmartGridEvalService;
import com.asiainfo.biapp.pec.eval.jx.vo.CampEvalVo;
import com.asiainfo.biapp.pec.eval.jx.vo.SmartGridEvalVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mamp
 * @date 2023/1/9
 */
@Service
@Slf4j
public class SmartGridEvalServiceImpl implements SmartGridEvalService {

	@Resource
	private SmartGridEvalMapper smartGridEvalMapper;

	/**
	 * 活动评估
	 *
	 * @param req
	 * @return
	 */
	@Override
	public IPage<SmartGridEvalVo> smartGridEval(SmartGridEvalReq req) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Page<CampEvalVo> page = new Page<>();
		page.setCurrent(req.getCurrent());
		page.setSize(req.getSize());
		// 日视图: 前一天数据
		if ("D".equals(req.getViewType())) {
			Date date = DateUtil.addDay(new Date(), -1);
			req.setStartDate(sdf.format(date) + "000000");
			req.setEndDate(sdf.format(date) + "235959");
			return smartGridEvalMapper.querySmartGridEffectDay(page, req);
		}
		// 月视图： 当月数据
		if ("M".equals(req.getViewType())) {
			sdf = new SimpleDateFormat("yyyyMM");
			// 当前月第一天
			req.setStartDate(DateUtil.getFirstDayByMonth(sdf.format(new Date()), "yyyyMM", "yyyyMMdd") + "000000");
			// 当前月最后一天
			req.setEndDate(DateUtil.getLastDayByMonth(sdf.format(new Date()), "yyyyMM", "yyyyMMdd") + "235959");
			return smartGridEvalMapper.querySmartGridEffectMonth(page, req);
		}
		// 周视图: 上一周数据
		if ("W".equals(req.getViewType())) {
			Date lastWeekFirstDay = sdf.parse(DateUtil.getLastWeekdayU("yyyyMMdd"));
			// 上周第一天
			req.setStartDate(req.getStartDate() + "000000");
			// 上周最后一天
			req.setEndDate(req.getEndDate() + "235959");
			return smartGridEvalMapper.querySmartGridEffectDay(page, req);
		}
		return smartGridEvalMapper.querySmartGridEffect(page, req);
	}

	@Override
	public List<List<String>> smartGridEvalExport(SmartGridEvalReq req) throws Exception {

		IPage<SmartGridEvalVo> iPage = smartGridEval(req);
		List<String> list1;
		List<List<String>> list2 = new ArrayList<>();
		for (SmartGridEvalVo record : iPage.getRecords()) {
			list1 = new ArrayList<>();
			list1.add(record.getCampsegName());
			list1.add(record.getCampsegRootId());
			list1.add(record.getPlanName());
			list1.add(record.getPlanId());
			list1.add(record.getStartDate());
			list1.add(record.getEndDate());
			list1.add(record.getCityName());
			list1.add(record.getCountyName());
			list1.add(record.getGridName());
			list1.add(String.valueOf(record.getCustomerNum()));
			list1.add(String.valueOf(record.getContactNum()));
			list1.add(String.valueOf(record.getContactRate()));
			list1.add(String.valueOf(record.getMarketSuccessNum()));
			list1.add(String.valueOf(record.getMarketSuccessRate()));
			list1.add(record.getContactType());
			list1.add(record.getContactMode());
			list1.add(record.getChannelName());
			list2.add(list1);
		}
		return list2;
	}

}

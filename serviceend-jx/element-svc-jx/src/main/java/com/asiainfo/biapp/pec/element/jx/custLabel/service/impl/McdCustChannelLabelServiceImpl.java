package com.asiainfo.biapp.pec.element.jx.custLabel.service.impl;

import com.asiainfo.biapp.pec.element.jx.custLabel.dao.McdCustChannelLabelMapper;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.ChannelLabelReq;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.CustLabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.LabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.service.McdCustChannelLabelService;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.ChannelLabel;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.ChannelLabelResult;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.LabelInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客群属性标签同步-和活动创建相关
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-16 上午 11:04:47
 */
@Service
public class McdCustChannelLabelServiceImpl implements McdCustChannelLabelService {

	@Autowired
	private McdCustChannelLabelMapper channelLabelMapper;
	@Override
	public Page<ChannelLabel> getLabelInfos(ChannelLabelReq channelLabelReq) {
		Page<ChannelLabel> page = new Page<>();
		page.setCurrent(channelLabelReq.getCurrent());
		page.setSize(channelLabelReq.getSize());
		page.setRecords(channelLabelMapper.queryChannelLabels(page,channelLabelReq));
		page.setTotal(channelLabelMapper.queryChannelLabelsCount(channelLabelReq));
		return page;
	}

	public List<LabelInfo> getLabelConfig(String channelId){
		return channelLabelMapper.queryLabelConfig(channelId);
	}

	@Override
	public void saveChannelLabel(LabelModel channelLabel) {
		channelLabelMapper.insertCustChannelLabel(channelLabel);
	}

	@Override
	public void updateChannelLabel(LabelModel channelLabel) {
		channelLabelMapper.updateChannelLabels(channelLabel);
	}

	@Override
	public void deleteChannelLabel(String channelId) {
		channelLabelMapper.deleteChannelLabel(channelId);
	}

	@Override
	public ChannelLabelResult getCanSelectlabelByChannelId(CustLabelModel custLabelModel) {
		String channelId = custLabelModel.getChannelId();
		String customGroupId = custLabelModel.getCustomGroupId();
		ChannelLabelResult result = new ChannelLabelResult();
		List<String > availableLabelIdList = new ArrayList<>();
		List<LabelInfo> allLabelList = new ArrayList<>();
		String iopLabelIds = "";
				//查询该渠道在IOP页面配置的所有标签
		ChannelLabel allChannelLabel = channelLabelMapper.queryAllIopLabelByChannel(channelId);
		if(allChannelLabel!=null){
			iopLabelIds = allChannelLabel.getCustLabelIds();
			String custLabelNames = allChannelLabel.getCustLabelNames();
			String[] splitLabelNames = custLabelNames.split(",");
			String[] splitLabelIds = iopLabelIds.split(",");
			for (int i = 0; i < splitLabelIds.length; i++) {
				LabelInfo labelInfo = new LabelInfo();
				labelInfo.setLabelId(splitLabelIds[i]);
				labelInfo.setLabelName(splitLabelNames[i]);
				allLabelList.add(labelInfo);
			}
		}
		//查询该渠道所对应的所有标签
		String custChannelLabels = channelLabelMapper.queryLabelByChannel(channelId,customGroupId);
		if(StringUtils.isNotBlank(custChannelLabels)){
			String[] iopLabelIdArrays = iopLabelIds.split(",");
			String[] cocLableIdArrays = custChannelLabels.split(",");
			List<String> iopLabelIdList = Arrays.asList(iopLabelIdArrays);
			List<String> cocLableIdList = Arrays.asList(cocLableIdArrays);
			// 交集
			availableLabelIdList = iopLabelIdList.stream().filter(item -> cocLableIdList.contains(item)).collect(Collectors.toList());
		}

		result.setAllLabelInfoList(allLabelList);
		result.setAvailableLabelIdList(availableLabelIdList);
		return result;
	}
}

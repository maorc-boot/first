package com.asiainfo.biapp.pec.element.jx.custLabel.service;

import com.asiainfo.biapp.pec.element.jx.custLabel.req.ChannelLabelReq;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.CustLabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.LabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.ChannelLabel;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.ChannelLabelResult;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.LabelInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface McdCustChannelLabelService {

	Page<ChannelLabel> getLabelInfos(ChannelLabelReq channelLabelReq);

	List<LabelInfo> getLabelConfig(String channelId);

	void saveChannelLabel(LabelModel channelLabel);

	void updateChannelLabel(LabelModel channelLabel);

	void deleteChannelLabel(String channelId);

	ChannelLabelResult getCanSelectlabelByChannelId(CustLabelModel custLabelModel);
}

package com.asiainfo.biapp.pec.element.jx.custLabel.dao;

import com.asiainfo.biapp.pec.element.jx.custLabel.req.ChannelLabelReq;
import com.asiainfo.biapp.pec.element.jx.custLabel.req.LabelModel;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.ChannelLabel;
import com.asiainfo.biapp.pec.element.jx.custLabel.vo.LabelInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface McdCustChannelLabelMapper {

	List<ChannelLabel> queryChannelLabels(Page page,@Param("req") ChannelLabelReq req);

	Long queryChannelLabelsCount(@Param("req") ChannelLabelReq req);

	void insertCustChannelLabel(@Param("req") LabelModel req);

	void updateChannelLabels(@Param("req") LabelModel req);

	void deleteChannelLabel(@Param("channelId") String channelId);

	String queryLabelByChannel(@Param("channelId")String channelId,@Param("customGroupId") String customGroupId);

	ChannelLabel queryAllIopLabelByChannel(@Param("channelId")String channelId);

	List<LabelInfo> queryLabelConfig(@Param("channelId")String channelId);

}

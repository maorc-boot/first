package com.asiainfo.biapp.pec.element.jx.custLabel.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 匹配取交集处理之后的结果
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-16 下午 16:43:56
 */
@Data
public class ChannelLabelResult {
	//渠道对应可用标签
	List<String> availableLabelIdList = new ArrayList<>();
	//渠道对应所有标签
	List<LabelInfo> allLabelInfoList = new ArrayList<>();

}

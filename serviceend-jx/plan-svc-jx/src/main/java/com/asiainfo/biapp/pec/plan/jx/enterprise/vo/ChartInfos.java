package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 集团画像信息- 负责结构
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/3 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartInfos {
	//标签类型id
	private String labelTypeId;
	//标签类型名称
	private String labelTypeName;
	//分类名称
	// private String categoryName;
	//chartInfo子节点
	private List<ChartSingle> chartList = new ArrayList<>();


}

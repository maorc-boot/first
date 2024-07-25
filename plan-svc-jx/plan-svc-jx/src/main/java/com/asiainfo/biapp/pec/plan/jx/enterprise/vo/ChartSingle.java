package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 集团画像信息-子结构
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/3 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartSingle {
	//标签分类
	private String categoryName;
	//标签id
	private String labelId;
	//标签名称
	private String labelName;
	//chartInfo子节点
	private List<Map<String,String>> labeldata = new ArrayList<>();


}

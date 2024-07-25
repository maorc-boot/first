package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户画像模型
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/2 17:10
 */
public class CustPortraitModel {
	//场景编码
	private String sceneCode;
	//普通标签、客户群
	private List<PortraitLongTable> longTable = new ArrayList<>();
	//复合标签标签数据结果集
	private List<PortraitCrossTable> crossTable = new ArrayList<>();

	public CustPortraitModel() {
	}

	public CustPortraitModel(String sceneCode, List<PortraitLongTable> longTable, List<PortraitCrossTable> crossTable) {
		this.sceneCode = sceneCode;
		this.longTable = longTable;
		this.crossTable = crossTable;
	}

	public String getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(String sceneCode) {
		this.sceneCode = sceneCode;
	}

	public List<PortraitLongTable> getLongTable() {
		return longTable;
	}

	public void setLongTable(List<PortraitLongTable> longTable) {
		this.longTable = longTable;
	}

	public List<PortraitCrossTable> getCrossTable() {
		return crossTable;
	}

	public void setCrossTable(List<PortraitCrossTable> crossTable) {
		this.crossTable = crossTable;
	}
}

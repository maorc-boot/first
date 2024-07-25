package com.asiainfo.biapp.pec.approve.jx.dto;

import lombok.Data;

/**
 * 活动预警明细查询---手机端传递
 *
 * @author admin
 * @version 1.0
 * @date 2023-05-25 下午 14:40:57
 */

@Data
public class CampWarnMobile {
	//子活动ID
	private String campsegId;
	//活动预警唯一识别码
	private String uniqueIdentifierId;

	private String campsegPId;

	private String userId;

	private String creater;



}

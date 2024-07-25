package com.asiainfo.biapp.pec.plan.jx.camp.model;

import lombok.Data;

/**
 * 客群信息-客群创建信息
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-31 下午 16:00:07
 */
@Data
public class CustomerDetail {
	//客户群ID
	private String customGroupId;
	//客群创建人
	private String createUserId;
	//客群是否更新，状态0/1 ：不更新/更新
	private Integer updateStatus;

}

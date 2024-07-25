package com.asiainfo.biapp.pec.eval.jx.req;

import lombok.Data;

/**
 * @author : yantao
 * @version : v1.0
 * @className : EnterpriseEvalReq
 * @description : [描述说明该类的功能]
 * @createTime : [2023-07-13 上午 09:38:25]
 */
@Data
public class EnterpriseEvalReq {
	//当前页,默认1
	private Long pageNum=1L;
	//每页容量，默认10
	private Long pageSize=10L;
	//政企任务名称
	private String taskName;
	//政企任务开始时间
	private String startDay;
	//政企任务结束时间
	private String endDay;
	//地市
	private String cityId;
	//区县
	private String countyId;
	//客户经理/网格经理
	private String managerId;
	//网格
	private String gridId;
	//集团ID
	private String groupsId;
	//集团名称
	private String inputGroupName;
	//客户经理/网格经理
	private String inputManager;
	//案例名称
	private String inputCamp;
	//集团类型
	private String selectGroupsType;

}

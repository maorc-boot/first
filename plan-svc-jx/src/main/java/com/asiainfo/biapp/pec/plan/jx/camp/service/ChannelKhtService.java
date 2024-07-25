package com.asiainfo.biapp.pec.plan.jx.camp.service;


import com.asiainfo.biapp.pec.plan.jx.camp.vo.TaskTypeDetail;

import java.util.List;

public interface ChannelKhtService {
	/**
	 * 查询任务类型
	 * @param pcode
	 * @return
	 */
	List<TaskTypeDetail> selectChannelTaskTypeDetail(String pcode);
}

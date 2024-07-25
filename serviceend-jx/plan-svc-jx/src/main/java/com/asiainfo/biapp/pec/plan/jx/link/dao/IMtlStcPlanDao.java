package com.asiainfo.biapp.pec.plan.jx.link.dao;


import com.asiainfo.biapp.pec.iopws.util.Pager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/*
 * Created on 4:00:43 PM
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: asiainfo.,Ltd</p>
 * @author weilin.wu  wuwl2@asiainfo.com
 * @version 1.0
 */
@Mapper
public interface IMtlStcPlanDao {
	/**
	 * 执行查询sql 返回结果集
	 * @return
	 */
	List<Map<String, Object>> queryListBySql(@Param("typeId") String typeId,@Param("statusId") String statusId,@Param("keyWords") String keyWords,@Param("pager") Pager pager);

}

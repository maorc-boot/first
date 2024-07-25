package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.req.EnterpriseEvalReq;
import com.asiainfo.biapp.pec.eval.jx.vo.Marketing;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IEnterpriseMarketingDao{


    List<Map<String, Object>> getDimManager(String cityId, String countyId) throws Exception;

    List<Map<String, Object>> getDimGrid(String cityId, String countyId, String managerId) throws Exception;

    List<Map<String, Object>> getDimGroups(String cityId, String countyId, String managerId, String gridId) throws Exception;

    IPage<Marketing> queryMarketingNumList(Page<?> pager, @Param("req") EnterpriseEvalReq req);

    IPage<Marketing> queryMarketingList(Page<?> pager, @Param("req") EnterpriseEvalReq req);

    List<Marketing> queryExportMarketingNumList(@Param("req") EnterpriseEvalReq req);

    List<Marketing> queryExportMarketingList(@Param("req") EnterpriseEvalReq req);

    List<Map<String, Object>> getCaseData(String taskId);

    long saveCase(Map<String, Object> paramMap);


}

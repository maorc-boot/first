package com.asiainfo.biapp.pec.eval.jx.service;

import com.asiainfo.biapp.pec.eval.jx.req.EnterpriseEvalReq;
import com.asiainfo.biapp.pec.eval.jx.vo.Marketing;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IEnterpriseMarketingService{


    List<Map<String, Object>> getDimManager(String cityId, String countyId) throws Exception;

    List<Map<String, Object>> getDimGrid(String cityId, String countyId, String managerId) throws Exception;

    List<Map<String, Object>> getDimGroups(String cityId, String countyId, String managerId, String gridId) throws Exception;

    IPage<Marketing> queryMarketingList(EnterpriseEvalReq enterpriseEvalReq);

    List<Object> exportMarketingList(EnterpriseEvalReq enterpriseEvalReq);

    Map<String, Object> saveCase(Map<String, Object> paramMap) throws Exception;

    void exportExcel(String fileName, String[] excelHeader, List<Object> list, HttpServletResponse response) throws Exception;

}

package com.asiainfo.biapp.pec.eval.jx.controller;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.eval.jx.req.EnterpriseEvalReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalSaveCaseReq;
import com.asiainfo.biapp.pec.eval.jx.service.IEnterpriseMarketingService;
import com.asiainfo.biapp.pec.eval.jx.utils.MpmUtil;
import com.asiainfo.biapp.pec.eval.jx.vo.Marketing;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.asiainfo.biapp.pec.core.utils.UserUtil.getUser;

@Api(tags = {"江西：政企效果评估"}, value = "江西：政企效果评估")
@Controller
@RequestMapping("/action/jx/enterpriseMarketing")
@Slf4j
public class EnterpriseMarketingController {

    @Autowired
    private IEnterpriseMarketingService marketingService;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "查询营销效果评估-经理信息", notes = "查询营销效果评估-经理信息")
    @ResponseBody
    @RequestMapping(value = "/getDimManager", method = RequestMethod.POST)
    public List<Map<String, Object>> getDimManager(@RequestBody EnterpriseEvalReq enterpriseEvalReq) {
        List<Map<String, Object>> list = null;
        String countyId = enterpriseEvalReq.getCountyId();
        String cityId = enterpriseEvalReq.getCityId();
        try {
            list = marketingService.getDimManager(cityId, countyId);
        } catch (Exception e) {
            log.error("getDimManager error:{}", e);
        }
        return list;
    }

    @ApiOperation(value = "查询营销效果评估-网格信息", notes = "查询营销效果评估-网格信息")
    @ResponseBody
    @RequestMapping(value = "/getDimGrid", method = RequestMethod.POST)
    public List<Map<String, Object>> getDimGrid(@RequestBody EnterpriseEvalReq enterpriseEvalReq) {
        List<Map<String, Object>> list = null;
        String managerId = enterpriseEvalReq.getManagerId();
        String cityId = enterpriseEvalReq.getCityId();
        String countyId = enterpriseEvalReq.getCountyId();
        try {
            list = marketingService.getDimGrid(cityId, countyId, managerId);
        } catch (Exception e) {
            log.error("getDimManager error:{}", e);
        }
        return list;
    }

    @ApiOperation(value = "查询营销效果评估-集团信息", notes = "查询营销效果评估-集团信息")
    @ResponseBody
    @RequestMapping(value = "/getDimGroups", method = RequestMethod.POST)
    public List<Map<String, Object>> getDimGroups(@RequestBody EnterpriseEvalReq enterpriseEvalReq) {
        List<Map<String, Object>> list = null;
        String gridId = enterpriseEvalReq.getGridId();
        String managerId = enterpriseEvalReq.getManagerId();
        String cityId = enterpriseEvalReq.getCityId();
        String countyId = enterpriseEvalReq.getCountyId();
        try {
            list = marketingService.getDimGroups(cityId, countyId, managerId, gridId);
        } catch (Exception e) {
            log.error("getDimManager error:{}", e);
        }
        return list;
    }

    @ApiOperation(value = "分页查询营销效果评估数据", notes = "分页查询营销效果评估数据")
    @ResponseBody
    @RequestMapping(value = "/queryMarketingList", method = RequestMethod.POST)
    public IPage<Marketing> queryMarketingList(@RequestBody EnterpriseEvalReq enterpriseEvalReq) {
        return marketingService.queryMarketingList(enterpriseEvalReq);
    }

    @ApiOperation(value = "导出营销效果评估数据", notes = "导出营销效果评估数据")
    @ResponseBody
    @RequestMapping(value = "/exportMarketingData", method = RequestMethod.POST)
    public void exportMarketingData(@RequestBody EnterpriseEvalReq enterpriseEvalReq, HttpServletResponse response) {

        try {
            List<Object> list = marketingService.exportMarketingList(enterpriseEvalReq);
            String fileName = "政企营销活动评估导出列表.xls";
            String[] excelHeader = new String[]{"政企任务名称", "政企任务ID", "活动名称", "活动ID", "地市", "区县", "网格",
                    "集团名称", "客户经理", "集团类型", "政企任务开始时间", "政企任务结束时间","目标用户数","预热目标用户数",
                    "驻场营销次数","执行及时数","执行及时率","下发数","接触数","接触成功率","营销成功数","营销成功率"};
            marketingService.exportExcel(fileName,excelHeader,list,response);
        } catch (Exception e) {
            log.error("政企营销任务评估失败{}", e);
        }
    }



    @ResponseBody
    @RequestMapping(value = "saveCase", method = RequestMethod.POST)
    public Map<String, Object> saveCase(@RequestBody EvalSaveCaseReq evalSaveCaseReq) {
         UserSimpleInfo user = UserUtil.getUser(request);
        Map<String, Object> map = null;
        String taskId = evalSaveCaseReq.getTaskId();
        String caseName = evalSaveCaseReq.getCaseName();
        String caseDes = evalSaveCaseReq.getCaseDes();
        String ID = MpmUtil.convertLongMillsToYYYYMMDDHHMMSSSSS();
        ;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {

            paramMap.put("taskId", taskId);
            paramMap.put("caseName", caseName);
            paramMap.put("caseDes", caseDes);
            paramMap.put("userId", user.getUserId());
            paramMap.put("userName", user.getUserName());
            paramMap.put("userCity", user.getCityId());
            paramMap.put("ID",ID);
            map  = marketingService.saveCase(paramMap);

        } catch (Exception e) {
            map.put("status",500);
            map.put("message","");
        }
        return map;
    }
}

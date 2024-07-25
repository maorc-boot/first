package com.asiainfo.biapp.pec.plan.jx.enterprise.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.client.app.element.vo.PlanDefVO;
import com.asiainfo.biapp.pec.core.common.OutInterface;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.cons.RestConstant;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.camp.common.ConstantCamp;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.req.CampBaseInfoJxVO;
import com.asiainfo.biapp.pec.plan.jx.camp.req.PlanBaseInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.PlanExtInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampsegServiceJx;
import com.asiainfo.biapp.pec.plan.jx.camp.service.impl.ChannelConfServcieHolder;
import com.asiainfo.biapp.pec.plan.jx.camp.service.impl.ChannelConfServiceImpl801;
import com.asiainfo.biapp.pec.plan.jx.enterprise.constant.Const;
import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.McdSysEnum;
import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.EnterpriseService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.ICustGroupSyncService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.IMcdMtlGuardNewService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.PlanChannelAdivResourceService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.util.QuotaUtils;
import com.asiainfo.biapp.pec.plan.jx.enterprise.util.XMLUtil;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.*;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.util.MpmUtil;
import com.asiainfo.biapp.pec.plan.model.McdDimAdivInfo;
import com.asiainfo.biapp.pec.plan.model.McdSysUser;
import com.asiainfo.biapp.pec.plan.service.IMcdCampDefService;
import com.asiainfo.biapp.pec.plan.service.IMcdSysUserService;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.asiainfo.biapp.pec.plan.util.ToolUtils;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @date 2023/6/25
 */
@Api(tags = "江西-政企:个性化接口")
@RestController
@RequestMapping("/api/jx/enterprise")
@Slf4j
public class EnterpriseJxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseJxController.class);
    //派单成功（执行中）
    public static final String MPM_CAMPSEG_STAT_DDCG = "54";
    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private PlanChannelAdivResourceService planChannelAdivResourceService;

    @Autowired
    private IMcdSysUserService mcdSysUserService;


    @Autowired
    private IMcdMtlGuardNewService guardNewService;

    @Autowired
    ICustGroupSyncService custGroupSyncService;

    @Autowired
    private ChannelConfServcieHolder servcieHolder;
    @Autowired
    private IMcdCampsegServiceJx campsegService;
    @Autowired
    private IMcdCampDefService campDefService;
    @Resource
    private PecApproveFeignClient pecApproveFeignClient;


    /**
     * 1.1 产品查询接口: 查询政企产品
     *
     * @param offerRequestVo 入参封装
     * @return
     */
    @ApiOperation(value = "产品查询接口(查询政企产品)", notes = "产品查询接口(查询政企产品)")
    @RequestMapping("/queryOffer")
    @OutInterface
    public Object queryOffer(@RequestBody OfferRequestVo offerRequestVo) {

        Pager pager = new Pager();
        String offerCode = offerRequestVo.getOfferCode() == null ? "" : offerRequestVo.getOfferCode();
        String offerName = offerRequestVo.getOfferName() == null ? "" : offerRequestVo.getOfferName();
        String pageIndex = String.valueOf(offerRequestVo.getPageIndex()) == null ? "1" : String.valueOf(offerRequestVo.getPageIndex());
        String pageSize = String.valueOf(offerRequestVo.getPageSize()) == null ? "1" : String.valueOf(offerRequestVo.getPageSize());

        pager.setPageSize(Integer.parseInt(pageSize));
        pager.setPageNum(Integer.parseInt(pageIndex)); // 当前页

        Map<String, Object> map = new HashMap<>();

        map.put("offerCode", offerCode);
        map.put("offerName", offerName);
        map.put("pager", pager);

        OfferResponseVo responseVo = new OfferResponseVo();

        try {
            List<OfferVo> list = enterpriseService.queryEnterprisePlan(map);
            responseVo.setResultCode("0");
            responseVo.setResultInfo("OK");
            responseVo.setPageIndex(pager.getPageNum());
            responseVo.setPageSize(pager.getPageSize());
            responseVo.setTotalCount((int) pager.getTotalSize());
            responseVo.setTotalPages(pager.getTotalPage());
            responseVo.setData(list);
        } catch (Exception e) {
            responseVo.setResultCode("201");
            responseVo.setResultInfo("ERROR,请联系管理员");
            responseVo.setData(null);
            LOGGER.error("查询政企产品出错{}", e);
        }
        return responseVo;
    }

    /**
     * 1.2 查询审批人接口: 获取下级审批人
     *
     * @return
     */
    @ApiOperation(value = "查询审批人接口(获取下级审批人)", notes = "查询审批人接口(获取下级审批人)")
    @RequestMapping("/queryApprove")
    @OutInterface
    public Object queryApprove() throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ApproveUserVo> map = enterpriseService.queryApprove();
            result.put("resultCode", "0");
            result.put("resultInfo", "OK");
            result.put("data", map);
        } catch (Exception e) {
            result.put("resultCode", "201");
            result.put("resultInfo", "ERROR,请联系管理员");
            result.put("data", null);
            LOGGER.error("查询审批人接口: 获取下级审批人异常{}", e);
        }
        if (result.isEmpty()) {
            throw new Exception("字典表查询角色ID,数据为空");
        }
        return result;
    }

    /**
     * 2.0 查询产品库删除产品所具有的角色ID---角色ID和审批角色配置一样，读同一个配置
     *
     * @return
     */
    @ApiOperation(value = "查询产品库删除产品所具有的角色ID", notes = "查询产品库删除产品所具有的角色ID")
    @GetMapping("/getDeletePlanRole")
    @OutInterface
    public Object getDeletePlanRole() throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ApproveUserVo> map = enterpriseService.queryApprove();
            result.put("resultCode", "0");
            result.put("resultInfo", "OK");
            result.put("data", map);
        } catch (Exception e) {
            result.put("resultCode", "201");
            result.put("resultInfo", "ERROR,请联系管理员");
            result.put("data", null);
            LOGGER.error("查询产品库删除产品所具有的角色ID{}", e);
        }
        if (result.isEmpty()) {
            throw new Exception("字典表查询角色ID,数据为空");
        }
        return result;
    }

    /**
     * 1.6 区域信息查询: 根据用户ID,查询用户所属区域
     *
     * @param userId 用户ID
     * @return
     */
    @ApiOperation(value = "区域信息查询(根据用户ID,查询用户所属区域)", notes = "区域信息查询(根据用户ID,查询用户所属区域)")
    @RequestMapping("/queryOrg")
    @OutInterface
    public Object queryOrg(@Param("userId") String userId, HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> org = new HashMap();
        result.put("resultCode", "0");
        result.put("resultInfo", "OK");
        List data = new ArrayList<>();
        if (StringUtils.isNotEmpty(userId)) {
            McdSysUser user = null;
            try {
                LambdaQueryWrapper<McdSysUser> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(McdSysUser::getUserId, userId).last(" limit 1");
                user = mcdSysUserService.getOne(wrapper);
            } catch (Exception e) {
                LOGGER.error("获取用户信息异常,userId= {}", userId, e);
            }
            if (null == user) {
                org.put("orgId", "999");
                org.put("orgName", "省公司");
                data.add(org);
                result.put("data", data);
                return result;
            }
            org.put("orgId", user.getCityId());
            org.put("orgName", "");
            data.add(org);
            result.put("data", data);
            return result;
        }
        result.put("data", enterpriseService.queryCityList());
        return result;
    }

    /**
     * 1.7 活动数据、线上营销数据、线下营销数据统计查询
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "活动数据、线上营销数据、线下营销数据统计查询", notes = "活动数据、线上营销数据、线下营销数据统计查询")
    @RequestMapping("/subitemStat")
    @OutInterface
    public Object subitemStat(HttpServletRequest request, HttpServletResponse response) {

        OfferResponseVo responseVo = new OfferResponseVo();
        responseVo.setResultCode("0");
        responseVo.setResultInfo("OK");
        responseVo.setPageIndex(1);
        responseVo.setPageSize(10);
        responseVo.setTotalCount(1);
        responseVo.setTotalPages(1);
        List<OfferVo> data = new ArrayList<>();
        responseVo.setData(data);
        return responseVo;
    }

    /**
     * 1.8 营销评估列表查询
     */
    @ApiOperation(value = "营销评估列表查询", notes = "营销评估列表查询")
    @RequestMapping("/effectAssess")
    @OutInterface
    public Object effectAssess(HttpServletRequest request, HttpServletResponse response) {

        OfferResponseVo responseVo = new OfferResponseVo();
        responseVo.setResultCode("0");
        responseVo.setResultInfo("OK");
        responseVo.setPageIndex(1);
        responseVo.setPageSize(10);
        responseVo.setTotalCount(1);
        responseVo.setTotalPages(1);
        List<OfferVo> data = new ArrayList<>();
        responseVo.setData(data);
        return responseVo;
    }

    /**
     * 1.9 运营位查询: 根据渠道ID,查询运营位
     *
     * @return
     */
    @ApiOperation(value = "运营位查询: 根据渠道ID,查询运营位", notes = "运营位查询: 根据渠道ID,查询运营位")
    @RequestMapping(value = "/queryAdsense", method = RequestMethod.POST)
    @OutInterface
    public Object queryAdsense(HttpServletRequest request) {
        net.sf.json.JSONObject params = this.getParams(request);
        String channelId = params.getString("channelId") == null ? "" : params.getString("channelId");

        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> resultList = new ArrayList<>();

        LambdaQueryWrapper<McdDimAdivInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimAdivInfo::getChannelId, channelId);
        List<McdDimAdivInfo> list = planChannelAdivResourceService.list(wrapper);

        for (McdDimAdivInfo adivInfo : list) {
            Map<String, String> org = new HashMap();
            org.put("adsenseCode", adivInfo.getAdivId());
            org.put("adsenseName", adivInfo.getAdivName());
            resultList.add(org);
        }

        result.put("resultCode", "0");
        result.put("resultInfo", "OK");

        result.put("data", resultList);
        return result;
    }

    /**
     * 获取参数
     *
     * @param request
     * @return
     */
    private net.sf.json.JSONObject getParams(HttpServletRequest request) {
        net.sf.json.JSONObject params = null;
        StringBuilder paramsStr = new StringBuilder();
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader br = null;
        try {
            in = request.getInputStream();
            inReader = new InputStreamReader(in, RestConstant.PARAMS_ENCONDING);
            br = new BufferedReader(inReader);
            String s = "";
            while ((s = br.readLine()) != null) {
                paramsStr.append(s); // 读取参数
            }
            params = net.sf.json.JSONObject.fromObject(paramsStr.toString());
        } catch (Exception e) {
            LOGGER.error("读取参数报错", e);
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (Exception e) {
                LOGGER.error("close BufferedReader error !");
            }
            try {
                if (null != inReader) {
                    inReader.close();
                }
            } catch (Exception e) {
                LOGGER.error("close InputStreamReader error !");
            }
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                LOGGER.error("close InputStream error !");
            }
        }
        return params;
    }

    /**
     * 1.12   客群创建               (政企同步用户群给IOP)
     *
     * @param grpInfo 客户群基础信息
     * @return
     */
    @ApiOperation(value = "客群创建: 政企同步用户群给IOP", notes = "客群创建: 政企同步用户群给IOP")
    @RequestMapping(value = "/createCustomGroup", method = RequestMethod.POST)
    @OutInterface
    public Object createCustomer(@RequestBody GrpInfo grpInfo) {

        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", "0");
        result.put("resultInfo", "OK");
        List data = new ArrayList<>();
        result.put("data", data);
        try {
            String xml = XMLUtil.convertToXml(grpInfo);

            String repXml = custGroupSyncService.saveCustPushLog(xml, 0);
            data.add(grpInfo.getCustomGroupId());
        } catch (Exception e) {
            result.put("resultCode", "1");
            result.put("resultInfo", e.getMessage());
        }
        return result;
    }

    /**
     * 1.14  获取营销任务活动详情
     *
     * @return
     */
    @ApiOperation(value = "获取营销任务活动详情", notes = "获取营销任务活动详情")
    @RequestMapping(value = "/querySubitemDetail", method = RequestMethod.POST)
    @OutInterface
    public Object querySubitemCampDetail(@RequestParam String subitemId) {
        // LOGGER.info("querySubitemDetail requset param json:{}",json);
        // String subitemId = json.getString("subitemId");
        net.sf.json.JSONObject obj = new net.sf.json.JSONObject();
        Map<String, Object> result = new HashMap<>();
        LOGGER.info("querySubitemDetail requset param subitemId:{}",subitemId);
        if (org.apache.commons.lang3.StringUtils.isEmpty(subitemId)) {
            result.put("resultCode", "9");
            return result;
        }
        SubitemDefineDTO dto = new SubitemDefineDTO();
        try {
            List<Map<String, Object>> campDetails = enterpriseService.getCampDetails(subitemId);
            if (campDetails != null && campDetails.size() > 0) {
                Map<String, Object> detailMap = campDetails.get(0);

                dto.setSubitemTarget(detailMap.get("ACTIVITY_OBJECTIVE").toString());//活动目的
                dto.setBusinessClass(detailMap.get("ACTIVITY_TYPE").toString());//活动类型
                dto.setOpposite(detailMap.get("CHANNEL_ADIV_ID") == null? "":detailMap.get("CHANNEL_ADIV_ID").toString());//运营位
                dto.setAdsenseName(detailMap.get("ADIV_NAME") == null? "":detailMap.get("ADIV_NAME").toString()); //运营位名称
                dto.setCampsegId(detailMap.get("CAMPSEG_ID").toString());//策略ID
                dto.setCampsegName(detailMap.get("CAMPSEG_NAME").toString());//策略名称
                dto.setActivityType(getActivityType(detailMap.get("ACTIVITY_TYPE").toString()));//活动类型
                dto.setActivityObjective(getActivityObject(detailMap.get("ACTIVITY_OBJECTIVE").toString()));//活动目的

                dto.setExecText(detailMap.get("EXEC_CONTENT") == null? "":detailMap.get("EXEC_CONTENT").toString());//营销用语
                dto.setChannelId(detailMap.get("CHANNEL_ID") == null? "":detailMap.get("CHANNEL_ID").toString());//渠道ID
                dto.setAdivId(detailMap.get("CHANNEL_ADIV_ID") == null? "":detailMap.get("CHANNEL_ADIV_ID").toString());//运营位
                dto.setExecContent(detailMap.get("EXEC_CONTENT") == null? "":detailMap.get("EXEC_CONTENT").toString());//营销用语
                dto.setCampType(detailMap.get("CAMPSEG_TYPE_ID") == null? "":detailMap.get("CAMPSEG_TYPE_ID").toString());//策略类型；
                dto.setCepEventId(detailMap.get("CEP_EVENT_ID") == null? "":detailMap.get("CEP_EVENT_ID").toString());//事件ID
                dto.setCepEventName(detailMap.get("CEP_SCENE_NAME") == null? "":detailMap.get("CEP_SCENE_NAME").toString());//事件名称
                if("811".equals(detailMap.get("CHANNEL_ID").toString())){
                    dto.setCancelRecommendCycle(detailMap.get("COLUMN_EXT3") == null? "":detailMap.get("COLUMN_EXT3").toString());//取消推荐时间天数
                    dto.setE_811_TASK_DESCRIPTION(detailMap.get("COLUMN_EXT11") == null? "":detailMap.get("COLUMN_EXT11").toString());//任务描述及要求
                    dto.setE_811_PROJECT_LEVEL_2(detailMap.get("COLUMN_EXT7") == null? "":detailMap.get("COLUMN_EXT7").toString());//项目层级二名称
                    dto.setE_811_PROJECT_LEVEL_3(detailMap.get("COLUMN_EXT6") == null? "":detailMap.get("COLUMN_EXT6").toString());//项目层级三ID
                    dto.setE_811_SUB_TASK_TYPE(detailMap.get("COLUMN_EXT8") == null? "":detailMap.get("COLUMN_EXT8").toString());//项目层级三KEY
                    dto.setE_811_CUSTGROUP_RULE(detailMap.get("COLUMN_EXT9") == null? "":detailMap.get("COLUMN_EXT9").toString());//营销(服务)目标客群提取规则
                    dto.setE_811_SERV_FILE_NAME(detailMap.get("COLUMN_EXT10") == null? "":detailMap.get("COLUMN_EXT10").toString());//外呼需求附件
                    dto.setE_811_SMS_CHANNEL_TYPE(detailMap.get("MARKETING_TYPE") == null? "":detailMap.get("MARKETING_TYPE").toString());//营销类型；
                    dto.setE_811_CALL_TYPE(detailMap.get("ADIV_NAME") == null? "":detailMap.get("ADIV_NAME").toString());//运营位名称
                    dto.setE_811_TASK_TYPE(detailMap.get("CAMP_BUSINESS_TYPE") == null? "":detailMap.get("CAMP_BUSINESS_TYPE").toString());//任务大类
                }
                if("802".equals(detailMap.get("CHANNEL_ID").toString())){
                    dto.setE_802_MARKETING(detailMap.get("COLUMN_EXT2") == null? "":detailMap.get("COLUMN_EXT2").toString());//营销用语
                    dto.setE_802_CALL_TYPE(detailMap.get("COLUMN_EXT3") == null? "":detailMap.get("COLUMN_EXT3").toString());//外呼类型
                    dto.setE_802_CUSTGROUP_RULE(detailMap.get("COLUMN_EXT4") == null? "":detailMap.get("COLUMN_EXT4").toString());//营销(服务)目标客户及提取原则
                    dto.setE_802_TASK_DESCRIPTION(detailMap.get("COLUMN_EXT5") == null? "":detailMap.get("COLUMN_EXT5").toString());//任务描述及要求
                    dto.setE_802_SERV_FILE_NAME(detailMap.get("COLUMN_EXT6") == null? "":detailMap.get("COLUMN_EXT6").toString());//外呼需求附件
                }

            }
            obj = net.sf.json.JSONObject.fromObject(dto);

            result.put("resultCode", "0");
            result.put("resultInfo", "OK");
            result.put("data", obj);

        } catch (Exception e) {
            LOGGER.error("获取活动信息异常", e);
            result.put("resultCode", "9");
            result.put("data", null);
            return result;
        }
        return result;
    }


    /**
     * 3.3 业务指标变化
     *
     * @param request HttpServletRequest
     * @return
     */
    @ApiOperation(value = "业务指标变化", notes = "业务指标变化")
    @RequestMapping("/busiTargetAlert")
    @OutInterface
    public Object busiTargetAlert(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", "0");
        result.put("resultInfo", "OK");
        List data = new ArrayList<>();
        result.put("data", data);
        return result;
    }

    /**
     * 重定向到政企页面
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "重定向到政企页面", notes = "重定向到政企页面")
    @RequestMapping("/redirect")
    @OutInterface
    public ModelAndView sendRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object obj = request.getSession().getAttribute("USER");
        if (null == obj) {
            response.sendRedirect(request.getContextPath() + Const.PAGE_NO_LOGIN);
        }
        McdSysUser user = (McdSysUser) obj;
        // url后添加用户ID和用户名称
        String urlParam = String.format("&userId=%s&userName=%s", user.getId(), user.getUserId());
        String url = request.getParameter("url");
        url += urlParam;

        ModelAndView model = new ModelAndView("menuframe/enterpriseFrame");
        model.addObject("url", url);
        return model;

    }


    /**
     * 811外呼项目层级查询
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "811外呼项目层级查询", notes = "811外呼项目层级查询")
    @ResponseBody
    @RequestMapping(value = "/queryCallItemLevel", method = RequestMethod.POST)
    @OutInterface
    public Object queryEnumByTypeAndParentId(HttpServletRequest request, HttpServletResponse response) {
        net.sf.json.JSONObject params = this.getParams(request);
        List<McdSysEnum> enumList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        try {
            String enumType = params.getString("enumType") == null ? "" : params.getString("enumType");
            String parentId = params.getString("parentId") == null ? "" : params.getString("parentId");

            enumList = guardNewService.queryEnumByTypeAndParentId(enumType, parentId);
            result.put("resultCode", "0");
            result.put("resultInfo", "OK");
            result.put("data", enumList);
        } catch (Exception e) {
            LOGGER.error("查询枚举信息失败！", e);
            result.put("resultCode", "9");
            result.put("data", null);
        }
        return result;
    }


    /**
     * 根据关键字以及父节点id获取对应的枚举值
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "根据关键字以及父节点id获取对应的枚举值", notes = "根据关键字以及父节点id获取对应的枚举值")
    @ResponseBody
    @RequestMapping("/queryCallTaskType")
    @OutInterface
    public Object queryTaskTypeBySubTaskId(HttpServletRequest request, HttpServletResponse response) {
        net.sf.json.JSONObject params = this.getParams(request);
        Map<String, Object> result = new HashMap<>();
        String taskId = null;
        try {
            String subTaskTypeId = params.getString("subTaskTypeId") == null ? "" : params.getString("subTaskTypeId");

            taskId = guardNewService.queryTaskIdBySubTaskId(subTaskTypeId);
            result.put("resultCode", "0");
            result.put("resultInfo", "OK");
            result.put("data", taskId);
        } catch (Exception e) {
            LOGGER.error("查询外呼任务大类失败！", e);
            result.put("resultCode", "9");
            result.put("data", "4");
        }
        return result;
    }

    /**
     * 根据key获取数据字典的value
     * 如果获取不到返回"" 返回的数据都是字符串格式
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取事件列表（此接口无需调用，参见7.0创建活动时所选事件的方式搞）", notes = "获取事件列表（此接口无需调用，参见7.0创建活动时所选事件的方式搞）")
    @RequestMapping("/queryCallEventUrl")
    @ResponseBody
    @OutInterface
    public Object getDicValueByKey(HttpServletRequest request) throws Exception {
        net.sf.json.JSONObject params = this.getParams(request);
        String key = params.getString("dicKey") == null ? "" : params.getString("dicKey");
        Map<String, Object> result = new HashMap<>();
        String value = "";
        // String value= AppConfigService.getProperty(key);
        if (value == null) {
            value = "";
        }
        result.put("resultCode", "0");
        result.put("resultInfo", "OK");
        result.put("data", value);
        return result;
    }


    private String getActivityObject(String aoj) {
        String workStationName = "";
        if ("1".equals(aoj)) {
            workStationName = "新增类";
        } else if ("2".equals(aoj)) {
            workStationName = "存量类";
        } else if ("3".equals(aoj)) {
            workStationName = "价值类";
        } else if ("4".equals(aoj)) {
            workStationName = "离网类";
        } else {
            workStationName = "其它类";
        }

        return workStationName;
    }


    private String getActivityType(String activiyType) {
        String workStationName = "";
        if ("1".equals(activiyType)) {
            workStationName = "4G产品类";
        } else if ("2".equals(activiyType)) {
            workStationName = "终端类";
        } else if ("3".equals(activiyType)) {
            workStationName = "流量类";
        } else if ("4".equals(activiyType)) {
            workStationName = "数字化服务类";
        } else if ("5".equals(activiyType)) {
            workStationName = "基础服务类";
        } else if ("6".equals(activiyType)) {
            workStationName = "PCC类";
        } else if ("7".equals(activiyType)) {
            workStationName = "宽带类";
        } else {
            workStationName = "其它类";
        }

        return workStationName;
    }

    /**
     * 审批回调接口
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "审批回调接口", notes = "审批回调接口")
    @RequestMapping(value = "/campsegApproveSync")
    @OutInterface
    public Object mcdExternalCampApproveFeedback(HttpServletRequest request, HttpServletResponse response) {
        //参数初始化
        McdCampDef mcdCampDef = initSyncParameters(request);
        log.info("审批调用approve入参:{}",mcdCampDef.toString());
        Object approveFeedback = pecApproveFeignClient.mcdExternalCampApproveFeedback(mcdCampDef);
        log.info("审批调用approve返回结果:{}",approveFeedback.toString());
        return approveFeedback;
    }


    /**
     * 创建策略接口新
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "创建策略接口新", notes = "创建策略接口新")
    @RequestMapping(value = "/createSubitem")
    @OutInterface
    public Object mcdExternalCampInfoSaveJx(HttpServletRequest request, HttpServletResponse response) {
        MarketResultVo resultVo = new MarketResultVo();
        try {
            net.sf.json.JSONObject params = this.getParams(request);
            String createUserId = params.has("creatorId") ? params.getString("creatorId") : ""; // 创建人
            String custId = params.getString("custId");
            //根据userid查询用户信息 createUserId
            UserSimpleInfo user = new UserSimpleInfo();
            if (StringUtils.isNotEmpty(createUserId)) {
                try {
                    LambdaQueryWrapper<McdSysUser> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(McdSysUser::getUserId, createUserId).last(" limit 1");
                    McdSysUser mcdSysUser = mcdSysUserService.getOne(wrapper);
                    user = chanageUserAttr(mcdSysUser);
                } catch (Exception e) {
                    LOGGER.error(" 政企接口根据创建人ID查询用户信息失败", e);
                    resultVo.setResultCode("1");
                    resultVo.setResultInfo(e.getMessage());
                    return resultVo;
                }
            } else {
                LOGGER.error("接口中参数创建人为空");
                resultVo.setResultCode("1");
                resultVo.setResultInfo("参数确实创建人信息");
                return resultVo;
            }

            GridRequestParameters requestParameters = this.initParameters(params, user); // 初始化参数信息
            this.validateParameters(requestParameters); // 针对接口传递的参数进行验证
            //组装参数
            Object[] object = new Object[21];
            object[0] = requestParameters.getCampsegName();//活动名称
            //无活动描述，暂用活动名称填充
            object[1] = requestParameters.getCampsegName();//活动描述
            String custgroupId ;
            if(StrUtil.isEmpty(custId)){
                custgroupId = "ZQ_KHQ" + MpmUtil.convertLongMillsToYYYYMMDDHHMMSSSSS();
            }else{
                custgroupId = custId;
            }
            object[2] = custgroupId;//客群ID
            object[3] = requestParameters.getPlanId();//产品ID
            object[4] = "";//融合产品
            object[5] = "";//同系列产品
            object[6] = "";//互斥产品
            object[7] = requestParameters.getStartTime();//活动开始时间
            object[8] = requestParameters.getEndTime();//活动结束时间
            object[9] = requestParameters.getCampTypeId();//策略类型
            object[10] = requestParameters.getBusinessClass();//活动类型
            object[11] = requestParameters.getSubitemTarget();//活动目的
            object[12] = requestParameters.getCampPreview();//是否预演
            object[13] = requestParameters.getChannelId();//渠道ID
            object[14] = requestParameters.getAdivId();//运营位ID
            object[15] = requestParameters.getExecText();//营销用语
            object[16] = "";//客户群规则描述
            object[17] = requestParameters.getExecText();//营销话术
            object[18] = "";//活动政策
            object[19] = "";//短信模板
            object[20] = requestParameters.getChannelExt();//扩展字段

            McdCampImportTask task = new McdCampImportTask();
            task.setCreateUser(requestParameters.getCreateorId());//创建人

            String campsegId = saveZqCamp(object, task, user);
            if(StrUtil.isEmpty(custId)) {
                //保存客群定义表信息,此时还查不出来targetId，先用0代替
                // long targetId = enterpriseService.getTargetIdBySubitemId(campsegId);
                long targetId = 0L;
                enterpriseService.saveZqCustDefInfo(targetId, custgroupId, createUserId);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subitemId", campsegId);
            resultVo.setData(jsonObject);
            resultVo.setResultInfo("OK");
            resultVo.setResultCode("0");
        } catch (Exception e) {
            LOGGER.error("接口调用失败", e);
            resultVo.setResultCode("1");
            resultVo.setResultInfo(e.getMessage());
            return resultVo;
        }
        return resultVo;
    }

    public UserSimpleInfo chanageUserAttr(McdSysUser mcdSysUser) {
        UserSimpleInfo userSimpleInfo = new UserSimpleInfo();
        userSimpleInfo.setUserId(mcdSysUser.getUserId());
        userSimpleInfo.setCityId(mcdSysUser.getCityId());
        userSimpleInfo.setPrivince(mcdSysUser.getProvince());
        userSimpleInfo.setUserName(mcdSysUser.getUserName());
        userSimpleInfo.setDepartmentId(mcdSysUser.getDepartmentId());
        userSimpleInfo.setRoleIds("");
        return userSimpleInfo;
    }

    /**
     * 政企保存活动信息
     *
     * @param object
     * @param task
     * @return
     * @throws ParseException
     */
    private String saveZqCamp(Object[] object, McdCampImportTask task, UserSimpleInfo user) throws Exception {
        if (object == null || object.length < 16) {
            throw new Exception("数据不完整");
        }
        String campsegName = (String) object[0];
        String campsegDesc = (String) object[1];
        String custGroupId = (String) object[2];
        String planId = (String) object[3];
        String startDate = (String) object[7];
        String endDate = (String) object[8];
        String campsegType = (String) object[9];
        String activityType = (String) object[10];
        String activityObjective = (String) object[11];
        String channelId = (String) object[13];
        String channelAdivId = (String) object[14];
        String execContent = (String) object[15];
        // 融合产品
        String fixPlanIds = (String) object[4];
        // 同系列产品
        String evaluationPlanIds = (String) object[5];
        // 互斥产品
        String exPlanIds = (String) object[6];
        // 是否预演
        String isPreview = (String) object[12];
        ChannelConfService confService = servcieHolder.getService(channelId);
        TacticsInfoJx req = new TacticsInfoJx();
        // 策略创建类型: 2-批量导入
        req.setType(1);
        // 默认不提交
        req.setIsSubmit(0);
        CampBaseInfoJxVO baseInfo = new CampBaseInfoJxVO();
        // 活动名称
        baseInfo.setCampsegName(campsegName);
        // 活动描述
        baseInfo.setCampsegDesc(campsegDesc);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 活动开始时间
        baseInfo.setStartDate(sdf.parse(startDate));
        // 活动结束时间
        baseInfo.setEndDate(sdf.parse(endDate));
        // rootId
        baseInfo.setCampsegRootId("0");
        baseInfo.setCampsegId(IdUtils.generateId());
        baseInfo.setCampDefType(1);
        // 创建人
        baseInfo.setCreateUserId(task.getCreateUser());
        // 创建时间
        baseInfo.setCreateTime(new Date());
        baseInfo.setTacticsMap("[{\"group\":[\"1\",\"2\",\"3\"],\"children\":[]}]");
        // 策略类型
        baseInfo.setCampsegTypeId(Integer.valueOf(campsegType.split(":")[0]));
        // 活动类型
        baseInfo.setActivityType(activityType.split(":")[0]);
        // 活动目的
        baseInfo.setActivityObjective(activityObjective.split(":")[0]);
        // 是否预演
        String[] previewItems = isPreview.split(":");
        baseInfo.setPreviewCamp(previewItems[0]);
        List<PlanExtInfo> extInfos = new ArrayList<>();
        PlanExtInfo planExtInfo = new PlanExtInfo();
        planExtInfo.setPlanId(planId);
        extInfos.add(planExtInfo);
        req.setPlanExtInfoList(extInfos);
        // 融合产品
        planExtInfo.setCampFusionPlans(getPlanBaseInfos(fixPlanIds));
        // 同系列产品
        planExtInfo.setCampSeriesPlan(getPlanBaseInfos(evaluationPlanIds));
        //  互斥产品
        planExtInfo.setCampExclusivePlan(getPlanBaseInfos(exPlanIds));

        req.setBaseCampInfo(baseInfo);
        List<CampScheme> campSchemes = new ArrayList<>();
        req.setCampSchemes(campSchemes);
        CampScheme campScheme = new CampScheme();
        campSchemes.add(campScheme);
        // 子活动基本信息
        CampBaseInfoVO baseCampInfo = new CampBaseInfoVO();
        // 将父活动信息复制到子活动
        BeanUtil.copyProperties(baseInfo, baseCampInfo);
        // 重新生成子活动ID
        baseCampInfo.setCampsegId(IdUtils.generateId());
        // 子活动rootId
        baseCampInfo.setCampsegRootId(baseInfo.getCampsegId());
        campScheme.setBaseCampInfo(baseCampInfo);

        // 产品信息
        List<PlanDefVO> product = new ArrayList<>();
        PlanDefVO planDefVO = new PlanDefVO();
        planDefVO.setPlanId(planId);
        product.add(planDefVO);
        campScheme.setProduct(product);

        // 客户群信息
        List<CustgroupDetailVO> customer = new ArrayList<>();
        CustgroupDetailVO custgroupDetailVO = new CustgroupDetailVO();
        custgroupDetailVO.setCustomGroupId(custGroupId);
        // 默认为一次性
        custgroupDetailVO.setUpdateCycle(1);
        customer.add(custgroupDetailVO);
        campScheme.setCustomer(customer);

        // 渠道信息
        List<ChannelInfo> channels = new ArrayList<>();

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannelId(channelId);
        CampChannelConfQuery channelConf = new CampChannelConfQuery();
        channelInfo.setChannelConf(channelConf);
        channelConf.setExecContent(execContent);

        // 渠道扩展信息
        CampChannelExtQuery extConf = confService.getChannelExtConf(object, task);
        // 内容短信默认频次
        if(ChannelConfServiceImpl801.CHANNEL_ID.equals(channelId)){
            channelConf.setFrequency(extConf.getColumnExt17());
            extConf.setColumnExt17(null);
        }
        channelConf.setChannelExtConf(extConf);
        channels.add(channelInfo);
        campScheme.setChannels(channels);

        // 运营位
        McdDimAdivInfo adivInfo = new McdDimAdivInfo();
        adivInfo.setAdivId(channelAdivId);
        channelInfo.setAdivInfo(adivInfo);

        List<CampChildrenScheme> childrenSchemes = new ArrayList<>();
        req.setChildrenSchemes(childrenSchemes);
        CampChildrenScheme campChildrenScheme = new CampChildrenScheme();
        childrenSchemes.add(campChildrenScheme);
        // 子活动ID
        campChildrenScheme.setCampsegId(IdUtils.generateId());
        // 产品id，客群id，渠道id，运营位id]
        List<String> data = new ArrayList<>();
        campChildrenScheme.setData(data);
        // 产品ID
        data.add(planDefVO.getPlanId());
        // 客群id
        data.add(custGroupId);
        // 渠道id
        data.add(channelInfo.getChannelId());
        // 运营位id
        data.add(channelInfo.getAdivInfo().getAdivId());
        // 保存活动
        campsegService.saveCamp(req, user);
        // 更新活动创建类开为 ,导入-2
        LambdaUpdateWrapper<com.asiainfo.biapp.pec.plan.model.McdCampDef> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(com.asiainfo.biapp.pec.plan.model.McdCampDef::getCampCreateType, ConstantCamp.CAMP_CREATE_TYPE_IMPORT).eq(com.asiainfo.biapp.pec.plan.model.McdCampDef::getCampsegRootId, baseInfo.getCampsegId()).or().eq(com.asiainfo.biapp.pec.plan.model.McdCampDef::getCampsegId, baseInfo.getCampsegId());
        campDefService.update(wrapper);
        return campChildrenScheme.getCampsegId();
    }

    /**
     * 初始化接口参数
     *
     * @param params
     * @param user
     * @return
     */
    private GridRequestParameters initParameters(net.sf.json.JSONObject params, UserSimpleInfo user) {
        // net.sf.json.JSONObject params = this.getParams(request);
        GridRequestParameters requestParameters = new GridRequestParameters();
        // 获取接口传递过来的参数

        String subitemName = params.has("subitemName") ? params.getString("subitemName") : ""; // 角色ID
        String startDate = params.has("startTime") ? params.getString("startTime") : ""; // 策略开始时间
        String endDate = params.has("endTime") ? params.getString("endTime") : ""; // 策略结束时间
        String createUserId = params.has("creatorId") ? params.getString("creatorId") : ""; // 创建人
        String planId = params.has("offerCode") ? params.getString("offerCode") : ""; // 产品编码

        String execText = params.has("execText") ? params.getString("execText") : ""; // 营销用语
        String approveId = params.has("approveId") ? params.getString("approveId") : ""; // 审批人ID
        //String taskId = params.has("taskId") ? params.getString("taskId") : ""; // 任务编码
        String subitemTarget = params.has("subitemTarget") ? params.getString("subitemTarget") : ""; // 活动目的
        String businessClass = params.has("businessClass") ? params.getString("businessClass") : ""; // 活动类型
        String campsegNature = params.has("subitemNature") ? params.getString("subitemNature") : ""; // 活动性质

        String createUserName = ""; // 创建人名称
        String createCityId = ""; // 创建人归属地市
        String createDeptId = ""; // 创建人归属部门

        if (user != null) {
            createUserName = user.getUserName(); // 创建人名称
            createCityId = user.getCityId(); // 创建人归属地市
            createDeptId = user.getDepartmentId(); // 创建人归属部门
            // requestParameters.setTestPhones(user.getMobilePhone());//测试号码
        }
        HashMap<String, Object> paramsMap = params.has("extend") ? JSON.parseObject(params.getString("extend"), HashMap.class) : new HashMap<String, Object>(); // 扩展字段
        String positionId = paramsMap.get("positionId") == null ? "1" : paramsMap.get("positionId").toString();//运营位
        String channelId = paramsMap.get("channelId") == null ? "" : paramsMap.get("channelId").toString();//渠道ID
        String channelName = paramsMap.get("channelName") == null ? "" : paramsMap.get("channelName").toString();//渠道名称
        String subitemId = paramsMap.get("subitemId") == null ? "" : paramsMap.get("subitemId").toString();//活动编码
        String exeContent = paramsMap.get("exeContent") == null ? execText : paramsMap.get("exeContent").toString(); //推荐用语
        String optType = paramsMap.get("optType") == null ? "0" : paramsMap.get("optType").toString(); //操作类型
        String cepEventId = paramsMap.get("cepEventId") == null ? "" : paramsMap.get("cepEventId").toString(); //事件ID
        String cepEventName = paramsMap.get("cepEventName") == null ? "" : paramsMap.get("cepEventName").toString(); //事件名称
        String campTypeId = paramsMap.get("campTypeId") == null ? "1" : paramsMap.get("campTypeId").toString();// 策略类型

        String campsegName = StringUtils.isNotEmpty(subitemName) ? subitemName : "政企集团运营策略" + QuotaUtils.getDayDate("yyyyMMddHHmmss");
        requestParameters.setMarketingType("0");//类型
        requestParameters.setPushType("1");//推送类型 1全量
        requestParameters.setCampsegName("【政企策略】"+campsegName);//活动名称
        requestParameters.setChannelId(channelId);//渠道ID
        requestParameters.setChannelName(channelName);//渠道名称
        requestParameters.setAdivId(channelId + positionId); // 运营位默认1
        requestParameters.setOptType(optType); //操作类型
        requestParameters.setCampsegId(subitemId); //活动编码

        requestParameters.setExecText(exeContent); //推荐用语

        String createTime = QuotaUtils.getDayDate("yyyy-MM-dd HH:mm:ss"); // 创建时间,当前时间

        requestParameters.setCreateType("8");//活动标识 政企的活动
        requestParameters.setOptionSign("8");//活动标识 政企的活动
        requestParameters.setStartTime(startDate);//开始时间
        requestParameters.setEndTime(endDate);//结束时间
        requestParameters.setCreateorId(createUserId);//创建人
        requestParameters.setCreateUserName(createUserName);//创建人名称
        requestParameters.setCreateTime(createTime);//创建时间
        requestParameters.setCreateCityId(createCityId);//地市
        requestParameters.setCreateDeptId(createDeptId);//部门
        requestParameters.setPlanId(planId);//产品ID
        requestParameters.setApproveId(approveId);//审批人

        requestParameters.setSubitemNature(campsegNature);//活动性质
        requestParameters.setSubitemTarget(subitemTarget);//活动目的
        requestParameters.setBusinessClass(businessClass);//活动类型
        requestParameters.setCampTypeId(campTypeId);//策略类型
        requestParameters.setCampPreview("0");//策略预演
        // requestParameters.setApproveInfo(getApproveInfos(requestParameters,createUser));//活动审批信息

        requestParameters.setChannelExt(paramsMap);
        requestParameters.setCepEventId(cepEventId);
        requestParameters.setCepEventName(cepEventName);


        return requestParameters;
    }

    /**
     * 对参数进行验证
     *
     * @param requestParameters
     * @throws Exception
     */
    private void validateParameters(GridRequestParameters requestParameters) throws Exception {
        // 创建人编码为空验证
        if (StringUtils.isEmpty(requestParameters.getCreateorId())) {
            throw new Exception("参数错误：创建人编码为空！");
        }
        // 策略名称为空验证
        if (StringUtils.isEmpty(requestParameters.getCampsegName())) {
            throw new Exception("参数错误：策略名称为空！");
        }
        // 策略开始时间为空验证，日期格式验证yyyy-MM-dd
        if (StringUtils.isEmpty(requestParameters.getOptType())) {
            throw new Exception("参数错误：活动操作类型为空！");
        }
        // 策略开始时间为空验证，日期格式验证yyyy-MM-dd
        if (StringUtils.isEmpty(requestParameters.getStartTime())) {
            throw new Exception("参数错误：策略开始时间为空！");
        }
        // 策略结束时间为空验证，日期格式验证yyyy-MM-dd
        if (StringUtils.isEmpty(requestParameters.getEndTime())) {
            throw new Exception("参数错误：策略结束时间为空！");
        }
        if (ToolUtils.getCurrentDate().compareTo(requestParameters.getEndTime()) > 0) {
            throw new Exception("参数错误：策略结束时间小于当前时间！");
        }
        if (requestParameters.getEndTime().compareTo(requestParameters.getEndTime()) < 0) {
            throw new Exception("参数错误：策略结束时间小于开始时间！");
        }
        // 产品编码为空验证
        if (StringUtils.isEmpty(requestParameters.getPlanId())) {
            throw new Exception("参数错误：产品编码为空！");
        }
        // 策略名称为空验证
        if (StringUtils.isEmpty(requestParameters.getSubitemTarget())) {
            throw new Exception("参数错误: 活动目的为空！");
        }
    }

    /**
     * 将产品字符串转为集合
     *
     * @param planIds
     * @return
     */
    List<PlanBaseInfo> getPlanBaseInfos(String planIds) {
        List<PlanBaseInfo> list = new ArrayList<>();
        if (StrUtil.isEmpty(planIds)) {
            return list;
        }
        for (String planId : planIds.split(",")) {
            PlanBaseInfo baseInfo = new PlanBaseInfo();
            baseInfo.setPlanId(planId);
            list.add(baseInfo);
        }
        return list;
    }

    /**
     * 初始化接口参数
     * @param request
     * @return
     */
    private McdCampDef initSyncParameters(HttpServletRequest request) {
        net.sf.json.JSONObject params = this.getParams(request);
        McdCampDef campInfo = new McdCampDef();
        log.info("政企审批回调请求入参: "+ params);

        // 获取接口传递过来的参数

        //此处子编码是mcd_camp_channel_list的CAMPSEG_ID
        // 活动编码
        String subitemId = StringUtils.isBlank(params.getString("subitemId"))?"":params.getString("subitemId");
        // 渠道信息
        String channelId= StringUtils.isBlank(params.getString("channelId"))?"":params.getString("channelId");
        // 审批结果
        String approveResult = StringUtils.isBlank(params.getString("approveResult"))?"":params.getString("approveResult");
        // 审批人
        String approveUserId = StringUtils.isBlank(params.getString("approveUserId"))?"":params.getString("approveUserId");
        // 审批意见
        String approveResultDesc = StringUtils.isBlank(params.getString("approveResultDesc"))?"":params.getString("approveResultDesc");

        if (StringUtils.isNotBlank(subitemId) && !"null".equals(subitemId)){
            try {
                campInfo = enterpriseService.getCampSegInfo(subitemId);
                campInfo.setChannelId(channelId);
                campInfo.setSubitemId(subitemId);
                campInfo.setApproveResultDesc(approveResultDesc);
                campInfo.setApproveResult(Integer.valueOf(approveResult));
                campInfo.setCampsegStatId(Integer.valueOf(MPM_CAMPSEG_STAT_DDCG));
            }catch (Exception e){
                log.error(" 政企接口根据创建人ID查询用户信息失败" ,e);
                return campInfo;
            }
        }else {
            log.error(" 政企创建活动入参没有创建人信息!");
            return campInfo;
        }
        return campInfo;
    }


}





























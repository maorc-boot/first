package com.asiainfo.biapp.pec.plan.jx.channelconfig.controller;


import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.service.IMcdSensitiveService;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdChannel;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdCustgroup;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdsettingHttpVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/action/sensitive")
@RestController
@Api(tags = "江西:渠道敏感客户群配置")
public class SensitiveController {

    @Autowired
    IMcdSensitiveService mcdSensitiveService;
    @Autowired
     private HttpServletRequest request;

    /**
     * 展示渠道列表（选渠道模块）
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "查询展示全部渠道")
    @RequestMapping(value = "/getChannels", method = {RequestMethod.POST})
    public List<McdChannel> getChannels(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mcdSensitiveService.getChannels();
    }

    /**
     * 查询渠道敏感客户群配置
     * @return
     */
    @ApiOperation(value = "查询渠道敏感客户群配置")
    @RequestMapping(value = "/queryChannelSetting", method = {RequestMethod.POST})
    public Map<String, Object> queryChannelSetting(@RequestBody McdsettingHttpVo mcdsettingHttpVo) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            final UserSimpleInfo user = UserUtil.getUser(request);
            Pager pager = new Pager();
            int pageSize = StringUtils.isNotBlank(mcdsettingHttpVo.getPageSize()) ? Integer.parseInt(mcdsettingHttpVo.getPageSize()) : 10;
            String pageNum = StringUtils.isNotBlank(mcdsettingHttpVo.getPageNum()) ? mcdsettingHttpVo.getPageNum() : "0";
            String channelId = StringUtils.isNotBlank(mcdsettingHttpVo.getChannelId()) ? mcdsettingHttpVo.getChannelId() : "";

            pager.setPageNum(pageNum);
            pager.setPageSize(pageSize);
            if (StringUtils.isNotEmpty(pageNum)) {
                pager.setPageFlag("G");
            }
            pager.setTotalSize(mcdSensitiveService.queryChannelSettingCnt(channelId));
            pager.getTotalPage();

            List<Map<String, Object>> dataList = mcdSensitiveService.queryChannelSetting(user.getUserId(), channelId, pager);
            pager = pager.pagerFlip();
            pager.setResult(dataList);

            dataMap.put("status", "200");
            dataMap.put("data", pager);
        } catch (Exception e) {
            log.error("查询queryChannelSetting--", e);
            throw e;
        }
        return dataMap;
    }

    /**
     * 保存敏感配置
     * @return
     */
    @ApiOperation("保存敏感配置")
    @RequestMapping(value = "/saveConfig", method = {RequestMethod.POST})
    public String saveConfig(@RequestBody McdsettingHttpVo mcdsettingHttpVo) throws Exception {
        UserSimpleInfo user = UserUtil.getUser(request);
        String channelId = mcdsettingHttpVo.getChannelId();
        String khqList = mcdsettingHttpVo.getKhqList();
        boolean flag = mcdSensitiveService.saveConfig(user.getUserId(), channelId, khqList);
        if (flag) return "1";
        return "0";
    }

    /**
     * 查询客群
     * @return
     * @throws Exception
     */
    @ApiOperation("查询客群")
    @RequestMapping(value = "/getMoreCustom", method = {RequestMethod.POST})
    public Pager getMoreCustom(@RequestBody McdsettingHttpVo mcdsettingHttpVo) throws Exception {
        List<McdCustgroup> resultList = null;
        Pager pager = new Pager();
        UserSimpleInfo user = UserUtil.getUser(request);
        String keyWords = StringUtils.isNotBlank(mcdsettingHttpVo.getKeyWords()) ? mcdsettingHttpVo.getKeyWords() : null;
        int pageSize = StringUtils.isNotBlank(mcdsettingHttpVo.getPageSize()) ? Integer.parseInt(mcdsettingHttpVo.getPageSize()) : 10;
        String pageNum = StringUtils.isNotBlank(mcdsettingHttpVo.getPageNum()) ? mcdsettingHttpVo.getPageNum() : "0";
        try {
            int size = mcdSensitiveService.getMoreMyCustomCount(user.getUserId(), keyWords);
            pager.setPageSize(pageSize);
            pager.setPageNum(pageNum); // 当前页
            pager.setTotalSize(size);
            resultList = mcdSensitiveService.getMoreMyCustom(user.getUserId(), keyWords, pager);
            pager.setResult(resultList);

        } catch (Exception e) {
            log.error("", e);
        }
        return pager;
    }

    /**
     * 删除敏感客群配置
     * @return
     */
    @ApiOperation("删除敏感客群配置")
    @RequestMapping(value = "/deleteSettings", method = {RequestMethod.POST})
    public String deleteSettings(@RequestBody McdsettingHttpVo mcdsettingHttpVo) {
        String channelId = mcdsettingHttpVo.getChannelId();
        String khqId = mcdsettingHttpVo.getKhqList();
        boolean flag = mcdSensitiveService.deleteSettings(channelId, khqId);
        if (flag) return "1";
        return "0";
    }

}

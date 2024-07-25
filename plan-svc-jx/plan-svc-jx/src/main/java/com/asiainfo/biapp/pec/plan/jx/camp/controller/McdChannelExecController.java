package com.asiainfo.biapp.pec.plan.jx.camp.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdChannelExecCapacityModel;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdChannelExecInfoModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdChannelExecQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdLogChnExpQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdChannelExecCapacityService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.McdChannelExecInfoService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.feign.McdLogChnExpFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "江西渠道下发量")
@RequestMapping("/mcd/camp/channelExec")
@Slf4j
@RestController
public class McdChannelExecController {

    private final String CHANNELIDS = "801,960,964";

    @Resource
    private McdChannelExecInfoService mcdChannelExecInfoService;
    @Resource
    private McdChannelExecCapacityService mcdChannelExecCapacityService;
    @Resource
    private McdLogChnExpFeignClient mcdLogChnExpFeignClient;

    @ApiOperation("活动策划渠道下发情况查询")
    @PostMapping("/queryChannelExecInfo")
    public ActionResponse queryChannelExecInfo(@RequestBody McdChannelExecQuery req){

        String channelId = req.getChannelId();
        if (StringUtils.isEmpty(channelId)){
            return ActionResponse.getFaildResp("入参渠道ID为空!");
        }
        return ActionResponse.getSuccessResp(mcdChannelExecInfoService.queryChannelExecInfo(req));
    }

    @ApiOperation("活动详情渠道下发情况查询")
    @PostMapping("/queryChannelExecList")
    public ActionResponse queryChannelExecList(@RequestBody McdChannelExecQuery req){

        String campsegId = req.getCampsegId();
        if (StringUtils.isEmpty(campsegId)){
            return ActionResponse.getFaildResp("入参策略ID为空!");
        }
        return ActionResponse.getSuccessResp(mcdChannelExecInfoService.queryChannelExecList(req));
    }


    //渠道今日营销情况统计任务
    @PostMapping("/channelExecStatisticsTask")
    public void channelExecStatisticsTask(){

        log.info("渠道今日营销情况统计任务channelExecStatisticsTask开始!");

         List<Map<String, Object>> allChnList = mcdChannelExecInfoService.queryCustNumByChannel();

        List<String> channelList = new ArrayList<>();
         McdLogChnExpQuery req = new McdLogChnExpQuery();
        for (Map<String, Object> map : allChnList) {

            String channelId = map.get("CHANNEL_ID") == null ? "":map.get("CHANNEL_ID").toString();
            String allCustNum = map.get("CUSTOM_NUM") == null ? "0":map.get("CUSTOM_NUM").toString();
            if (StringUtils.isEmpty(channelId)){
                continue;
            }

            channelList.add(channelId);

            //查询已营销
            req.setChannelId(channelId);
            req.setLogTime(getDateStr("yyyy-MM-dd"));
            ActionResponse actionResponse = mcdLogChnExpFeignClient.queryLogChnExpNum(req);
            int execNum =  actionResponse.getData() == null? 0: (int)actionResponse.getData();

            McdChannelExecInfoModel execInfoModel = new McdChannelExecInfoModel();
            execInfoModel.setChannelId(channelId);
            execInfoModel.setExecNum(execNum);//已营销
            execInfoModel.setNoExecNum(Integer.parseInt(allCustNum) - execNum);//待营销
            mcdChannelExecInfoService.saveOrUpdate(execInfoModel);
        }

        if (channelList.size()< 3){
            String channelIds = RedisUtils.getDicValue("MCD_CHANNEL_EXEC_BY_IDS") == null ?
                    CHANNELIDS:RedisUtils.getDicValue("MCD_CHANNEL_EXEC_BY_IDS");

            String[]  chans = channelIds.split(",");
            List<String> configChnList = Arrays.asList(chans);
            for (String channelId : configChnList) {
                if (!channelList.contains(channelId)){
                    req.setChannelId(channelId);
                    req.setLogTime(getDateStr("yyyy-MM-dd"));
                    ActionResponse actionResponse = mcdLogChnExpFeignClient.queryLogChnExpNum(req);
                    int execNum =  actionResponse.getData() == null? 0: (int)actionResponse.getData();

                    McdChannelExecInfoModel execInfoModel = new McdChannelExecInfoModel();
                    execInfoModel.setChannelId(channelId);
                    execInfoModel.setExecNum(execNum);//已营销
                    execInfoModel.setNoExecNum(0);//待营销
                    mcdChannelExecInfoService.saveOrUpdate(execInfoModel);
                }
            }
        }

        log.info("渠道今日营销情况统计任务channelExecStatisticsTask完成!");
    }


    //渠道今日营销情况统计任务
    @PostMapping("/channelExecMaxNumTask")
    public void channelExecMaxNumTask(){

        log.info("渠道日营销最大量统计任务channelExecMaxNumTask开始!");
        String channelIds = RedisUtils.getDicValue("MCD_CHANNEL_EXEC_BY_IDS") == null ?
                CHANNELIDS:RedisUtils.getDicValue("MCD_CHANNEL_EXEC_BY_IDS");
        String[] chns = channelIds.split(",");
        McdLogChnExpQuery req = new McdLogChnExpQuery();
        req.setLogTime(getMonthFirstDay("yyyy-MM-dd"));

        for (String chn : chns) {

            req.setChannelId(chn);
            ActionResponse actionResponse = mcdLogChnExpFeignClient.queryLogChnExpMaxNum(req);
            int execCapacity =  actionResponse.getData() == null? 0: (int)actionResponse.getData();

            McdChannelExecCapacityModel capacityModel = mcdChannelExecCapacityService.getById(chn);
            if (Objects.isNull(capacityModel)){
                capacityModel = new McdChannelExecCapacityModel();
            }
            if ( execCapacity < capacityModel.getExecCapacity()){
                log.info("渠道"+chn+"日营销量已为最大值,保持不变!");
                continue;
            }

            capacityModel.setExecCapacity(execCapacity);
            capacityModel.setChannelId(chn);
            mcdChannelExecCapacityService.saveOrUpdate(capacityModel);

        }
        log.info("渠道日营销最大量统计任务channelExecMaxNumTask完成!");

    }


    //获取当前时间
    private String getDateStr(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateStr);
        String sgTimestamp = simpleDateFormat.format(new Date());
        return sgTimestamp;
    }

    //获取上月第一天
    private String  getMonthFirstDay(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat(dateStr);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MONTH,-1);
        rightNow.set(Calendar.DAY_OF_MONTH,1);
        String firstDay = format.format(rightNow.getTime());
        return firstDay;
    }
}

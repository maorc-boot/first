package com.asiainfo.biapp.pec.plan.jx.camp.service.feign;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdLogChnExpQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdSmsSendInfoQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.SingleUserDetailQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author ranpf
 * @date 2023/3/1
 */
@FeignClient(value = "pec-log")
public interface McdLogChnExpFeignClient {

    /**
     * 渠道今日已营销查询
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/mcd/logChn/exp/queryLogChnExpNum")
    ActionResponse queryLogChnExpNum(@RequestBody McdLogChnExpQuery req);

    /**
     * 渠道日最大营销量查询
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/mcd/logChn/exp/queryLogChnExpMaxNum")
    ActionResponse queryLogChnExpMaxNum(@RequestBody McdLogChnExpQuery req);

    /**
     * 短信发送记录
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/mcd/logChn/exp/querySmsSendNum")
    ActionResponse querySmsSendNum(@RequestBody McdSmsSendInfoQuery req);


    /**
     * 短信发送免打扰过滤数据
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/mcd/logChn/exp/querySmsSendBotherNum")
    ActionResponse querySmsSendBotherNum(@RequestBody McdSmsSendInfoQuery req);


    /**
     * 短信发送频次过滤数据
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/mcd/logChn/exp/querySmsSendFqcNum")
    ActionResponse querySmsSendFqcNum(@RequestBody McdSmsSendInfoQuery req);


    /**
     * 渠道今日已营销查询
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/singleUser/data/queryChannelInfo")
    ActionResponse queryChannelInfo(@RequestBody SingleUserDetailQuery req);


    /**
     * 渠道今日已营销查询
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/singleUser/data/queryMarketingResultDetail")
    ActionResponse queryMarketingResultDetail(@RequestBody SingleUserDetailQuery req);


    /**
     * 渠道今日已营销查询
     *
     * @param req
     * @return
     */
    @PostMapping("/pec-log/singleUser/data/queryChannelExeLogInfo")
    ActionResponse queryChannelExeLogInfo(@RequestBody SingleUserDetailQuery req);

}

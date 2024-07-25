package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service;

import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveGAppIdAppSecretRelVo;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.QueryTmpListRequestVo;

import java.util.List;

/**
 * description: 5G消息渠道service
 *
 * @author: lvchaochao
 * @date: 2022/12/22
 */
public interface IFiveGMsgChannelService {

    /**
     * 获取5G应用号密钥映射数据
     *
     * @param channelId 渠道id
     * @return List<FiveGAppIdAppSecretRelVo>
     */
    List<FiveGAppIdAppSecretRelVo> queryApplicationNumInfo(String channelId);

    /**
     * 获取模板列表
     *
     * @param requestVo requestVo
     * @return {@link String}
     */
    String getTemplateList(QueryTmpListRequestVo requestVo);
}

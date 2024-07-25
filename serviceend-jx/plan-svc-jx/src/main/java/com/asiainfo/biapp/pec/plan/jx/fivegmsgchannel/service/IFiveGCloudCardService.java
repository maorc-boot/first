package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service;

import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dto.QryTmpListReqDto;

/**
 * description: 5G云卡渠道service
 *
 * @author: lvchaochao
 * @date: 2023/9/12
 */
public interface IFiveGCloudCardService {

    /**
     * 查询模板列表
     *
     * @param reqDto 请求入参对象
     * @return {@link String}
     */
    String getTemplateList(QryTmpListReqDto reqDto);
}

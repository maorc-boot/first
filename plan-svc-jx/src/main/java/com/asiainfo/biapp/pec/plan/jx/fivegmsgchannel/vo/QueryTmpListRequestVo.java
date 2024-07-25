package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo;

import lombok.Data;

/**
 * description: 请求获取5G模板列表的入参对象
 *
 * @author: lvchaochao
 * @date: 2022/12/29
 */
@Data
public class QueryTmpListRequestVo {

    private String chatbotId;

    private String appId;

    private String appSecret;

    private String grantType = "client_credential";

    /**
     * 模板类型 1 静态、 2 交互动态、 3 主动动态、 4 空白
     */
    private Integer templateType;

    /**
     * 模板内容类型：1-文本，2-多媒体，3-卡片，4-组合
     */
    private Integer contentType;

    /**
     * 审核结果：-1-未提交审核，0-审核通过，1审核不通过，999-审核中
     */
    private Integer result;

    /**
     * 模板名称关键词，传入对应的关键词，即可查找名称符合对应关键词的素材。
     */
    private String templateName;

    private Integer pageNum = 1;

    private Integer pageSize = 10;



}

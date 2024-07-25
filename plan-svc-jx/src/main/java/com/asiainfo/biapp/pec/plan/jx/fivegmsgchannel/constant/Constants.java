package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.constant;

/**
 * description: 5g消息常量类
 *
 * @author: lvchaochao
 * @date: 2023/2/10
 */
public class Constants {

    /**
     * token 获取地址
     */
    public static final String API_TOKEN = "/token/getAccessToken?grant_type=client_credential&appid={APPID}&secret={SECRET}";

    /**
     * 模板列表获取地址
     */
    public static final String API_TEMPLATE_LIST = "/template/query/sip:{chatbotId}@botplatform.rcs.chinamobile.com/requests";

    /**
     * 占位符部分
     */
    public static final String PLACEHOLDER_APPID = "{APPID}";
    public static final String PLACEHOLDER_SECRET = "{SECRET}";
    public static final String PLACEHOLDER_CHATBOT_ID = "{chatbotId}";
    public static final String PLACEHOLDER_ID = "{id}";

    public static final String RESP_SUCCESS_CODE0 = "0";

    public static final String RESP_PROP_DATA = "data";
    public static final String RESP_PROP_CODE = "code";
    public static final String RESP_PROP_MSG = "msg";

    // 5G云卡接口响应常量
    public static final String RESP_PROP_STATUS = "status";
    public static final String RESP_PROP_CONTENT = "content";
}

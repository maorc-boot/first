package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.enumtype;

import lombok.Getter;

/**
 * 5g订阅号相关配置枚举类
 *
 * @author lvcc
 * @date 2023/02/10
 */
@Getter
public enum Msg5GConfigEnum {

    SERVER_ROOT("MSG_SVC_5G_SERVER_ROOT","MSG_5G_SERVER_ROOT","SERVER_ROOT_URL","5g消息平台服务地址"),
    APP_ID("MSG_SVC_5G_APP_ID", "","APPID","5g订阅号AppID"),
    APP_SECRET("MSG_SVC_5G_APP_SECRET", "","SECRET","5g订阅号秘钥"),
    CHATBOT("MSG_SVC_5G_CHATBOT","CHATBOT","CHATBOT_ID", "5g订阅号ID"),
    AUTHORIZATION("MSG_SVC_5G_AUTHORIZATION","","", "5g消息验证头"),
    CHATBOT_URL("MSG_SVC_5G_CHATBOT_URL", "CHATBOT","CHATBOT_ID","5g订阅号URL");

    private String key;
    private String cfgType;
    private String cfgName;
    private String desc;

    Msg5GConfigEnum(String key, String cfgType, String cfgName, String desc) {
        this.key = key;
        this.cfgType = cfgType;
        this.cfgName = cfgName;
        this.desc = desc;
    }


}

package com.asiainfo.biapp.auth.jx.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mamp
 * @date 2022/10/9
 */
@Data
public class LoginQueryJx {
    @ApiModelProperty(value = "客户端ID", required = true)
    private String clientId;

    @ApiModelProperty(value = "客户端秘钥", required = true)
    private String clientSecret;

    @ApiModelProperty(value = "授权类型，固定值password", required = true)
    private String grantType;

    @ApiModelProperty(value = "4A门户带过来的Token", required = false)
    private String token;

    @ApiModelProperty(value = "4A门户带过来的账号ID", required = false)
    private String appAcctId;

    /**
     * EmisCas单点登录 ticket
     */
    @ApiModelProperty(value = " EmisCas单点登录 ticket", required = false)
    private String ticket;

    @ApiModelProperty(value = "IOP4传的用户ID", required = false)
    private String userId;
}

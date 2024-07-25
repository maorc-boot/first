package com.asiainfo.biapp.pec.plan.jx.webservice.impl;


import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.plan.jx.user.service.User4AManageService;
import com.asiainfo.biapp.pec.plan.jx.user.vo.Head;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserInfo4A;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserModifyReq;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserModifyReqBody;
import com.asiainfo.biapp.pec.plan.jx.utils.JaxbUtil;
import com.asiainfo.biapp.pec.plan.jx.webservice.Ws4AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Ws4AServiceImpl implements Ws4AService {

    @Resource
    private User4AManageService user4AManageService;


    @Override
    public String UpdateAppAcctSoap(String infoXml) {
        log.info("UpdateAppAcctSoap:{}", infoXml);
        String result = "操作失败";
        String resultCode = "0";
        String userId = "";
        String loginNo = "";
        String modifyMode = "";
        String code = "";
        String sid = "";
        String serviceId = "";
        JaxbUtil jaxbUtil = new JaxbUtil(UserModifyReq.class);
        UserModifyReq userModifyReq = jaxbUtil.fromXml(infoXml);
        if (userModifyReq != null) {
            Head head = userModifyReq.getHead();
            code = head.getCode();
            sid = head.getSid();
            serviceId = head.getServiceId();
            UserModifyReqBody userModifyReqBody = userModifyReq.getBody();
            String operatorId = userModifyReqBody.getOperatorId();
            modifyMode = userModifyReqBody.getModifyMode();
            UserInfo4A userInfo4A = userModifyReqBody.getUserInfo();
            userId = userInfo4A.getUserId();
            loginNo = userInfo4A.getLoginNo();
            String password = userInfo4A.getPassword();
            String status = userInfo4A.getStatus();
            if(StrUtil.isEmpty(userId)){
                userId = loginNo;
                userInfo4A.setUserId(userId);
            }
            int count = -1;
            if ("delete".equals(modifyMode)) {
                List<Map<String, Object>> list = user4AManageService.getUserByUserId(userId);
                user4AManageService.deleteUserById4A(userId);
                if (list != null && list.size() > 0) {
                    // String userid = (String) list.get(0).get("USERID");
                    count = user4AManageService.deleteUserInfoByUserId(userId);
                    if (count > -1) {
                        result = "删除成功";
                        resultCode = "200";
                    } else {
                        result = "删除失败";
                    }
                } else {
                    result = "用户ID不存在";
                }
            } else if ("resetpwd".equals(modifyMode)) {
                List<Map<String, Object>> list = user4AManageService.getUserByUserId(userId);
                if (list != null && list.size() > 0) {
                    count = user4AManageService.updateUserPwd4A(userId, password);
                    if (count > -1) {
                        result = "密码修改成功";
                        resultCode = "200";
                    } else {
                        result = "密码修改失败";
                    }
                } else {
                    result = "用户ID不存在";
                }
            } else if ("chgstatus".equals(modifyMode)) {
                List<Map<String, Object>> list = user4AManageService.getUserByUserId(userId);
                if (list != null && list.size() > 0) {
                    if ("0".equals(status) || "1".equals(status)) {
                        count = user4AManageService.updateStatus4A(userId, status);
                        if (count > -1) {
                            result = "用户状态修改成功";
                            resultCode = "200";
                        } else {
                            result = "用户状态修改失败";
                        }
                    } else {
                        result = "输入的状态不属于可输入状态";
                    }
                } else {
                    result = "用户ID不存在";
                }
            } else if ("add".equals(modifyMode) || "change".equals(modifyMode)) {
                if ("add".equals(modifyMode)) {
                    userId = userInfo4A.getLoginNo();
                    userInfo4A.setUserId(userId);
                    log.info(userInfo4A.toString());
                }
                Map<String, String> resultMap = user4AManageService.saveOrUpdateUserInfo4A(userInfo4A, modifyMode);
                resultCode = resultMap.get("SUCCESS");
                result = resultMap.get("msg");
            } else {
                result = "输入的操作类型不属于可输入操作类型";
            }
        }
        if ("0".equals(resultCode)) {
            resultCode = "1";
        } else {
            resultCode = "0";
        }
        StringBuffer returnXml = new StringBuffer();
        returnXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<USERMODIFYRSP>")
                .append("<HEAD>")
                .append("<CODE>" + code + "</CODE>")
                .append("<SID>" + sid + "</SID>")
                .append("<TIMESTAMP>" + new Timestamp(System.currentTimeMillis()) + "</TIMESTAMP>")
                .append("<SERVICEID>" + serviceId + "</SERVICEID>")
                .append("</HEAD>")
                .append("<BODY>")
                .append("<MODIFYMODE>" + modifyMode + "</MODIFYMODE>")
                .append("<USERID>" + userId + "</USERID>")
                .append("<LOGINNO>" + loginNo + "</LOGINNO>")
                .append("<RSP>" + resultCode + "</RSP>")
                .append("<ERRDESC>" + result + "</ERRDESC>")
                .append("</BODY>")
                .append("</USERMODIFYRSP>");
        log.info(returnXml.toString());
        return returnXml.toString();
    }
}

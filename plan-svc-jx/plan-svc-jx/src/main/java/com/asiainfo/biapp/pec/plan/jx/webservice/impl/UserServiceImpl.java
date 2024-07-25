package com.asiainfo.biapp.pec.plan.jx.webservice.impl;

import cn.hutool.core.date.DateUtil;
import com.asiainfo.biapp.pec.plan.dao.McdSysUserDao;
import com.asiainfo.biapp.pec.plan.dao.UserRoleRelationDao;
import com.asiainfo.biapp.pec.plan.jx.user.dao.UserEmisDao;
import com.asiainfo.biapp.pec.plan.jx.user.model.McdEmisUserCrmOmModel;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserOrg;
import com.asiainfo.biapp.pec.plan.jx.user.vo.UserRole;
import com.asiainfo.biapp.pec.plan.jx.webservice.UserService;
import com.asiainfo.biapp.pec.plan.model.McdSysUser;
import com.asiainfo.biapp.pec.plan.model.UserRoleRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: zhaoqing3
 * @Date: 2019/12/31 11:21
 * @Description:
 */
@Slf4j
@Component
@WebService(endpointInterface = "com.asiainfo.biapp.pec.plan.jx.webservice.UserService", targetNamespace = "http://webservice.jx.plan.pec.biapp.asiainfo.com/")
public class UserServiceImpl implements UserService {

    @Resource
    private UserEmisDao userEmisDao;

    @Resource
    private McdSysUserDao sysUserDao;

    @Resource
    private UserRoleRelationDao userRoleRelationDao;


    @Override
    public String operUser(String infoXml) {
        log.info(infoXml);

        StringBuffer xmlTitle = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlTitle.append("<result>");
        StringBuffer xmlInfo = new StringBuffer();
        StringBuffer xml = new StringBuffer();
        String seq = null;  //唯一标识
        String userId = null; //用户登录名
        String userName = null; //用户中文名称
        String mobile = null;  //电话号码
        String idCard = null; //身份证号
        String cityId = null; //地市编号
        String bureauId = null; //区县编号
        String bureauName = null;//区县名称
        String districtId = null; //局向编号
        String districtName = null; //局向名称
        String channelType = null;//渠道类型
        String perState = null; //工号状态
        String postCode = null; //岗位角色
        String effectTime = null;  //工号生效时间
        String expireTime = null; //工号失效时间

        Document dom = null;
        Element users = null;
        Element operType = null;
        List<Element> userList = null;

        try {
            dom = DocumentHelper.parseText(infoXml);
            users = dom.getRootElement();
            operType = users.element("operType");
            userList = users.elements("user");

            List seqtrueList = new ArrayList();
            List seqflaseList = new ArrayList();

            try {
                /**
                 增加
                 */
                if (operType.getText().equals("add")) {
                    for (Element user : userList) {
                        seq = StringUtils.isEmpty(user.element("seq").getText()) ? null : user.element("seq").getText();
                        userId = StringUtils.isEmpty(user.element("userId").getText()) ? null : user.element("userId").getText().trim();
                        userName = StringUtils.isEmpty(user.element("userName").getText()) ? null : user.element("userName").getText().trim();
                        mobile = StringUtils.isEmpty(user.element("mobile").getText()) ? null : user.element("mobile").getText();
                        idCard = StringUtils.isEmpty(user.element("idCard").getText()) ? null : user.element("idCard").getText();
                        bureauId = StringUtils.isEmpty(user.element("bureauId").getText()) ? "9999" : user.element("bureauId").getText();
                        bureauName = StringUtils.isEmpty(user.element("bureauName").getText()) ? null : user.element("bureauName").getText();
                        districtId = StringUtils.isEmpty(user.element("districtId").getText()) ? null : user.element("districtId").getText();
                        List<Map<String, Object>> cityList = userEmisDao.queryByDeptId(districtId);
                        cityId = StringUtils.isEmpty(user.element("cityId").getText()) ? cityList.get(0).get("CITYID").toString() : user.element("cityId").getText();
                        districtName = StringUtils.isEmpty(user.element("districtName").getText()) ? null : user.element("districtName").getText();
                        channelType = StringUtils.isEmpty(user.element("channelType").getText()) ? null : user.element("channelType").getText();
                        perState = StringUtils.isEmpty(user.element("perState").getText()) ? null : user.element("perState").getText();
                        postCode = StringUtils.isEmpty(user.element("postCode").getText()) ? null : user.element("postCode").getText();
                        effectTime = StringUtils.isEmpty(user.element("effectTime").getText()) ? null : user.element("effectTime").getText();
                        expireTime = StringUtils.isEmpty(user.element("expireTime").getText()) ? null : user.element("expireTime").getText();


                        McdEmisUserCrmOmModel addEimsModel = new McdEmisUserCrmOmModel();
                        addEimsModel.setId(seq);
                        addEimsModel.setUserId(userId);
                        addEimsModel.setUserName(userName);
                        addEimsModel.setMobile(mobile);
                        addEimsModel.setIdCardNo(idCard);
                        addEimsModel.setCityId(cityId);
                        addEimsModel.setCountyId(bureauId);
                        addEimsModel.setCountyName(bureauName);
                        addEimsModel.setDtrictId(districtId);
                        addEimsModel.setDtrictName(districtName);
                        addEimsModel.setChannelType(channelType);
                        addEimsModel.setStatus(perState);
                        addEimsModel.setPostCode(postCode);
                        addEimsModel.setEffecttTime(effectTime);
                        addEimsModel.setExpireTime(expireTime);


                        McdSysUser addUserModel = new McdSysUser();
                        addUserModel.setUserId(userId);
                        addUserModel.setUserName(userName);
                        addUserModel.setCityId(cityId);
                        addUserModel.setActualCityId(cityId);
                        addUserModel.setDepartmentId(districtId);
                        addUserModel.setPwd("92d994b44e003a7f09a56ab2ef0430bc");
                        addUserModel.setStatus("0".equals(perState)? 1:0);
                        addUserModel.setMobilePhone(mobile);
                        addUserModel.setDelFlag(0);
                        addUserModel.setCreateTime( new Date());


                        List postCodeList = Arrays.asList(postCode.split(","));
                        //删除USER_USER表存在的用户，防止数据重复
                        if (StringUtils.isNotBlank(userId)) {
                            LambdaQueryWrapper<McdSysUser> queryUserWrapper = new LambdaQueryWrapper<>();
                             McdSysUser sysUser = sysUserDao.selectOne(queryUserWrapper);
                            if (Objects.nonNull(sysUser)) {
                                xmlInfo.append("<returnCode>" + "0" + "</returnCode>");
                                xmlInfo.append("<returnMsg>" + "用户已经存在！" + "</returnMsg>");
                                xmlTitle.append(xmlInfo);
                                xmlTitle.append("</result>");
                                return xmlTitle.toString();
                            }
                        }

                        if (seq == null || userId == null || userName == null || districtId == null ||
                                perState == null || mobile == null) {//|| idCard == null
                            seqflaseList.add(seq);
                        } else if (userEmisDao.insert(addEimsModel) > 0 && sysUserDao.insert(addUserModel) > 0) {
                            for (int i = 0; i < postCodeList.size(); i++) {
                                UserRoleRelation addUserRole = new UserRoleRelation();
                                addUserRole.setUserId(userId);
                                addUserRole.setRoleId(Long.parseLong(postCodeList.get(i).toString()));

                              userRoleRelationDao.insert(addUserRole);
                            }
                            seqtrueList.add(seq);
                        }
                    }
                    if (seqtrueList.size() == userList.size()) {
                        xmlInfo.append("<returnCode>" + "1" + "</returnCode>");
                        xmlInfo.append("<returnMsg>" + "成功" + "</returnMsg>");
                        for (int i = 0; i < seqtrueList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqtrueList.get(i) + "</seq>");
                            xml.append("<code>" + "1" + "</code>");
                            xml.append("<msg>" + "成功" + "</msg>");
                            xml.append("</user>");
                            log.info("添加成功，seq为----" + seqtrueList.get(i));
                        }
                    } else {
                        xmlInfo.append("<returnCode>" + "0" + "</returnCode>");
                        xmlInfo.append("<returnMsg>" + "失败" + "</returnMsg>");
                        for (int i = 0; i < seqtrueList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqtrueList.get(i) + "</seq>");
                            xml.append("<code>" + "1" + "</code>");
                            xml.append("<msg>" + "成功" + "</msg>");
                            xml.append("</user>");
                            log.info("添加成功，seq为----" + seqtrueList.get(i));
                        }
                        for (int i = 0; i < seqflaseList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqflaseList.get(i) + "</seq>");
                            xml.append("<code>" + "0" + "</code>");
                            xml.append("<msg>" + "失败" + "</msg>");
                            xml.append("</user>");
                            log.info("添加失败，seq为----" + seqflaseList.get(i));
                        }
                    }
                    xmlTitle.append(xmlInfo);
                    xmlTitle.append(xml);
                    xmlTitle.append("</result>");
                    log.info(String.valueOf(xmlTitle));
                }
            } catch (Exception e) {
                xmlTitle.append("<returnCode>" + "0" + "</returnCode>");
                xmlTitle.append("<returnMsg>" + "失败。具体原因如下" + e.getMessage() + "</returnMsg></result>");
                log.error("--add--", e);
                log.info(String.valueOf(xmlTitle));
                return xmlTitle.toString();
            }

            try {
                /**
                 修改
                 */
                if (operType.getText().equals("update")) {
                    for (Element user : userList) {
                        seq = user.element("seq") == null ? null : user.element("seq").getText();
                        userId = user.element("userId") == null ? null : user.element("userId").getText().trim();
                        LambdaQueryWrapper<McdEmisUserCrmOmModel>  userCrmOmQueryWrapper = new LambdaQueryWrapper<>();
                        userCrmOmQueryWrapper.eq(McdEmisUserCrmOmModel::getUserId,userId).last(" limit 1 ");
                        McdEmisUserCrmOmModel updateEimsModel  = userEmisDao.selectOne(userCrmOmQueryWrapper);
                        //优先查询USER_CRM_OM表，如果没有则查询user_user表
                        if (Objects.isNull(updateEimsModel)) {
                            //不存在则查询用户是否在user_user表中，存在则跳过，不存在则将用户数据保存至USER_CRM_OM
                            LambdaQueryWrapper<McdSysUser>  queryUserM = new LambdaQueryWrapper<>();
                            queryUserM.eq(McdSysUser::getUserId,userId).last(" limit 1 ");
                            McdSysUser mcdSysUser = sysUserDao.selectOne(queryUserM);
                            if (Objects.nonNull(mcdSysUser)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String startTime = sdf.format(new Date());
                                Date afterDate = DateUtil.offsetDay(new Date(), 3650);
                                String endTime = sdf.format(afterDate);

                                McdEmisUserCrmOmModel addEimsModel = new McdEmisUserCrmOmModel();
                                addEimsModel.setId(seq);
                                addEimsModel.setUserId(userId);
                                addEimsModel.setUserName(mcdSysUser.getUserName());
                                addEimsModel.setMobile(mcdSysUser.getMobilePhone());
                                addEimsModel.setCityId(mcdSysUser.getCityId());
                                addEimsModel.setStatus("1");
                                addEimsModel.setEffecttTime(startTime);
                                addEimsModel.setExpireTime(endTime);

                                //保存数据至USER_CRM_OM表中
                                userEmisDao.insert(addEimsModel);
                            } else {
                                xmlInfo.append("<returnCode>" + "0" + "</returnCode>");
                                xmlInfo.append("<returnMsg>" + "失败,用户不存在请先新增用户！" + "</returnMsg>");
                                xmlTitle.append(xmlInfo);
                                xmlTitle.append("</result>");
                                return xmlTitle.toString();
                            }
                        }

                        userName = user.element("userName").getText().equals("") ? updateEimsModel.getUserName() : user.element("userName").getText().trim();
                        mobile = user.element("mobile").getText().equals("") ? updateEimsModel.getMobile() : user.element("mobile").getText();
                        districtId = user.element("districtId").getText().equals("") ? updateEimsModel.getDtrictId() : user.element("districtId").getText();
                        districtName = user.element("districtName").getText().equals("") ? updateEimsModel.getDtrictName() : user.element("districtName").getText();
                        perState = user.element("perState").getText().equals("") ? updateEimsModel.getStatus(): user.element("perState").getText();
                        postCode = user.element("postCode").getText().equals("") ? updateEimsModel.getPostCode(): user.element("postCode").getText();


                        updateEimsModel = new McdEmisUserCrmOmModel();
                        updateEimsModel.setId(seq);
                        updateEimsModel.setUserName(userName);
                        updateEimsModel.setMobile(mobile);
                        updateEimsModel.setDtrictId(districtId);
                        updateEimsModel.setDtrictName(districtName);
                        updateEimsModel.setStatus(perState);
                        updateEimsModel.setPostCode(postCode);
                        LambdaUpdateWrapper<McdEmisUserCrmOmModel> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.eq(McdEmisUserCrmOmModel::getUserId,userId);

                        McdSysUser updateUser = new McdSysUser();
                        updateUser.setUserName(userName);
                        updateUser.setDepartmentId(districtId);
                        updateUser.setStatus("0".equals(perState)? 1:0);
                        updateUser.setMobilePhone(mobile);
                        updateUser.setUpdateDate( new Date());

                        LambdaUpdateWrapper<McdSysUser> updateUserWrapper = new LambdaUpdateWrapper<>();
                        updateUserWrapper.eq(McdSysUser::getUserId,userId);

                        List postCodeList = Arrays.asList(postCode.split(","));

                        if (seq == null || userId == null) {
                            seqflaseList.add(seq);
                        } else if (userEmisDao.update(updateEimsModel,updateWrapper) > 0 && sysUserDao.update(updateUser,updateUserWrapper) > 0) {

                            LambdaQueryWrapper<UserRoleRelation> roleWrapper = new LambdaQueryWrapper<>();
                            roleWrapper.eq(UserRoleRelation::getUserId,userId);
                            List<UserRoleRelation> userRoleRelationList = userRoleRelationDao.selectList(roleWrapper);

                            for (int i = 0; i < postCodeList.size(); i++) {
                                String roleCode = postCodeList.get(i).toString();
                                for (UserRoleRelation userRoleRelation : userRoleRelationList) {
                                    if (!roleCode.equals(userRoleRelation.getRoleId().toString())) {
                                        UserRoleRelation addUserRole = new UserRoleRelation();
                                        addUserRole.setUserId(userId);
                                        addUserRole.setRoleId(Long.parseLong(roleCode));

                                        userRoleRelationDao.insert(addUserRole);
                                    }
                                }

                            }
                            seqtrueList.add(seq);
                        }
                    }
                    if (seqtrueList.size() == userList.size()) {
                        xmlInfo.append("<returnCode>" + "1" + "</returnCode>");
                        xmlInfo.append("<returnMsg>" + "成功" + "</returnMsg>");
                        for (int i = 0; i < seqtrueList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqtrueList.get(i) + "</seq>");
                            xml.append("<code>" + "1" + "</code>");
                            xml.append("<msg>" + "成功" + "</msg>");
                            xml.append("</user>");
                            log.info("修改成功，seq为----" + seqtrueList.get(i));
                        }
                    } else {
                        xmlInfo.append("<returnCode>" + "0" + "</returnCode>");
                        xmlInfo.append("<returnMsg>" + "失败" + "</returnMsg>");
                        for (int i = 0; i < seqtrueList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqtrueList.get(i) + "</seq>");
                            xml.append("<code>" + "1" + "</code>");
                            xml.append("<msg>" + "成功" + "</msg>");
                            xml.append("</user>");
                            log.info("修改成功，seq为----" + seqtrueList.get(i));
                        }
                        for (int i = 0; i < seqflaseList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqflaseList.get(i) + "</seq>");
                            xml.append("<code>" + "0" + "</code>");
                            xml.append("<msg>" + "失败，请检查参数是否正确" + "</msg>");
                            xml.append("</user>");
                            log.info("修改失败，seq为----" + seqflaseList.get(i));
                        }
                    }
                    xmlTitle.append(xmlInfo);
                    xmlTitle.append(xml);
                    xmlTitle.append("</result>");
                    log.info(String.valueOf(xmlTitle));
                }
            } catch (Exception e) {
                xmlTitle.append("<returnCode>" + "0" + "</returnCode>");
                xmlTitle.append("<returnMsg>" + "失败。具体原因如下" + e.getMessage() + "</returnMsg></result>");
                log.error("--update--", e);
                log.info(String.valueOf(xmlTitle));
                return xmlTitle.toString();
            }

            try {
                /**
                 删除
                 */
                if (operType.getText().equals("delete")) {
                    for (Element user : userList) {
                        seq = user.element("seq") == null ? null : user.element("seq").getText();
                        userId = user.element("userId") == null ? null : user.element("userId").getText().trim();

                        LambdaUpdateWrapper<McdEmisUserCrmOmModel> deleEmisUser = new LambdaUpdateWrapper<>();
                        deleEmisUser.eq(McdEmisUserCrmOmModel::getUserId,userId);

                        LambdaUpdateWrapper<McdSysUser> deleUser = new LambdaUpdateWrapper<>();
                        deleUser.eq(McdSysUser::getUserId,userId);

                        LambdaUpdateWrapper<UserRoleRelation> deleUserRole = new LambdaUpdateWrapper<>();
                        deleUserRole.eq(UserRoleRelation::getUserId,userId);

                        if (userEmisDao.delete(deleEmisUser) > 0 && sysUserDao.delete(deleUser) > 0) {
                            userRoleRelationDao.delete(deleUserRole);
                            seqtrueList.add(seq);
                        } else {
                            seqflaseList.add(seq);
                        }
                    }

                    if (seqtrueList.size() == userList.size()) {
                        xmlInfo.append("<returnCode>" + "1" + "</returnCode>");
                        xmlInfo.append("<returnMsg>" + "成功" + "</returnMsg>");
                        for (int i = 0; i < seqtrueList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqtrueList.get(i) + "</seq>");
                            xml.append("<code>" + "1" + "</code>");
                            xml.append("<msg>" + "成功" + "</msg>");
                            xml.append("</user>");
                            log.info("删除成功，seq为----" + seqtrueList.get(i));
                        }
                    } else {
                        xmlInfo.append("<returnCode>" + "0" + "</returnCode>");
                        xmlInfo.append("<returnMsg>" + "失败" + "</returnMsg>");
                        for (int i = 0; i < seqtrueList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqtrueList.get(i) + "</seq>");
                            xml.append("<code>" + "1" + "</code>");
                            xml.append("<msg>" + "成功" + "</msg>");
                            xml.append("</user>");
                            log.info("删除成功，seq为----" + seqtrueList.get(i));
                        }
                        for (int i = 0; i < seqflaseList.size(); i++) {
                            xml.append("<user>");
                            xml.append("<seq>" + seqflaseList.get(i) + "</seq>");
                            xml.append("<code>" + "0" + "</code>");
                            xml.append("<msg>" + "失败" + "</msg>");
                            xml.append("</user>");
                            log.info("删除失败，seq为----" + seqflaseList.get(i));
                        }
                    }
                    xmlTitle.append(xmlInfo);
                    xmlTitle.append(xml);
                    xmlTitle.append("</result>");
                }
                log.info(String.valueOf(xmlTitle));
                return xmlTitle.toString();
            } catch (Exception e) {
                xmlTitle.append("<returnCode>" + "0" + "</returnCode>");
                xmlTitle.append("<returnMsg>" + "失败。具体原因如下" + e.getMessage() + "</returnMsg></result>");
                log.error("--del--", e);
                log.info(String.valueOf(xmlTitle));
                return xmlTitle.toString();
            }
        } catch (DocumentException e) {
            xmlTitle.append("<returnCode>" + "0" + "</returnCode>");
            xmlTitle.append("<returnMsg>" + "失败。具体原因如下" + e.getMessage() + "</returnMsg></result>");
            log.info(String.valueOf(xmlTitle));
            return xmlTitle.toString();
        }
    }

    @Override
    public String queryRole(String infoXml) {
        log.info(infoXml);
        Document dom = null;
        Element roles = null;
        Element operType = null;
        String roleId;
        String roleName;
        String parentRoleId = null;
        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<result>");
        try {
            dom = DocumentHelper.parseText(infoXml);
            roles = dom.getRootElement();
            operType = roles.element("operType");

            /**
             查询
             */
            if (operType.getText().equals("query")) {
                List<UserRole> dataList = userEmisDao.queryRoleInfo();
                for (int i = 0; i < dataList.size(); i++) {
                    roleId = dataList.get(i).getId();
                    roleName = dataList.get(i).getName();
                    parentRoleId = dataList.get(i).getDesc() == null ? "" : dataList.get(i).getDesc();

                    xml.append("<roles>");
                    xml.append("<roleId>" + roleId + "</roleId>");
                    xml.append("<roleName>" + roleName + "</roleName>");
                    xml.append("<parentRoleId>" + parentRoleId + "</parentRoleId>");
                    xml.append("</roles>");
                }
            }
        } catch (DocumentException e) {
            log.error("error:", e);
        }
        xml.append("</result>");
        return xml.toString();
    }

    @Override
    public String queryOrg(String infoXml) {
        log.info(infoXml);
        Document dom = null;
        Element orgs = null;
        Element operType = null;
        String orgId;
        String orgName;
        String parentOrgId;
        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<result>");
        try {
            dom = DocumentHelper.parseText(infoXml);
            orgs = dom.getRootElement();
            operType = orgs.element("operType");

            /**
             查询
             */
            if (operType.getText().equals("query")) {
                List<UserOrg> dataList = userEmisDao.queryOrgInfo();
                for (int i = 0; i < dataList.size(); i++) {
                    orgId = dataList.get(i).getOrgId();
                    orgName = dataList.get(i).getOrgName();
                    parentOrgId = dataList.get(i).getParentOrgId();

                    xml.append("<orgs>");
                    xml.append("<orgId>" + orgId + "</orgId>");
                    xml.append("<orgName>" + orgName + "</orgName>");
                    xml.append("<parentOrgId>" + parentOrgId + "</parentOrgId>");
                    xml.append("</orgs>");
                }
            }
        } catch (DocumentException e) {
            log.error("error:", e);
        }
        xml.append("</result>");
        return xml.toString();
    }
}


package com.asiainfo.biapp.pec.plan.jx.assembler;

import cn.hutool.core.util.PhoneUtil;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.specialuser.model.JxMcdSpecialUse;
import com.asiainfo.biapp.pec.plan.jx.specialuser.service.impl.JxChannelDimCacheTask;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseSave;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : zhouyang
 * @date : 2021-12-04 17:18:28
 */
@Component
public class JxSpecialUserAssembler {

    @Resource
    private JxChannelDimCacheTask channelTask;
    /**
     * 组装实体列表
     *
     * @param save
     * @param phoneList
     * @param user
     * @return
     */
    public List<JxMcdSpecialUse> convertToModels(SpecialUseSave save, List<String> phoneList, UserSimpleInfo user) {
        final Integer userType = Integer.parseInt(save.getUserType());
        final String channelIds = save.getChannelId();
        final String[] channelArr = channelIds.split(Constant.SpecialCharacter.COMMA);
        List<JxMcdSpecialUse> list = new ArrayList<>();
        for (String phoneNum : phoneList) {
            for (String channelId : channelArr) {
                JxMcdSpecialUse mcdSpecialUse = convertToModel(userType, channelId, phoneNum,user);
                mcdSpecialUse.setEndTime(save.getEndTime());
                mcdSpecialUse.setStartTime(save.getStartTime());
                list.add(mcdSpecialUse);
            }
        }
        return list;
    }

    /**
     * 组装实体
     *
     * @param userType
     * @param channelId
     * @param phoneNo
     * @param user
     * @return
     */
    private JxMcdSpecialUse convertToModel(Integer userType, String channelId, String phoneNo,UserSimpleInfo user ) {
        JxMcdSpecialUse list = new JxMcdSpecialUse();
        String channelName = channelTask.getChannelNameByChannelId(channelId);
        list.setProductNo(phoneNo);
        list.setChannelId(channelId);
        list.setChannelName(channelName);
        list.setCustType(userType);
        list.setCreateUserId(user.getUserId());
        list.setEnterTime(new Date());
        list.setUserName(user.getUserName());
        list.setDataSource(Constant.SpecialNumber.ONE_NUMBER);
        return list;
    }

    /**
     * 解析手机号
     *
     * @param phoneNo
     * @return
     */
    public static Map<String, List<String>> convertPhone(String phoneNo) {
        Map<String, List<String>> result = Maps.newHashMap();
        List<String> phoneList = new ArrayList<>();
        List<String> notPhone = new ArrayList<>();
        result.put(Constant.SpecialNumber.ONE_STRING, phoneList);
        result.put(Constant.SpecialNumber.ZERO_STRING, notPhone);
        if (!phoneNo.contains(Constant.SpecialCharacter.COMMA) && !phoneNo.contains(Constant.SpecialCharacter.CHINESE_COMMA)) {
            if (!PhoneUtil.isMobile(phoneNo)) {
                notPhone.add(phoneNo);
                return result;
            }
            phoneList.add(phoneNo);
            return result;
        }
        final String[] phoneArr = phoneNo.replaceAll(Constant.SpecialCharacter.CHINESE_COMMA, Constant.SpecialCharacter.COMMA)
                .split(Constant.SpecialCharacter.COMMA);
        for (String phone : phoneArr) {
            if (!PhoneUtil.isMobile(phone) || phoneList.contains(phone)) {
                notPhone.add(phone);
                continue;
            }
            phoneList.add(phone);
        }
        return result;
    }

    public static Map<String, List<String>> convertPhone(MultipartFile file) {
        String productNos = Strings.EMPTY;
        try {
            productNos = (new String(file.getBytes())).replaceAll("\r\n", Constant.SpecialCharacter.COMMA)
                    .replaceAll("[\r\n]", Constant.SpecialCharacter.COMMA);
        }catch (Exception e){
            Assert.error("请使用模版导入");
        }
        Assert.isTrue(StringUtils.isNotBlank(productNos), "导入内容为空，请重试");
        return convertPhone(productNos);
    }

    public static List<SpecialUseSave> convertBatchDelModel(SpecialUseSave del) {
        List<SpecialUseSave> result = new ArrayList<>();
        final String[] channelIds = del.getChannelId().split(Constant.SpecialCharacter.COMMA);
        final String[] userTypes = del.getUserType().split(Constant.SpecialCharacter.COMMA);
        final String[] phones = del.getPhoneNos().split(Constant.SpecialCharacter.COMMA);
        for (int i = Constant.SpecialNumber.ZERO_NUMBER; i < channelIds.length; i++) {
            SpecialUseSave delItem = new SpecialUseSave();
            delItem.setUserType(userTypes[i]);
            delItem.setPhoneNos(phones[i]);
            delItem.setChannelId(channelIds[i]);
            result.add(delItem);
        }
        return result;
    }

}

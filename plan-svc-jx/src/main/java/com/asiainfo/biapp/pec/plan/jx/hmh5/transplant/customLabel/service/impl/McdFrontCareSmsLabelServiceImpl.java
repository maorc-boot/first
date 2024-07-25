package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.AddCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.ModifyCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.SelectCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.mapper.McdFrontCareSmsLabelMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model.McdFrontCareSmsLabel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model.McdFrontCareSmsLabelRela;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.McdFrontCareSmsLabelRelaService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.McdFrontCareSmsLabelService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service.impl.resultInfo.SelectCareSmsLabelResultInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Service
public class McdFrontCareSmsLabelServiceImpl extends ServiceImpl<McdFrontCareSmsLabelMapper, McdFrontCareSmsLabel> implements McdFrontCareSmsLabelService {

    @Autowired
    private McdFrontCareSmsLabelRelaService smsLabelRelaService;

    @Autowired
    private McdFrontCareSmsLabelMapper mcdFrontCareSmsLabelMapper;

    @Override
    public void addNewCareSmsLabel(UserSimpleInfo user, AddCareSmsLabelParam smsLabelParam) {
        String labelCondition = smsLabelParam.getLabelCondition();
        //JSONObject.parse(labelCondition)在没有大括号包裹的时候也能转成功，所以先判断有没有大括号
        if (!labelCondition.contains("{") && !labelCondition.contains("}"))
            throw new BaseException("筛选条件不是有效的JSON字符串！");
        //检查创建的筛选条件是否是有效的json字符串
        try {
            JSONObject.parse(labelCondition);
        } catch (Exception e) {
            throw new BaseException("筛选条件不是有效的JSON字符串！");
        }

        McdFrontCareSmsLabel smsLabel = baseMapper.selectOne(new QueryWrapper<McdFrontCareSmsLabel>()
                .eq("LABEL_NAME", smsLabelParam.getLabelName())
        );
        if (smsLabel != null) throw new BaseException("标签名：{" + smsLabelParam.getLabelName() + "}已存在！");


        smsLabel = BeanUtil.copyProperties(smsLabelParam, McdFrontCareSmsLabel.class);
        // 查询表中最大的SERIAL_NUM
        int maxSerialNum = mcdFrontCareSmsLabelMapper.selectMaxSerialNum();
        smsLabel.setSerialNum(maxSerialNum + 1);
        smsLabel.setDataState(1);   //之前版本设置的1，但不知道什么含义
        smsLabel.setCreateBy(user.getUserId());
        // smsLabel.setCreateUserId(user.getUserId());
        smsLabel.setCreateTime(new Date());
        baseMapper.insert(smsLabel);
    }

    @Override
    public void modifyCareSmsLabel(UserSimpleInfo user, ModifyCareSmsLabelParam smsLabelParam) {
        String labelCondition = smsLabelParam.getLabelCondition();
        //JSONObject.parse(labelCondition)在没有大括号包裹的时候也能转成功，所以先判断有没有大括号
        if (!labelCondition.contains("{") && !labelCondition.contains("}"))
            throw new BaseException("筛选条件不是有效的JSON字符串！");
        //先检查创建的筛选条件是否是有效的json字符串，避免不必要的数据库查询
        try {
            JSONObject.parse(labelCondition);
        } catch (Exception e) {
            throw new BaseException("筛选条件不是有效的JSON字符串！");
        }
        //根据serialNum查询客群标签
        McdFrontCareSmsLabel smsLabel = baseMapper.selectOne(Wrappers.<McdFrontCareSmsLabel>query().lambda().eq(McdFrontCareSmsLabel::getSerialNum, smsLabelParam.getSerialNum()));
        if (smsLabel == null) {
            throw new BaseException("根据serialNum：{" + smsLabelParam.getSerialNum() + "}没有查询到相应的客群标签！");
        } else {
            if (!smsLabel.getCreateBy().equals(user.getUserId()))
                throw new BaseException("标签名：{" + smsLabelParam.getLabelName() + "}不是你创建的，你无权修改！");

            McdFrontCareSmsLabel smsLabelOther = baseMapper.selectOne(new QueryWrapper<McdFrontCareSmsLabel>()
                    .eq("LABEL_NAME", smsLabelParam.getLabelName())
            );
            if (smsLabelOther != null && !smsLabelOther.getSerialNum().equals(smsLabelParam.getSerialNum()))
                throw new BaseException("标签名：{" + smsLabelParam.getLabelName() + "}已存在！");

            BeanUtil.copyProperties(smsLabelParam, smsLabel);
            smsLabel.setUpdateBy(user.getUserId());
            // smsLabel.setUpdateUserId(user.getUserId());
            smsLabel.setUpdateTime(new Date());
            baseMapper.update(smsLabel, Wrappers.<McdFrontCareSmsLabel>update().lambda().eq(McdFrontCareSmsLabel::getSerialNum, smsLabelParam.getSerialNum()));
        }
    }

    @Override
    public void deleteCareSmsLabel(String userId, Integer serialNum) {
        McdFrontCareSmsLabel smsLabel = baseMapper.selectOne(Wrappers.<McdFrontCareSmsLabel>query().lambda().eq(McdFrontCareSmsLabel::getSerialNum, serialNum));
        if (smsLabel == null)
            throw new BaseException("序列id：{" + serialNum + "}不存在！");

        if (!smsLabel.getCreateBy().equals(userId))
            throw new BaseException("序列id：{" + serialNum + "}不是你创建的，你无权删除！");

        // 一个标签可以对应多个短信模板
        List<McdFrontCareSmsLabelRela> careSmsLabelRela = smsLabelRelaService.list(new QueryWrapper<McdFrontCareSmsLabelRela>()
                .eq("LABEL_CODE", smsLabel.getLabelCode())
        );
        if (CollectionUtil.isNotEmpty(careSmsLabelRela))
            throw new BaseException("该客户标签id:{" + serialNum + "}已经与短信模板相关联，请先删除关联关系！");

        baseMapper.delete(Wrappers.<McdFrontCareSmsLabel>query().lambda().eq(McdFrontCareSmsLabel::getSerialNum, serialNum));
    }

    @Override
    public Page selectCareSmsLabels(SelectCareSmsLabelParam smsLabelParam) {
        Page<SelectCareSmsLabelResultInfo> careSmsLabels = baseMapper.selectCareSmsLabels(
                new Page(smsLabelParam.getPageNum(), smsLabelParam.getPageSize()),
                smsLabelParam
        );
        return careSmsLabels;
    }
}

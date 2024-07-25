package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.AddCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.ModifyCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.controller.reqParam.SelectCareSmsLabelParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.customLabel.model.McdFrontCareSmsLabel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdFrontCareSmsLabelService extends IService<McdFrontCareSmsLabel> {

    void addNewCareSmsLabel(UserSimpleInfo user, AddCareSmsLabelParam smsLabelParam);

    void modifyCareSmsLabel(UserSimpleInfo user, ModifyCareSmsLabelParam smsLabelParam);

    void deleteCareSmsLabel(String userId, Integer serialNum);

    Page selectCareSmsLabels(SelectCareSmsLabelParam smsLabelParam);
}

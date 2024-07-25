package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.controller.reqParam.*;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.mapper.McdCustomLabelDefMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.model.McdCustomLabelDef;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.McdCustomLabelDefService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultConfInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelDef.service.impl.resultInfo.SelfDefinedLabelResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelRel.model.McdCustomLabelRel;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdCustomLabelRel.service.impl.McdCustomLabelRelServiceImpl;
import com.asiainfo.biapp.pec.plan.service.IMcdSysCityService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签定义表 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-06-26
 */
@Service
public class McdCustomLabelDefServiceImpl extends ServiceImpl<McdCustomLabelDefMapper, McdCustomLabelDef> implements McdCustomLabelDefService {

    @Autowired
    private IMcdSysCityService cityService;

    @Autowired
    private McdCustomLabelRelServiceImpl labelRelService;

    @Autowired
    @Qualifier("com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient")
    private PecApproveFeignClient approveFeignClient;

    @Autowired
    private McdCustomLabelDefMapper mcdCustomLabelDefMapper;

    @Override
    public Page<SelfDefinedLabelResultInfo> selectSelfDefinedLabel(String cityId, SelectSelfDefinedLabelConfParam selfDefinedLabelConfParam) {
        return baseMapper.selectSelfDefinedLabel(
                new Page<>(selfDefinedLabelConfParam.getPageNum(), selfDefinedLabelConfParam.getPageSize()),
                cityId, selfDefinedLabelConfParam.getCustomLabelName()
        );
    }

    @Override
    public void modifySelfDefinedLabel(String userId, ModifySelfDefinedLabelParam selfDefinedLabelParam) {

        McdCustomLabelDef mcdCustomLabelDef = baseMapper.selectById(selfDefinedLabelParam.getCustomLabelId());
        if (mcdCustomLabelDef == null) throw new BaseException("传入的customLabelId值有误！");

        if (!userId.equals(mcdCustomLabelDef.getCreateUserId()))
            throw new BaseException("customLabelId值：" + selfDefinedLabelParam.getCustomLabelId() + "对应的标签不是由你创建的，无法修改！");

        mcdCustomLabelDef.setCustomLabelDesc(selfDefinedLabelParam.getCustomLabelDes());
        mcdCustomLabelDef.setUpdateTime(new Date());

        baseMapper.updateById(mcdCustomLabelDef);
    }

    @Override
    public Page<SelfDefinedLabelResultConfInfo> selectSelfDefinedLabelConfDetail(String cityId, SelectSelfDefinedLabelParam selfDefinedLabelParam) {

        // String selectCityId = selfDefinedLabelParam.getCityId();

        // McdSysCity city = cityService.getById(cityId);

        //因数据库此字段的默认值为“0”，可能出现省公司父id为“0”的情况，目前为"-1"
        //当查询者的身份不是省公司的时候
        // if (!String.valueOf(city.getParentId()).equals("-1") && !String.valueOf(city.getParentId()).equals("0")) {
        //     if (StringUtils.isNotBlank(selectCityId)) { //如果传递了查询cityId参数，但cityId参数与用户cityId不一致，就抛出异常
        //         if (!cityId.equals(selectCityId)) throw new BaseException("当前身份不可查询其他城市自定义标签！");
        //     } else { //如果没有传递查询cityId参数，就自动赋值为用户所在的cityId
        //         selfDefinedLabelParam.setCityId(cityId);
        //     }
        // }
        return baseMapper.selectSelfDefinedLabelConfDetail(
                new Page<>(selfDefinedLabelParam.getPageNum(), selfDefinedLabelParam.getPageSize()),
                selfDefinedLabelParam, cityId
        );
    }

    @Override
    public Map<String, List<SelfDefinedLabelResultInfo>> selectSelfDefinedLabelConf(String cityId) {
        List<SelfDefinedLabelResultInfo> selfDefinedLabelResultInfos = baseMapper.selectSelfDefinedLabelConf(cityId);
        return selfDefinedLabelResultInfos.stream().collect(Collectors.groupingBy(SelfDefinedLabelResultInfo::getModuleName));
        // HashMap<String, List<SelfDefinedLabelResultInfo>> labelMap = new HashMap<>();
        //
        // for (int i = 0; i < selfDefinedLabels.size(); i++) {
        //     SelfDefinedLabelResultInfo selfDefinedLabel = selfDefinedLabels.get(i);
        //     String moduleName = selfDefinedLabel.getModuleName();
        //     if (labelMap.get(moduleName) == null) {
        //         labelMap.put(moduleName, new ArrayList<>(Arrays.asList(selfDefinedLabel)));
        //     } else {
        //         labelMap.get(moduleName).add(selfDefinedLabel);
        //     }
        //     //将模块名称置为null，避免引起误会
        //     selfDefinedLabel.setModuleName(null);
        // }
        //
        // return labelMap;
    }

    @Override
    public void modifySelfDefinedLabelConf(String cityId, ModifySelfDefinedLabelConfParam selfDefinedLabelConfParam) {

        //1.客户分布，2.关怀短信，3.重点营销，检查是否传入了labelId值
        HashMap<String, List<String>> labelConf = new HashMap<String, List<String>>() {{
            put("1", selfDefinedLabelConfParam.getCustomerDistributes());
            put("2", selfDefinedLabelConfParam.getCareMessages());
            put("3", selfDefinedLabelConfParam.getMajorMarketings());
            put("4", selfDefinedLabelConfParam.getDaiweiMessages());
        }};
        Set<String> allLabelIds = new HashSet<>();
        // 2. 获取所有的标签id且去重后
        labelConf.values().forEach(allLabelIds::addAll);
        if (allLabelIds.size() == 0) throw new BaseException("没有添加相关参数，标签配置修改失败！");

        // 3. 检查是否存在传入的labelId值
        Map<String, String> labelMap = baseMapper.selectSelfDefinedLabelByIds(cityId, allLabelIds)
                .stream().collect(Collectors.toMap(SelfDefinedLabelResultInfo::getCustomLabelId, SelfDefinedLabelResultInfo::getCustomLabelName));
        if (labelMap.size() == 0) throw new BaseException("无效的labelId值！");

        // 4. 检查是否包含了所有的labelId值(传入的和表中查询的标签id集合进行校验)，因为上述查询时用的in list查询
        if (!labelMap.keySet().containsAll(allLabelIds)) throw new BaseException("参数列表中包含了无效的labelId值！");

        // 5. 生成所有关联对象
        ArrayList<McdCustomLabelRel> mcdCustomLabelRels = new ArrayList<>();
        Iterator<Map.Entry<String, List<String>>> labelConfIterator = labelConf.entrySet().iterator();
        while (labelConfIterator.hasNext()) {
            Map.Entry<String, List<String>> eachConf = labelConfIterator.next();
            List<String> labelIds = eachConf.getValue();
            for (int i = 0; i < labelIds.size(); i++) {
                mcdCustomLabelRels.add(new McdCustomLabelRel(cityId, eachConf.getKey(), labelIds.get(i), i + 1));
            }
        }

        // 6. 移除之前的配置关系
        labelRelService.remove(new LambdaQueryWrapper<McdCustomLabelRel>().eq(McdCustomLabelRel::getCityId, cityId));

        // 7. 添加新的配置关系
        labelRelService.saveBatch(mcdCustomLabelRels);
    }

    @Override
    public void modifyLabelStatus(ModifySelfLabelStatusParam labelStatusParam) {

        //根据labelId检查是否存在相应的预警信息
        McdCustomLabelDef mcdCustomLabelDef = baseMapper.selectById(labelStatusParam.getLabelId());
        if (mcdCustomLabelDef == null) throw new BaseException("无效的labelId值！");

        //修改状态的接口只给远程调用，所以只有在审批中：51 的预警才能修改
        if (mcdCustomLabelDef.getApproveStatus() != 51)
            throw new BaseException("当前标签不是审批中的状态！");

        //更改标签审批状态和设置流程实例ID
        mcdCustomLabelDef.setApproveStatus(labelStatusParam.getApproveStatus());
        mcdCustomLabelDef.setApproveFlowId(labelStatusParam.getApproveFlowId());
        baseMapper.updateById(mcdCustomLabelDef);


    }

    @Override
    public ActionResponse<SubmitProcessJxDTO> getNodeApprover() {
        //获取自定义预警的审批模板
        ActionResponse<CmpApprovalProcess> customAlarm = approveFeignClient.getApproveConfig(PecApproveFeignClient.CUSTOM_LABEL);
        //feign调用失败的情况
        if (ResponseStatus.SUCCESS.getCode() != customAlarm.getStatus().getCode())
            throw new BaseException(customAlarm.getMessage());

        //首次获取审批流程节点的必要参数
        JSONObject triggerParm = new JSONObject();
        triggerParm.put("channelId", "");  //不设置此值无法查询到结果

        SubmitProcessJxDTO submitProcessJxDTO = new SubmitProcessJxDTO();
        submitProcessJxDTO.setProcessId(customAlarm.getData().getProcessId());
        submitProcessJxDTO.setBerv(customAlarm.getData().getBerv());
        submitProcessJxDTO.setTriggerParm(triggerParm);

        ActionResponse<SubmitProcessJxDTO> nodeApprover = approveFeignClient.getNodeApprover(submitProcessJxDTO);
        //feign调用失败的情况
        if (ResponseStatus.SUCCESS.getCode() != nodeApprover.getStatus().getCode())
            throw new BaseException(customAlarm.getMessage());

        return nodeApprover;
    }

    @Override
    public void commitProcess(LabelCommitProcessParam processParam, UserSimpleInfo user) {
        //检查当前登录用户是否是标签的创建者
        McdCustomLabelDef mcdCustomLabelDef = baseMapper.selectById(processParam.getCustomLabelId());
        if (!user.getUserId().equals(mcdCustomLabelDef.getCreateUserId()))
            throw new BaseException("此标签不是你创建的，无法提交审批！");

        //将数据复制到SubmitProcessQuery中，因feign调用使用的此实体类
        SubmitProcessQuery submitProcessQuery = BeanUtil.copyProperties(processParam, SubmitProcessQuery.class);
        submitProcessQuery.setBusinessId(processParam.getCustomLabelId());
        submitProcessQuery.setApprovalType(PecApproveFeignClient.CUSTOM_LABEL);

        //此方法是提交审批时的接口，远程调用方法为：commitProcess（易产生歧义）
        ActionResponse<Object> commitProcess = approveFeignClient.submit(submitProcessQuery);
        if (ResponseStatus.SUCCESS.getCode() != commitProcess.getStatus().getCode())
            throw new BaseException(commitProcess.getMessage());

        //修改标签审批状态
        mcdCustomLabelDef.setApproveStatus(51);    //将标签的状态改为审批中
        // mcdCustomLabelDef.setApproveFlowId(commitProcess.getData().toString()); //添加流程编号
        baseMapper.updateById(mcdCustomLabelDef);
    }

    /**
     * 根据labelId值删除自定义预警
     *
     * @param user 登录用户信息
     * @param labelId 标签id
     */
    @Transactional
    @Override
    public void deleteLabel(UserSimpleInfo user, String labelId) {
        //根据labelId检查是否存在相应的标签
        McdCustomLabelDef mcdCustomLabelDef = baseMapper.selectById(labelId);
        if (mcdCustomLabelDef == null) throw new BaseException("无效的labelId值！");

        //当标签状态不是审批中时：51，才能删除标签
        if (mcdCustomLabelDef.getApproveStatus() == 51)
            throw new BaseException("该标签正在审核中，无法删除！");

        if (!user.getUserId().equals(mcdCustomLabelDef.getCreateUserId()))
            throw new BaseException("此标签信息不是你创建的，删除失败！");

        //将标签信息逻辑删除
        // mcdCustomLabelDef.setDataStatus(2);
        // baseMapper.updateById(mcdCustomLabelDef);
        //
        // //获取与用户地市关联的标签
        // List<McdCustomLabelRel> mcdCustomLabelRels = labelRelService.list(
        //         new LambdaQueryWrapper<McdCustomLabelRel>().eq(McdCustomLabelRel::getCityId, user.getCityId())
        // );
        // //将要移除的标签提取出来
        // List<McdCustomLabelRel> modules = mcdCustomLabelRels.stream()
        //         .filter(rel -> rel.getCustomLabelId().equals(mcdCustomLabelDef.getCustomLabelId()))
        //         .collect(Collectors.toList());
        // //将被移除标签之后的标签序号(sortNum)-1
        // modules.forEach(module -> {
        //     mcdCustomLabelRels.stream()
        //             .filter(rel -> rel.getModuleId().equals(module.getModuleId()) && rel.getSortNum() > module.getSortNum())
        //             .forEach(rel -> rel.setSortNum(rel.getSortNum() - 1));
        // });
        // //从集合中移除指定标签
        // mcdCustomLabelRels.removeAll(modules);
        // //移除之前的配置关系
        // labelRelService.remove(new LambdaQueryWrapper<McdCustomLabelRel>().eq(McdCustomLabelRel::getCityId, user.getCityId()));
        // //添加新的配置关系
        // labelRelService.saveBatch(mcdCustomLabelRels);

        // 1. 清除模块配置关联表  2. 删除标签定义表数据 3. 删除同步记录表
        mcdCustomLabelDefMapper.deleteByLabelId(labelId);
        // 4. 清除自定义标签列
        mcdCustomLabelDefMapper.deleteLabelCol(labelId, user.getCityId());
    }

    @Override
    public String selectLabelApproveFlowId(Integer labelId) {
        McdCustomLabelDef mcdCustomLabelDef = baseMapper.selectById(labelId);
        if (mcdCustomLabelDef == null) throw new BaseException("无效的labelId值！");
        return mcdCustomLabelDef.getApproveFlowId();
    }


}

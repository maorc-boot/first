package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.approve.jx.dao.McdEmisReadTaskMapper;
import com.asiainfo.biapp.pec.approve.jx.dto.WarningDetailReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTask;
import com.asiainfo.biapp.pec.approve.jx.model.McdCampWarnEmisTaskExt;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.asiainfo.biapp.pec.approve.jx.service.IWarningService;
import com.asiainfo.biapp.pec.approve.jx.service.McdCampWarnEmisTaskService;
import com.asiainfo.biapp.pec.approve.jx.service.McdEmisReadTaskService;
import com.asiainfo.biapp.pec.approve.jx.utils.EmisUtils;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mamp
 * @date 2022/12/6
 */
@Service
@Slf4j
public class WarningServiceImpl implements IWarningService {

    @Resource
    private McdCampWarnEmisTaskService taskService;

    @Resource
    private McdEmisReadTaskMapper mcdEmisReadTaskMapper;

    @Resource
    private McdEmisReadTaskService mcdEmisReadTaskService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询阅知待办列表
     *
     * @param req
     * @return
     */
    @Override
    public IPage<McdCampWarnEmisTask> queryWarningList(WarningDetailReq req) {
        IPage page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        QueryWrapper<McdCampWarnEmisTask> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StrUtil.isNotEmpty(req.getCampsegKey()), McdCampWarnEmisTask::getCampsegId, req.getCampsegKey())
                .like(StrUtil.isNotEmpty(req.getCampsegKey()), McdCampWarnEmisTask::getCampsegName, req.getCampsegKey())
                // 保查询 未处理的
                .eq(McdCampWarnEmisTask::getStatus, 0)
                .eq(McdCampWarnEmisTask::getCreater,req.getCreateUser());
        return taskService.page(page, wrapper);

    }

    /**
     * 查询客户通渠道/自定义预警审批驳回阅知记录
     *
     * @param req 请求入参
     * @return {@link IPage}<{@link McdEmisReadTask}>
     */
    @Override
    public IPage<McdEmisReadTask> queryRejectList(McdPageQuery req) {
        String userId = UserUtil.getUserId(request);
        IPage<McdEmisReadTask> page = new Page<>(req.getCurrent(), req.getSize());
        LambdaQueryWrapper<McdEmisReadTask> readTaskWrapper = new LambdaQueryWrapper<>();
        readTaskWrapper.in(McdEmisReadTask::getBusinessType, 2, 3) // 2-预警 3-客户通渠道活动
                .eq(McdEmisReadTask::getStatus, 0) // 待阅
                .eq(McdEmisReadTask::getReadUser, userId) // 当前登陆人
                .and(StrUtil.isNotEmpty(req.getKeyWords()), c -> c.like(McdEmisReadTask::getName, req.getKeyWords())
                .or()
                .eq(StrUtil.isNotEmpty(req.getKeyWords()), McdEmisReadTask::getBusinessId, req.getKeyWords()));
        // .apply(StrUtil.isNotEmpty(req.getKeyWords()), "and (name like #{req.getKeyWords} or SUBSTRING_INDEX(ID, '_', -1) = #{req.getKeyWords} or BUSINESS_ID = #{req.getKeyWords})");
        return mcdEmisReadTaskMapper.selectPage(page, readTaskWrapper);
    }

    /**
     * 更新自定义预警&客户通渠道活动驳回Emis阅知待办状态
     *
     * @param query 自定义预警id&客户通渠道活动id
     * @return boolean
     */
    @Override
    public boolean updateRejectEmisReadTask(McdEmisReadTask query) {
        try {
            // 1.查询需要被消除阅知的活动或预警信息
            LambdaQueryWrapper<McdEmisReadTask> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(McdEmisReadTask::getStatus, 0)
                    .eq(McdEmisReadTask::getId, query.getId());
            McdEmisReadTask one = mcdEmisReadTaskService.getOne(queryWrapper);
            if (ObjectUtil.isEmpty(one)) {
                log.warn("updateRejectEmisReadTask-->没有需要更新阅知为已阅的待办");
                return false;
            }
            // 2.组装消除阅知emis接口调用入参
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unid", one.getId());
            // 需要阅知的人(创建人)
            jsonObject.put("approvalUserId", one.getReadUser());
            jsonObject.put("nodeId", one.getNodeId());
            // 增加阅知的人(审批节点驳回人)
            jsonObject.put("createUserid", one.getSubmitUser());
            // 3.消除emis侧阅知待办 待办转已阅
            boolean updateReadMessage = EmisUtils.updateReadMessage(jsonObject, one.getReadUser(), one.getBusinessId());
            log.info("updateRejectEmisReadTask-->阅知待办转已阅返回updateReadMessage={}", updateReadMessage);
            // 4.更新阅知待办任务表状态为已阅
            LambdaUpdateWrapper<McdEmisReadTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(McdEmisReadTask::getStatus, 1) // 已阅
                    .eq(McdEmisReadTask::getStatus, 0)
                    .eq(McdEmisReadTask::getName, query.getName())
                    .eq(McdEmisReadTask::getBusinessId, query.getBusinessId())
                    .eq(McdEmisReadTask::getBusinessType, query.getBusinessType())
                    .eq(McdEmisReadTask::getNodeId, query.getNodeId());
            // .apply("where SUBSTRING_INDEX(ID, '_', -1) = #{query.getId}");
            boolean update = mcdEmisReadTaskService.update(updateWrapper);
            // 5.待办转已阅成功且更新任务为已阅成功才算
            return updateReadMessage && update;
        } catch (Exception e) {
            log.error("id={}更新自定义预警&客户通渠道活动驳回Emis阅知待办状态异常：", query.getId(), e);
            return false;
        }
    }

    @Override
    public IPage<McdCampWarnEmisTaskExt> queryWarnDetailList(WarningDetailReq req) {
        Page page = new Page(req.getCurrent(), req.getSize());
        return taskService.queryWarnDetailList(page, req);
    }

    @Override
    public IPage<McdCampWarnEmisTaskExt> queryWarnSumList(WarningDetailReq req) {
        Page page = new Page(req.getCurrent(), req.getSize());
        return taskService.queryWarnSumList(page, req);
    }

    /**
     * 处理待办
     *
     * @param task
     * @return
     */
    @Override
    public Boolean dealWarning(McdCampWarnEmisTask task) {
       LambdaQueryWrapper<McdCampWarnEmisTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCampWarnEmisTask::getUniqueIdentifierId,task.getUniqueIdentifierId());
        McdCampWarnEmisTask emisTask = taskService.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(emisTask)) {
            return false;
        }
        // 调用Emis接口取消 Emis预警待办
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UNIQUE_IDENTIFIER_ID", emisTask.getUniqueIdentifierId());
        jsonObject.put("CAMPSEG_ID", emisTask.getCampsegId());
        jsonObject.put("CREATER", emisTask.getCreater());
        LambdaUpdateWrapper<McdCampWarnEmisTask> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(McdCampWarnEmisTask::getStatus, 1).eq(McdCampWarnEmisTask::getUniqueIdentifierId, task.getUniqueIdentifierId());
        EmisUtils.updateWarnMessage(jsonObject,task.getCreater());
        return taskService.update(wrapper);
    }
}

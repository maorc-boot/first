package com.asiainfo.biapp.pec.approve.jx.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.approve.common.MetaHandler;
import com.asiainfo.biapp.pec.approve.jx.dao.McdEmisReadTaskMapper;
import com.asiainfo.biapp.pec.approve.jx.dto.ReadInfoReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.asiainfo.biapp.pec.approve.jx.service.IReadService;
import com.asiainfo.biapp.pec.approve.jx.service.McdEmisReadTaskService;
import com.asiainfo.biapp.pec.approve.jx.utils.EmisUtils;
import com.asiainfo.biapp.pec.approve.model.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mamp
 * @date 2022/12/6
 */
@Service
@Slf4j
public class ReadServiceImpl implements IReadService {

    @Resource
    private McdEmisReadTaskService taskService;

    @Resource
    private McdEmisReadTaskMapper mcdEmisReadTaskMapper;


    /**
     * 查询阅知待办列表
     *
     * @param req
     * @return
     */
    @Override
    public IPage<McdEmisReadTask> queryReadList(ReadInfoReq req) {
        IPage page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        User user = MetaHandler.getUser();
        if (null != user && StrUtil.isNotEmpty(user.getUserId())) {
            req.setLoginUserId(user.getUserId());
        }
        return mcdEmisReadTaskMapper.queryReadList(page, req);
    }

    /**
     * 处理待办
     *
     * @param task
     * @return
     */
    @Override
    public Boolean dealRead(McdEmisReadTask task) {

        LambdaQueryWrapper<McdEmisReadTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdEmisReadTask::getStatus, 0).eq(McdEmisReadTask::getId, task.getId());
        McdEmisReadTask one = taskService.getOne(queryWrapper);
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
        UpdateWrapper<McdEmisReadTask> wrapper = new UpdateWrapper<>();
        if(updateReadMessage){
            wrapper.set("STATUS", 1).in("ID", task.getId().split(","));
            return taskService.update(wrapper);
        }else{
            wrapper.set("STATUS", 3).in("ID", task.getId().split(","));
            taskService.update(wrapper);
            return false;
        }
    }
}



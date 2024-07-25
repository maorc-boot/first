package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import com.asiainfo.biapp.pec.plan.vo.req.CampChannelExtQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mamp
 * @date 2023/4/11
 */
@Service
@Slf4j
public class ChannelConfServiceImpl802 implements ChannelConfService {

    /**
     * 获取渠道扩展配置信息
     *
     * @param object
     * @param task
     * @return
     */
    @Override
    public CampChannelExtQuery getChannelExtConf(Object[] object, McdCampImportTask task) throws Exception {
        if (object.length < 20) {
            throw new Exception("配置数据不完整");
        }
        CampChannelExtQuery channelExtConf = new CampChannelExtQuery();
        //渠道扩展表信息
        Map<String,Object> channelExt = (Map<String, Object>) object[20];
        // 默认全量推送
        channelExtConf.setColumnExt1("1");
        // 营销用语,营销用语为空时，取扩展字段E_802_MARKETING
        channelExtConf.setColumnExt2(object[15] == null? channelExt.get("E_802_MARKETING").toString():object[15].toString());
        // 外呼类型
        channelExtConf.setColumnExt3(channelExt.get("E_802_CALL_TYPE") == null? "":channelExt.get("E_802_CALL_TYPE").toString());
        // 营销(服务)目标 客户及提取原则
        channelExtConf.setColumnExt4(channelExt.get("E_802_CUSTGROUP_RULE") == null? "":channelExt.get("E_802_CUSTGROUP_RULE").toString());
        // 任务描述及要求
        channelExtConf.setColumnExt5(channelExt.get("E_802_TASK_DESCRIPTION") == null? "":channelExt.get("E_802_TASK_DESCRIPTION").toString());
        //外呼需求附件
        channelExtConf.setColumnExt6(channelExt.get("E_802_SERV_FILE_NAME") == null? "":channelExt.get("E_802_SERV_FILE_NAME").toString());
        //标签属性ID（推送至触点的标签属性ID）
        // channelExtConf.setColumnExt7(channelExt.get("E_811_PROJECT_LEVEL_2") == null? "":channelExt.get("E_811_PROJECT_LEVEL_2").toString());
        //标准化素材ID
        // channelExtConf.setColumnExt20(channelExt.get("") == null? "":channelExt.get("").toString());
        return channelExtConf;
    }

    @Override
    public String getChannelId() {
        return "802";
    }
}

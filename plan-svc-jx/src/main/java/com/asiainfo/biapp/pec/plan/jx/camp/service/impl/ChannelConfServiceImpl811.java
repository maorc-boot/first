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
public class ChannelConfServiceImpl811 implements ChannelConfService {

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
        // 营销用语
        channelExtConf.setColumnExt2(object[15] == null? "":object[15].toString());
        // 取消推荐规则设置
        channelExtConf.setColumnExt3(channelExt.get("cancelRecommendCycle") == null? "0":"1");
        // 取消推荐时间
        channelExtConf.setColumnExt4(channelExt.get("cancelRecommendCycle") == null? "":channelExt.get("cancelRecommendCycle").toString());
        // 项目层级二ID
        // channelExtConf.setColumnExt5(channelExt.get("") == null? "":channelExt.get("").toString());
        //项目层级三ID
        channelExtConf.setColumnExt6(channelExt.get("E_811_PROJECT_LEVEL_3") == null? "":channelExt.get("E_811_PROJECT_LEVEL_3").toString());
        //项目层级二名称
        channelExtConf.setColumnExt7(channelExt.get("E_811_PROJECT_LEVEL_2") == null? "":channelExt.get("E_811_PROJECT_LEVEL_2").toString());
        //项目层级三名称
        channelExtConf.setColumnExt8(channelExt.get("E_811_SUB_TASK_TYPE") == null? "":channelExt.get("E_811_SUB_TASK_TYPE").toString());
        //营销(服务)目标客群提取规则
        channelExtConf.setColumnExt9(channelExt.get("E_811_CUSTGROUP_RULE") == null? "":channelExt.get("E_811_CUSTGROUP_RULE").toString());
        //外呼需求附件
        channelExtConf.setColumnExt10(channelExt.get("E_811_SERV_FILE_NAME") == null? "":channelExt.get("E_811_SERV_FILE_NAME").toString());
        //任务描述及要求
        channelExtConf.setColumnExt11(channelExt.get("E_811_TASK_DESCRIPTION") == null? "":channelExt.get("E_811_TASK_DESCRIPTION").toString());
        //标签属性ID（推送至触点的标签属性ID）
        // channelExtConf.setColumnExt12(channelExt.get("") == null? "":channelExt.get("").toString());
        //标准化素材ID
        // channelExtConf.setColumnExt20(channelExt.get("") == null? "":channelExt.get("").toString());
        return channelExtConf;
    }

    @Override
    public String getChannelId() {
        return "811";
    }
}

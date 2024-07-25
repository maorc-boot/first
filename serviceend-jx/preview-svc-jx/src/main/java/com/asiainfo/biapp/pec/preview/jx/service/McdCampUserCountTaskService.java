package com.asiainfo.biapp.pec.preview.jx.service;

import com.asiainfo.biapp.pec.preview.jx.model.McdCampUserCountTask;
import com.asiainfo.biapp.pec.preview.jx.query.McdCampUserCountQuery;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author mamp
 * @description 针对表【mcd_camp_user_count_task(IOP策略用户统计任务表)】的数据库操作Service
 * @createDate 2024-07-18 15:47:18
 */
public interface McdCampUserCountTaskService extends IService<McdCampUserCountTask> {

    /**
     * 开启策略用户数统计任务
     */
    void startTask(McdCampUserCountQuery query);

}

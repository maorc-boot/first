package com.asiainfo.biapp.pec.preview.jx.mapper;

import com.asiainfo.biapp.pec.preview.jx.model.McdCampUserCountTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author AI
* @description 针对表【mcd_camp_user_count_task(IOP策略用户统计任务表)】的数据库操作Mapper
* @createDate 2024-07-18 15:47:18
* @Entity com.asiainfo.biapp.pec.preview.jx.model.McdCampUserCountTask
*/
public interface McdCampUserCountTaskMapper extends BaseMapper<McdCampUserCountTask> {

    /**
     * 插入 mcd_camp_user_count_task 表数据
     */
    void insertUserCountTask();

}





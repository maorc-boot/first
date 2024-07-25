package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.model.McdEmisTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author mamp
 * @date 2022/9/19
 */
public interface McdEmisTaskDao extends BaseMapper<McdEmisTask> {

    void updateMcdEmisTask();
}

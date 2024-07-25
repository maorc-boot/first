package com.asiainfo.biapp.pec.approve.jx.dao;

import com.asiainfo.biapp.pec.approve.jx.dto.ReadInfoReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 阅知待办任务表 Mapper 接口
 * </p>
 *
 * @author mamp
 * @since 2022-12-07
 */
public interface McdEmisReadTaskMapper extends BaseMapper<McdEmisReadTask> {

    IPage<McdEmisReadTask> queryReadList (IPage page , @Param("param") ReadInfoReq req);
}

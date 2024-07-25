package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.jx.dto.ReadInfoReq;
import com.asiainfo.biapp.pec.approve.jx.model.McdEmisReadTask;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author mamp
 * @date 2022/12/6
 */
public interface IReadService {

    /**
     * 查询阅知待办列表
     *
     * @param req
     * @return
     */
    IPage<McdEmisReadTask> queryReadList(ReadInfoReq req);

    /**
     * 处理待办
     *
     * @param vo
     * @return
     */
    Boolean dealRead(McdEmisReadTask vo);

}

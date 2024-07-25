package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service;


import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveGMsgKeywordsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IFiveGMsgKeywordsService extends IService<FiveGMsgKeywordsVo> {

    /**
     * 获取5G消息关键字信息
     *
     * @return
     * @throws Exception
     */
    List<FiveGMsgKeywordsVo> queryFiveGKeywordsInfo(String applicationNum, String keyWords, Pager pager, boolean flag) throws Exception;

    /**
     * 根据关键词ID删除关键词数据
     *FiveGMsgKeywordsServiceImpl
     * @param keywords 关键词ID
     * @throws Exception
     */
    void delFiveGKeywordsInfo(String keywords) throws Exception;

    /**
     * 新增或修改5G消息关键字信息
     *
     * @param flag  修改或者新增标志
     * @param fiveGMsg  关键词信息
     */
    void modifyFiveGKeywordsInfo(boolean flag, FiveGMsgKeywordsVo fiveGMsg, UserSimpleInfo user) throws Exception;
}

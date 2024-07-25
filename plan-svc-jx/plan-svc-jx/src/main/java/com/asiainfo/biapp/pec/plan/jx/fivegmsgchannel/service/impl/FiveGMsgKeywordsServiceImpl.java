package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.impl;

import com.asiainfo.biapp.client.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.iopws.util.Pager;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dao.IFiveGMsgKeywordsDao;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IFiveGMsgKeywordsService;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveGMsgKeywordsVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FiveGMsgKeywordsServiceImpl extends ServiceImpl<IFiveGMsgKeywordsDao, FiveGMsgKeywordsVo> implements IFiveGMsgKeywordsService {
    private static final Logger logger = LoggerFactory.getLogger(FiveGMsgKeywordsServiceImpl.class);

    @Resource
    private IFiveGMsgKeywordsDao fiveGMsgKeywordsDao;

    /**
     * 获取5G消息关键字信息
     * @return List<FiveGMsgKeywordsVo>
     * @throws Exception 异常
     */
    @Override
    public List<FiveGMsgKeywordsVo> queryFiveGKeywordsInfo(String applicationNum, String keyWords, Pager pager, boolean flag) throws Exception {
        String pageNum = String.valueOf(pager.getPageNum());
        String pageSize = String.valueOf(pager.getPageSize());
        int county = fiveGMsgKeywordsDao.queryFiveGKeywordsCount(applicationNum, keyWords, flag);
        pager.setTotalSize(county);
        return fiveGMsgKeywordsDao.queryFiveGKeywordsInfo(applicationNum, keyWords, pageSize, pageNum, flag);
    }

    /**
     * 根据关键词ID删除关键词数据
     * @param keywords 关键字ID
     * @throws Exception 异常
     */
    @Override
    public void delFiveGKeywordsInfo(String keywords) throws Exception {
        fiveGMsgKeywordsDao.deleteFiveGKeywordsInfo(keywords);
    }

    /**
     * 新增或修改5G消息关键字信息
     * @param flag     修改或者新增标志
     * @param fiveGMsg 关键词信息
     */
    @Override
    public void modifyFiveGKeywordsInfo(boolean flag, FiveGMsgKeywordsVo fiveGMsg, UserSimpleInfo user) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        fiveGMsg.setCreateTime(dateFormat.parse(dateFormat.format(new Date())));
        if (flag) {
            fiveGMsgKeywordsDao.addFiveGKeywordsInfo(fiveGMsg);
        } else {
            fiveGMsg.setCreateUser(user.getUserName());
            fiveGMsgKeywordsDao.updateFiveGKeywordsInfo(fiveGMsg);
        }
    }
}

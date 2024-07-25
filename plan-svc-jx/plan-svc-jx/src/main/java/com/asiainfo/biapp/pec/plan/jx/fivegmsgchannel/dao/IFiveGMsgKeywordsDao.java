package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dao;


import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveGMsgKeywordsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFiveGMsgKeywordsDao extends BaseMapper<FiveGMsgKeywordsVo> {

    /**
     * 查询列表总数
     * @param applicationNum
     * @param keyWords
     * @param flag
     * @return
     */
    int queryFiveGKeywordsCount(@Param("applicationNum") String applicationNum, @Param("keyWords") String keyWords, @Param("flag") boolean flag);

    /**
     * 查询列表数据
     * @param applicationNum
     * @param keyWords
     * @param pageSize
     * @param pageNum
     * @param flag
     * @return
     */
    List<FiveGMsgKeywordsVo> queryFiveGKeywordsInfo(@Param("applicationNum") String applicationNum, @Param("keyWords") String keyWords, @Param("pageSize") String pageSize, @Param("pageNum") String pageNum, @Param("flag") boolean flag);

    /**
     * 删除
     * @param keywords
     */
    void deleteFiveGKeywordsInfo(@Param("keywords") String keywords);

    void addFiveGKeywordsInfo(@Param("fiveGMsg") FiveGMsgKeywordsVo fiveGMsg);

    void updateFiveGKeywordsInfo(@Param("fiveGMsg") FiveGMsgKeywordsVo fiveGMsg);
}

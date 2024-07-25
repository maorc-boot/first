package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.req.CampStatReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeChnExecReq;
import com.asiainfo.biapp.pec.eval.jx.req.EvalHomeReq;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeCampStatVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeChnExecVO;
import com.asiainfo.biapp.pec.eval.jx.vo.EvalHomeDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/5/6
 */
@Mapper
public interface EvalHomeDao {
    /**
     * 总营销数
     *
     * @param param
     * @return
     */
    List<EvalHomeDataVo> queryTotal(@Param("param") EvalHomeReq param);

    /**
     * 总营成功数
     *
     * @param param
     * @return
     */
    List<EvalHomeDataVo> querySuccess(@Param("param") EvalHomeReq param);

    /**
     * 营销转化率
     *
     * @param param
     * @return
     */
    List<EvalHomeDataVo> querySuccessRate(@Param("param") EvalHomeReq param);


    /**
     * 评估-效果总览-渠道执行情况
     *
     * @param req
     * @return
     * @throws Exception
     */
    List<EvalHomeChnExecVO> queryChnExec(@Param("param") EvalHomeChnExecReq req) throws Exception;


    /**
     * 评估-效果总览-策略统计
     *
     * @param req
     * @return
     */
    List<EvalHomeCampStatVO> queryCampStatistics(@Param("param") CampStatReq req) throws Exception;

    /**
     * 查询策略列表
     *
     * @return
     */
    List queryCampList(@Param("keyword") String keyword);

    /**
     * 查询所有地市
     *
     * @return
     */
    List<Map<String, String>> queryAllCity();


}

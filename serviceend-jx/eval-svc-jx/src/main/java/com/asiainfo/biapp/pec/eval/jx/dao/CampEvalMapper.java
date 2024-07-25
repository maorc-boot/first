package com.asiainfo.biapp.pec.eval.jx.dao;

import com.asiainfo.biapp.pec.eval.jx.req.CampEvalReq;
import com.asiainfo.biapp.pec.eval.jx.req.ChannelRadarMapReq;
import com.asiainfo.biapp.pec.eval.jx.vo.CampEvalVo;
import com.asiainfo.biapp.pec.eval.jx.vo.UseEffectVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/1/9
 */
@Mapper
public interface CampEvalMapper {
    /**
     * 查询日使用效果
     *
     * @param dataDate
     * @return
     */
    List<UseEffectVo> queryDayUseEffect(@Param("dataDate") String dataDate);


    /**
     * 查询月使用效果
     *
     * @param dataDate
     * @return
     */
    List<UseEffectVo> queryMonthUseEffect(@Param("dataDate") String dataDate);


    /**
     * 活动评估
     *
     * @param req
     * @return
     */
    IPage<CampEvalVo> queryCampEffect(Page page, @Param("param") CampEvalReq req);

    /**
     * 活动评估-日视图
     *
     * @param req
     * @return
     */
    IPage<CampEvalVo>   queryCampEffectDay(Page page,@Param("param") CampEvalReq req);
    /**
     * 活动评估-月视图
     *
     * @param req
     * @return
     */
    IPage<CampEvalVo>  queryCampEffectMonth(Page page,@Param("param") CampEvalReq req);


    /**
     * 渠道雷达图数据查询
     *
     * @param req req
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryChannelRadarMap(ChannelRadarMapReq req);

    /**
     * 查询执行中、已完成所属各渠道活动总数
     *
     * @return int
     */
    List<Map<String, String>> queryChannelCampCount();

    /**
     * 查询执行中、已完成活动总数
     *
     * @return int
     */
    int queryCampCount();
}

package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.req.CampThemeQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPrismCampThemeVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SceneCampVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.ThemeCampStatDetail;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismCampTheme;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
public interface IMcdPrismCampThemeDao extends BaseMapper<McdPrismCampTheme> {

    /**
     * 分页查询主题信息
     *
     * @param page 分页信息
     * @param req 入参对象
     * @return {@link IPage}<{@link McdPrismCampThemeVO}>
     */
    IPage<McdPrismCampThemeVO> searchThemeCampList(Page<McdPrismCampThemeVO> page, @Param("param") CampThemeQuery req);

    /**
     * 根据主题id批量查询主题下的活动
     *
     * @param themeIdList 主题id集合
     * @return {@link List}<{@link SceneCampVO}>
     */
    List<SceneCampVO> searchCampListByTheme(@Param("themeIdList") List<String> themeIdList);
    
    List<ThemeCampStatDetail> searchAllCampStatAndPreviewByTheme(@Param("param") String themeId);

    /**
     * 根据主题批量查询渠道信息以及所有活动的状态及是否是预演策略
     *
     * @param themeIdList 主题id集合
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<ThemeCampStatDetail> listChannelIdByThemeId(@Param("themeIdList") List<String> themeIdList);
}

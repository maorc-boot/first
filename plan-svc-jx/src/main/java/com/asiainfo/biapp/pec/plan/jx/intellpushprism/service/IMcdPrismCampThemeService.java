package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service;

import com.asiainfo.biapp.pec.plan.jx.camp.req.CampThemeQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdPrismCampThemeVO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.SceneCampVO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismCampTheme;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * description:
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
public interface IMcdPrismCampThemeService extends IService<McdPrismCampTheme> {

    /**
     * 查询主题信息
     * @param req
     * @return
     */
    IPage<McdPrismCampThemeVO> searchThemeCampList(CampThemeQuery req);

    /**
     * 根据主题ID查询策略
     * @param req
     * @return
     */
    IPage<SceneCampVO> searchCampByTheme(CampThemeQuery req);
}

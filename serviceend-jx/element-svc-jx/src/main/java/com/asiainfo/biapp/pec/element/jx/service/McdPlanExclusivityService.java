package com.asiainfo.biapp.pec.element.jx.service;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.element.jx.entity.McdPlanExclus;
import com.asiainfo.biapp.pec.element.jx.entity.McdPlanExclusivity;
import com.asiainfo.biapp.pec.element.jx.query.PlanExcluQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 产品互斥关系表 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
public interface McdPlanExclusivityService extends IService<McdPlanExclusivity> {

    IPage<McdPlanExclusivity> getPlanExclusivityList(McdPageQuery excluQuery);
    McdPlanExclus getPlanExclusivityInfo(PlanExcluQuery excluQuery);

    void saveOrUpdatePlanExclusivity(McdPlanExclusivity  exclusivity);

    /**
     * 互斥产品模板下载
     *
     * @param response
     */
    void exportPlanExcluTemplate(HttpServletResponse response);
    boolean uploadPlanExcluFile(MultipartFile uploadPlanExcluFile) throws Exception;
}

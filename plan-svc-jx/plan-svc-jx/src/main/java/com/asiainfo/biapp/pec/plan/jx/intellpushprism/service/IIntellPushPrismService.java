package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.TemplateByTypeRespondDTO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.ColumnSearchRespondDTO;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity.McdPrismCampTemplate;
import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdPlanDef;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * description: 智推棱镜service层
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
public interface IIntellPushPrismService {

    /**
     * 根据主题id、名称校验主题是否存在
     *
     * @param themeId 主题id
     * @param themeName 主题名称
     * @return boolean true-存在
     */
    boolean checkThemes(String themeId, String themeName);

    /**
     * 保存场景信息
     *
     * @param req 保存入参对象
     * @param user 当前登录用户对象
     * @return {@link String}
     */
    List<String> saveScene(IntellPushPrismReqVO req, UserSimpleInfo user);

    /**
     * 验证活动结束时间
     *
     * @param req 保存入参对象
     */
    void checkCampEndDate(TacticsInfoJx req);

    /**
     * 批量导入产品模板下载
     *
     * @param response 响应流
     */
    void batchImpPlanTmpDownload(HttpServletResponse response);

    /**
     * 批量导入客群模板下载
     *
     * @param response 响应流
     */
    void batchImpCustTmpDownload(HttpServletResponse response);

    /**
     * 批量导入标签模板下载
     *
     * @param response 响应流
     */
    void batchImpLabelTmpDownload(HttpServletResponse response);

    /**
     * 批量导入产品
     *
     * @param multipartFile 文件
     * @return List<McdPlanDef>
     * @throws Exception 异常
     */
    List<McdPlanDef> batchImportProduct(MultipartFile multipartFile) throws Exception;

    /**
     * 批量导入客群
     *
     * @param multipartFile 文件
     * @return {@link List}<{@link BatchImportCustRespVO}>
     * @throws Exception 异常
     */
    List<BatchImportCustRespVO> batchImportCust(MultipartFile multipartFile) throws Exception;

    /**
     * 批量导入标签
     *
     * @param multipartFile 文件
     * @return {@link List}<{@link ColumnSearchRespondDTO.ColumnList}>
     * @throws Exception 异常
     */
    List<ColumnSearchRespondDTO.ColumnList> batchImportLabel(MultipartFile multipartFile) throws Exception;

    /**
     * 主题模板下载
     *
     * @param response 响应流
     */
    void themeTemplateDownload(HttpServletResponse response);

    /**
     * 查询各类型下的模板
     *
     * @param pageQuery 入参对象
     * @return {@link List}<{@link TemplateByTypeRespondDTO}>
     */
    List<TemplateByTypeRespondDTO> queryTemplateByType(McdPageQuery pageQuery);

    /**
     * 分页查询模板列表数据
     *
     * @param paramVO 分页查询入参对象
     * @return {@link IPage}<{@link McdPrismCampTemplate}>
     */
    IPage<McdPrismCampTemplate> queryTemplateList(TemplateListParamVO paramVO, UserSimpleInfo user);

    /**
     * 批量删除主题下策略信息
     *
     * @param themeId 主题id
     */
    boolean batchDelCampseg(String themeId);

    /**
     * 根据主题id查询画布流程详细信息
     *
     * @param reqVO 入参
     * @return {@link McdPrismThemeDetailRespVO}
     */
    McdPrismThemeDetailRespVO getThemeDetailByThemeId(QueryCampByThemeIdOrTepIdReqVO reqVO);

    /**
     * 根据模板id查询画布流程详细信息
     *
     * @param reqVO 入参
     * @return {@link McdPrismCampTemplate}
     */
    McdPrismThemeDetailRespVO getTemplateDetailByTemplateId(QueryCampByThemeIdOrTepIdReqVO reqVO);

    /**
     * 复制主题
     *
     * @param sourceThemeId 源主题id
     * @param newThemeName 新的主题名称
     * @param user 用户信息
     * @param flag flag
     * @return 复制后的根活动id
     */
    String copyTheme(String sourceThemeId, String newThemeName, UserSimpleInfo user, String flag);

    /**
     * 我的模板、首页模板信息一键引用
     *
     * @param templateId 模板id
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> oneClickRefMyTemplate(String templateId);

    /**
     * 根据主题或者模版查询下面的活动
     *
     * @param reqVO 入参
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryCampInfoByThemeIdOrTemplateId(QueryCampByThemeIdOrTepIdReqVO reqVO);
}

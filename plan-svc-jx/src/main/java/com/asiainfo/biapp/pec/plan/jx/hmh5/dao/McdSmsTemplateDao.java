package com.asiainfo.biapp.pec.plan.jx.hmh5.dao;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCareSmsTemplate;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.*;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupAttrList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * description: 江西客户通关怀短信模板dao
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
public interface McdSmsTemplateDao extends BaseMapper<McdCareSmsTemplate> {

    /**
     * 查询关怀短信模板列表数据
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage}<{@link McdCareSmsTemplate}>
     */
    IPage<McdCareSmsTemplateResp> getCareSmsTemplate(Page<McdCareSmsTemplateResp> page, @Param("query") McdCareSmsTemplateQuery query);

    /**
     * 获取营销用语变量替换信息
     *
     * @return {@link List}<{@link McdCustgroupAttrList}>
     */
    List<McdCustgroupAttrList> getCustGroupVars(@Param("pCode") String pCode);

    /**
     * 根据模板编码查询关怀短信模板详情
     *
     * @param templateCode 模板代码
     * @return {@link McdCareSmsTemplateResp}
     */
    McdCareSmsTemplateResp getCareSmsTemplateDetail(@Param("templateCode") String templateCode);

    /**
     * 根据模板编码查询关怀短信模板审批详情
     *
     * @param templateCode 模板代码
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> getApproveInfo(@Param("templateCode") String templateCode);

    /**
     * 素材审批列表查询
     *
     * @param templateCodes 模板编码集合
     * @param req req
     * @return List<SmsTemplateApprRecord>
     */
    List<SmsTemplateApprRecord> qryApprRecord(@Param("templateCodes") Set<String> templateCodes, @Param("query") CareSmsTemplateApproveJxQuery req);

    /**
     * 获取短信模板配置表序列
     *
     * @return 短信模板配置表序列
     */
    int getSeq();

    /**
     * 查询代维短信标签列表
     *
     * @return {@link List}<{@link McdFrontCareSmsLabel}>
     */
    List<McdFrontCareSmsLabel> getLabelListDaiwei();

    /**
     * 根据模板id查询关联的标签数据
     *
     * @param smsTemplateCode 模板编号
     * @return {@link List}<{@link McdFrontCareSmsLabel}>
     */
    List<McdFrontCareSmsLabel> getRelaLabels(@Param("smsTemplateCode")String smsTemplateCode);

    /**
     * 查询关怀短信模板使用的旧的标签
     *
     * @param smsTemplateCode 短信模板编号
     * @return {@link List}<{@link String}>
     */
    List<String> selectCareSmsLabelCodeList(@Param("smsTemplateCode") String smsTemplateCode);

    /**
     * 根据模板编号删除使用的标签信息
     *
     * @param smsTemplateCode 模板编号
     * @param labelCodeList 标签编码集合
     */
    void deleteCareSmsLabelRela(@Param("smsTemplateCode") String smsTemplateCode, @Param("labelCodeList") List<String> labelCodeList);
}

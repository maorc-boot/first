package com.asiainfo.biapp.pec.element.jx.material.dao;

import com.asiainfo.biapp.pec.core.enums.KeyWordsEnum;
import com.asiainfo.biapp.pec.element.dto.request.DimMaterialPageListRequest;
import com.asiainfo.biapp.pec.element.jx.material.model.McdDimMaterialJxModel;
import com.asiainfo.biapp.pec.element.jx.material.request.DimMaterialApproveJxQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.ExportMaterialInfoQuery;
import com.asiainfo.biapp.pec.element.jx.material.response.DimMaterialJxResponse;
import com.asiainfo.biapp.pec.element.jx.material.response.actionFrom.DimActionJxFrom;
import com.asiainfo.biapp.pec.element.query.McdDimMaterialQuery;
import com.asiainfo.biapp.pec.element.query.appr.ApprRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 营销素材 Mapper 接口
 * </p>
 *
 * @author wuhq6
 * @since 2021-11-29
 */
@Mapper
public interface McdDimMaterialJxDao extends BaseMapper<McdDimMaterialJxModel> {


    /**
     * 导入素材excel文件 mapper
     *
     * @param materialsList 素材对象列表
     * @return int
     */
    int batchSaveMaterials(List<McdDimMaterialJxModel> materialsList);

    /**
     * 素材excel文件下载'
     *
     * @param exportQuery
     * @return
     */
    List<DimActionJxFrom> selectFromList(@Param("exportQ") ExportMaterialInfoQuery exportQuery);

    /**
     * 查询素材列表
     *
     * @param page
     * @param dimMaterialPageListRequest
     * @return
     */
    IPage<DimMaterialJxResponse> selectPageList(IPage<DimMaterialJxResponse> page, @Param("request") DimMaterialPageListRequest dimMaterialPageListRequest);

    /**
     * 添加到优秀素材库
     *
     * @param materialId
     * @return
     */
    Integer addExcellenceById(String materialId);

    /**
     * 移除出优秀素材库
     *
     * @param materialId
     * @return
     */
    Integer removeExcellenceById(String materialId);

    /**
     * 修改审批通过的状态
     *
     * @param flowId
     * @param materialStat
     */
    void updateByFlowId(@Param("flowId") String flowId, @Param("materialStat") Integer materialStat);

    /**
     * 修改素材上下线状态
     *
     * @param materialId
     * @param statusId
     * @return
     */
    boolean saveOrUpdatePlanStatus(@Param("materialId") String materialId, @Param("statusId") String statusId);

    /**
     * 修改素材信息
     *
     * @param mcdDimMaterial
     * @return
     */
    int updateByMaterialId(@Param("mcdDimMaterial") McdDimMaterialQuery mcdDimMaterial);

    /**
     * 素材审批列表查询
     *
     * @param materialIds
     * @param
     * @return
     */
    List<ApprRecord> qryApprRecord(@Param("materialIds") Set<String> materialIds, @Param("param") DimMaterialApproveJxQuery req);
    
    /**
     * 删除我的素材
     *
     * @param materialId 素材ID
     * @return 影响行数
     */
    Integer removeMyMaterialById(String materialId);
}

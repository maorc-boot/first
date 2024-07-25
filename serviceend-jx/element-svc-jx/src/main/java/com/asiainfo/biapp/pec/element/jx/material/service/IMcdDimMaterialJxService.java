package com.asiainfo.biapp.pec.element.jx.material.service;

import com.asiainfo.biapp.pec.element.dto.MaterialStatusDTO;
import com.asiainfo.biapp.pec.element.dto.request.*;
import com.asiainfo.biapp.pec.element.dto.response.AdivInfoListResponse;
import com.asiainfo.biapp.pec.element.dto.response.ChannelNameAndIdListResponse;
import com.asiainfo.biapp.pec.element.dto.response.ConctactListResponse;
import com.asiainfo.biapp.pec.element.dto.response.DimMaterialResponse;
import com.asiainfo.biapp.pec.element.jx.material.model.McdDimMaterialJxModel;
import com.asiainfo.biapp.pec.element.jx.material.request.DimAdivInfoJxQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.DimMaterialPageListQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.ExportMaterialInfoQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.McdDimMaterialNewQuery;
import com.asiainfo.biapp.pec.element.jx.material.response.DimMaterialJxResponse;
import com.asiainfo.biapp.pec.element.model.McdDimMaterial;
import com.asiainfo.biapp.pec.element.query.DimMaterialListQuery;
import com.asiainfo.biapp.pec.element.query.McdDimMaterialQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 营销素材 服务类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-3
 */
public interface IMcdDimMaterialJxService extends IService<McdDimMaterialJxModel> {

    /**
     * 素材分页(或根据条件)查询
     *
     * @return
     */
    IPage<DimMaterialJxResponse> queryMaterialPageList(DimMaterialPageListQuery req);

    /**
     * 查询素材信息
     *
     * @param materialId 素材id
     * @return 素材响应对象
     */
    DimMaterialJxResponse getMaterialInfo(String materialId);


    /**
     * 营销素材导入
     *
     * @param uploadMaterialsFile 导入文件
     * @return
     */
    boolean uploadMaterialsFile(MultipartFile uploadMaterialsFile,String userId) throws Exception;

    /**
     * 营销素材导出列表获取
     *
     * @param exportMaterialInfoQuery 入参
     * @return list
     */
    List<Object> exportMaterialsFile(ExportMaterialInfoQuery exportMaterialInfoQuery);

    /**
     *  excel导出
     * @param s
     * @param excelHeader
     * @param exportMaterialsList
     * @param response
     * @throws IOException
     */
    void exportExcel(String s, String[] excelHeader, List<Object> exportMaterialsList, HttpServletResponse response) throws IOException;

    /**
     * 素材模板下载
     *
     * @param response
     */
    void importMaterialsTemplate(HttpServletResponse response);

    /**
     * 上传素材 （图片）
     *
     * @param file
     * @return
     */
    Map<String, String> uploadMaterialPicture(MultipartFile file);

    /**
     *上传素材 (视频)
     *
     * @param file
     * @return
     */
    Map<String, String> uploadMaterialVideo(MultipartFile file);

    /**
     * 上传素材 (远程服务器保存)
     *
     * @param file 文件
     * @return
     */
    Map<String,String> uploadMaterialPictureBySftp(MultipartFile file, HttpServletRequest request);

    /**
     * 渠道 下拉框信息查询
     *
     * @return
     */
    ChannelNameAndIdListResponse listChannelInfo();

    /**
     * 触点 下拉框信息查询
     *
     * @param channelId 渠道ID
     * @return
     */
    ConctactListResponse listContactInfo(String channelId);

    /**
     * 运营位 下拉框信息查询
     *
     * @param jxQuery 入参对象
     * @return
     */
    AdivInfoListResponse listPositionInfo(DimAdivInfoJxQuery jxQuery);

    /**
     * 根据素材 ID 查询所有素材信息
     *
     * @param materialId
     * @return
     */
    List<McdDimMaterialJxModel> queryMaterialById(String materialId);

    /**
     * 图片 视频预览
     *
     * @param resourceUrl
     * @param dimMaterialRequest
     */
    void loadImage(String resourceUrl, DimMaterialRequest dimMaterialRequest, HttpServletResponse response);
    
    /**
     * 新建素材校验素材名称
     *
     * @param materialName
     * @return
     */
    boolean validateMaterialName(String materialName);
    
    /**
     * 修改素材检验素材名称
     * @param mcdDimMaterial
     * @return
     */
    boolean validateMaterialNameByUpdate(McdDimMaterialNewQuery mcdDimMaterial);
    
    /**
     * 优秀素材分页查询
     *
     * @param request
     * @return
     */
    IPage<DimMaterialJxResponse> pagelistExcellentMaterial(MaterialTemplateRequest request);
    
    /**
     * 优秀素材查询
     * @param materialId
     * @return
     */
    DimMaterialJxResponse getExcellentMaterialInfo(String materialId);
    
    /**
     * 根据素材id 添加素材到优秀素材库
     *
     * @param dimMaterialRequest
     * @return
     */
    boolean addExcellentMaterialLibrary(DimMaterialRequest dimMaterialRequest);
    
    /**
     *根据素材id 从优秀素材库移除
     *
     * @param dimMaterialRequest
     * @return
     */
    boolean removeExcellentMaterialLibrary(DimMaterialRequest dimMaterialRequest);
    
    /**
     * 优秀素材模板一键创建素材
     *
     * @param dimMaterialRequest
     * @return
     */
    boolean createExcellentMaterial(DimMaterialRequest dimMaterialRequest);
    
    /**
     * 我的素材 分頁
     *
     * @param request
     * @return
     */
    IPage<DimMaterialJxResponse> pagelistMyMaterial(MaterialTemplateRequest request);
    
    /**
     * 我的素材-素材详情
     *
     * @param materialId
     * @return
     */
    DimMaterialJxResponse getMyMaterialInfo(String materialId);
    
    /**
     * 热点素材-素材分页
     *
     * @param request
     * @return
     */
    IPage<DimMaterialJxResponse> pagelistHotSpotMaterial(MaterialTemplateRequest request);
    
    /**
     * 热点素材-素材详情
     *
     * @param materialId
     * @return
     */
    DimMaterialJxResponse getHotSpotMaterialInfo(String materialId);
    
    /**
     * 热点素材一键创建素材
     *
     * @param dimMaterialRequest
     * @return
     */
    boolean createHotSpotMaterial(DimMaterialRequest dimMaterialRequest);
    
    /**
     * 素材审批管理-审批素材分页
     *
     * @param request
     * @return
     */
    IPage<DimMaterialJxResponse> pagelistApproveMaterial(MaterialTemplateRequest request);
    
    /**
     * 新建素材
     *
     * @param mcdDimMaterial
     */
    boolean saveOrUpdateMaterial(McdDimMaterialNewQuery mcdDimMaterial);
    
    /**
     * 根据素材ID删除我的素材
     *
     * @param materialId
     * @return
     */
    boolean removeMyMaterialById(String materialId);
    
    /**
     * 修改素材上下线
     *
     * @param materialStatusDTO
     * @return
     */
    boolean saveOrUpdatePlanStatus(MaterialStatusDTO materialStatusDTO);
    
    
    /**
     * 修改素材信息
     *
     * @param mcdDimMaterial
     * @return
     */
    boolean updateMaterial(McdDimMaterialNewQuery mcdDimMaterial);
    
    /**
     * 根据运营位ID 获取素材列表(上线)
     *
     * @param dimMaterialListQuery
     * @return
     */
    List<McdDimMaterialJxModel> getIntelligentMatchingMaterialList(DimMaterialListQuery dimMaterialListQuery);
    
    /**
     * 删除素材
     *
     * @param materialId 素材id
     * @return true/false
     */
    boolean removeMaterialById(String materialId);
}

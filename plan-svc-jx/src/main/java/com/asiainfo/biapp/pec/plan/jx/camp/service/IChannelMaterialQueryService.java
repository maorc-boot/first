package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.jx.camp.model.ChannelMaterialModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ChannelMaterialListQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ChannelSelectMaterialListQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.DimMaterialPreviewRequest;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCommunicationQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCommunicationUsers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IChannelMaterialQueryService extends IService<ChannelMaterialModel> {

    IPage<ChannelMaterialModel> queryChannelMaterialList(ChannelMaterialListQuery req);
    List<ChannelMaterialModel> getSelectMaterialList(ChannelSelectMaterialListQuery req);
    /**
     * 根据素材 ID 查询所有素材信息
     *
     * @param materialId
     * @return
     */
    List<ChannelMaterialModel> queryMaterialById(String materialId);

    /**
     * 图片 视频预览
     *
     * @param resourceUrl
     * @param dimMaterialRequest
     */
    void loadImage(String resourceUrl, DimMaterialPreviewRequest dimMaterialRequest, HttpServletResponse response);



    IPage<McdCommunicationUsers> queryCommunicationUsers(McdCommunicationQuery pageQuery);
}

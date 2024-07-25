package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.ChannelMaterialModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCommunicationQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCommunicationUsers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface IChannelMaterialQueryDao extends BaseMapper<ChannelMaterialModel> {

    IPage<McdCommunicationUsers> queryCommunicationUsers(Page page , @Param("param") McdCommunicationQuery pageQuery);

    /**
     * 判断此素材的审批状态(仅查询草稿状态的素材信息)
     *
     * @param channelId 渠道id
     * @return {@link List}<{@link ChannelMaterialModel}>
     */
    List<ChannelMaterialModel>  queryMaterialRelChannelList(@Param("channelId") String channelId);

    /**
     * 根据素材id判断该素材是否有非草稿、预演完成状态的活动使用
     *
     * @param materialId 素材id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> chkHasCampUsedMaterial (@Param("materialId") String materialId);

    /**
     * 判断该素材是否广点通已回执审核状态且状态是驳回
     *
     * @param materialId 素材id
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> chkMaterialFallbackStat(@Param("materialId") String materialId);
}

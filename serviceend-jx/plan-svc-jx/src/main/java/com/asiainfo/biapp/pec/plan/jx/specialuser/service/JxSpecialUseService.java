package com.asiainfo.biapp.pec.plan.jx.specialuser.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.specialuser.model.JxMcdSpecialUse;
import com.asiainfo.biapp.pec.plan.model.McdSpecialUse;
import com.asiainfo.biapp.pec.plan.vo.SpecialUser;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseMod;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseQuery;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseSave;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 特殊客户群表 服务类
 * </p>
 *
 * @author imcd
 * @since 2021-12-06
 */
public interface JxSpecialUseService extends IService<McdSpecialUse> {

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    IPage<SpecialUser> pageSpecialUser(SpecialUseQuery query);

    /**
     * 批量插入
     *
     * @param specialUsesList
     */
    void saveBatchSpecialUser(List<JxMcdSpecialUse> specialUsesList);

    /**
     * 删除
     *
     * @param del
     */
    void delSpecialUser(SpecialUseSave del);

    /**
     * 修改
     *
     * @param mod
     */
    void modSpecialUser(SpecialUseMod mod,UserSimpleInfo user ) throws Exception;

    /**
     * 批量导入
     * @param multiFile
     * @param userType
     * @param channelId
     * @param startTime
     * @param endTime
     */
    String  importSpecialUser(MultipartFile multiFile, String userType, String channelId, Date startTime, Date endTime, UserSimpleInfo user) throws Exception;

}

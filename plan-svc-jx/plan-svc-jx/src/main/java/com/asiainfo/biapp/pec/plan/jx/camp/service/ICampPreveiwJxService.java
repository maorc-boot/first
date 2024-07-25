package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.plan.model.McdCampChannelList;
import com.asiainfo.biapp.pec.plan.model.McdCampDef;

import java.util.List;

/**
 * @author mamp
 * @date 2022/10/20
 */
public interface ICampPreveiwJxService {

    /**
     * 生成预演信息
     *
     * @param campDef
     * @param campChannelLists
     */
    void savePreview(McdCampDef campDef, List<McdCampChannelList> campChannelLists);
}

package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.vo.IopUnite;
import com.asiainfo.biapp.pec.plan.vo.RecomendUnityCampDetail;
import com.asiainfo.biapp.pec.plan.vo.req.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * IUnityTacticsManageService.java
 *
 * @author [Li.wang]
 * @CreateDate 2022/1/20 0020
 * @see com.asiainfo.biapp.pec.plan.service
 */
public interface IUnityTacticsManageJxService {
    IPage<IopUnite> searchIopUnityTactics(SearchUnityTacticsQuery form);

}

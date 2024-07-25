package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCallOutRecordingDetail;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdOutCallQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdIdentifierQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdOutDetailsVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IMcdCallDetailsService extends IService<McdCallOutRecordingDetail> {

    /**
     * 外呼明细查询
     *
     * @param query
     * @return
     */
    IPage queryCallDetailsList(McdOutCallQuery query, UserSimpleInfo user);

    /**
     * 外呼明细导出
     *
     * @param query
     * @param response
     */
    String exportExcel(McdOutCallQuery query,UserSimpleInfo user, HttpServletResponse response);

    /**
     * 通话明细查询
     *
     * @param query
     */
    List<McdOutDetailsVo> callHttpData(McdIdentifierQuery query);
}

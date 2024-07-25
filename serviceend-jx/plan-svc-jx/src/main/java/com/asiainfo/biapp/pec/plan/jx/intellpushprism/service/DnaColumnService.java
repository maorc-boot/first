package com.asiainfo.biapp.pec.plan.jx.intellpushprism.service;

import com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * dna标签获取service
 *
 * @author lvcc
 * @date 2024/04/15
 */
public interface DnaColumnService {

    /**
     * 选择标签
     * @param columnSearchRequestDTO
     * @return
     */
    DNAActionResponse<ColumnSearchRespondDTO> search(@RequestBody @Valid ColumnSearchRequestDTO columnSearchRequestDTO);

    /**
     * 获取标签映射值
     * @param columnValuePageRequestDTO
     * @return
     */
    DNAActionResponse<ColumnValuePageRespondDTO> getValuePage(@RequestBody @Valid ColumnValuePageRequestDTO columnValuePageRequestDTO);

    /**
     * 标签裂变获取客群编号
     *
     * @param labelFissionGetCustIdReqDTO 标签裂变获取客群编号接口请求参数
     * @return {@link DNACustomActionResponse}<{@link LabelFissionGetCustIdRespDTO}>
     */
    DNACustomActionResponse getLabelFissionCustId(@RequestBody LabelFissionGetCustIdReqDTO labelFissionGetCustIdReqDTO);

    /**
     * 根据标签映射值实时获取客群数量
     *
     * @param query
     * @return {@link DNACustomActionResponse}
     */
    DNACustomActionResponse getTargetUserCount(TargetUserCountReqDTO query);

}

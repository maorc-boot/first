package com.asiainfo.biapp.pec.approve.jx.service;

import com.asiainfo.biapp.pec.approve.dto.SubmitProcessDTO;
import com.asiainfo.biapp.pec.approve.jx.dto.SubmitProcessJxDTO;

/**
 * description: 江西策略统筹审批&&增加emis代办service
 *
 * @author: lvchaochao
 * @date: 2023/7/28
 */
public interface IMcdCoordinationAppr2Emis {

    /**
     * 策略统筹任务发起审批流程
     *
     * @param submitProcessDTO 提交过程dto
     * @return {@link Long}
     */
    Long commitProcess(SubmitProcessJxDTO submitProcessDTO);
}

package com.asiainfo.biapp.pec.approve.jx.event;

import com.asiainfo.biapp.pec.approve.jx.dto.CmpApproveProcessJxRecord;

/**
 * description: 江西审批事件节点触发接口
 *
 * @author: lvchaochao
 * @date: 2023/8/1
 */
public interface EventInterfaceJx {

    void invoke(CmpApproveProcessJxRecord record);
}

package com.asiainfo.biapp.pec.approve.jx.dto;

import com.asiainfo.biapp.pec.approve.jx.vo.ApproveInfo;
import com.asiainfo.biapp.pec.approve.jx.vo.ApproveUserTaskH5Bo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by ranpf on 2023/5/25.
 */
@Data
@ApiModel("H5查询活动待审批入参")
public class MobileActionForm {
    //数据总共分几页
    String pageSize;
    //每页显示数量
    String pageNum;
    //用于判断活动保存或者提交审批（值为save为保存，commit为提交审批）
    private String flag;

    //策略 ID
    String campsegId;
    //策略名称
    private String campsegName;

    private String systemId;

    private String keyword;

    private String channelId;

    private String approveStatus;

    private String userId;

    private String channelIds;

    private String instanceId;

    private String marketingType;

    private String callType;

    private String whPosition;

    private String campsegPid;

    private String endDate;

    private String keyWord;

    private ApproveInfo approveInfo;

    private String serviceName;

    private List<Map<String, Object>> assignNodesApprover;

    private ApproveUserTaskH5Bo task;

    private String materialId;

    private String fileName;
    //活动预警唯一识别码
    private String uniqueIdentifierId;
    //活动父ID
    private String campsegPId;
    //活动预警创建人
    private String creater;

    private String serviceType;
    private String nodeId;


    public String getCampsegPId() {
        return campsegPId;
    }

    public void setCampsegPId(String campsegPId) {
        this.campsegPId = campsegPId;
    }

    public String getCampsegPid() {
        return campsegPid;
    }

    public void setCampsegPid(String campsegPid) {
        this.campsegPid = campsegPid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

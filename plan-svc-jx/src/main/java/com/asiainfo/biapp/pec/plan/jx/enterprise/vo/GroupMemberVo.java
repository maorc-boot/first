package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.List;

/**
 * @author mamp
 * @date 2022/7/30
 */
public class GroupMemberVo {
    /**
     * 集团编码
     */
    private String groupId;

    /**
     * 集团成员集合
     */
    private List<String> memberId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberId() {
        return memberId;
    }

    public void setMemberId(List<String> memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GroupMemberVo{");
        sb.append("groupId='").append(groupId).append('\'');
        sb.append(", memberId=").append(memberId);
        sb.append('}');
        return sb.toString();
    }
}

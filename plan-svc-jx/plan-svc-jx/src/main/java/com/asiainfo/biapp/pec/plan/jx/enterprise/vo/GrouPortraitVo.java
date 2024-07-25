package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

/**
 * @author mamp
 * @date 2022/7/30
 */
public class GrouPortraitVo {
    /**
     * 集团编码
     */
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GrouPortraitVo{");
        sb.append("groupId='").append(groupId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

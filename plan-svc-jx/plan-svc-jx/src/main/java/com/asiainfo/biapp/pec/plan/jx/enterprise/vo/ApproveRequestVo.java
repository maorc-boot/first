package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

/**
 * @author mamp
 * @date 2022/7/30
 */
public class ApproveRequestVo {
    /**
     * 渠道ID
     */
    private String channelId;
    /**
     * 运营位ID
     */
    private String position;
    /**
     * 用户名
     */
    private String userName;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ApproveRequestVo{");
        sb.append("channelId='").append(channelId).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

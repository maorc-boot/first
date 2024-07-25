package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

/**
 * 外呼权限管理参数实体类
 */
public class ScenarioAllVo {
    //当前页
    private String pageNum;
    //每页个数
    private String pageSize;
    //地市
    private String cityNameAll;
    //区县
    private String couniyNameAll;
    //渠道编码
    private String channelIdAll;
    //渠道名称
    private String channelNameAll;
    //经理工号
    private String managerNumberAll;
    //外显号码
    private String phoneAll;
    //外呼权限
    private String permissionsAll;

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCityNameAll() {
        return cityNameAll;
    }

    public void setCityNameAll(String cityNameAll) {
        this.cityNameAll = cityNameAll;
    }

    public String getCouniyNameAll() {
        return couniyNameAll;
    }

    public void setCouniyNameAll(String couniyNameAll) {
        this.couniyNameAll = couniyNameAll;
    }

    public String getChannelIdAll() {
        return channelIdAll;
    }

    public void setChannelIdAll(String channelIdAll) {
        this.channelIdAll = channelIdAll;
    }

    public String getChannelNameAll() {
        return channelNameAll;
    }

    public void setChannelNameAll(String channelNameAll) {
        this.channelNameAll = channelNameAll;
    }

    public String getManagerNumberAll() {
        return managerNumberAll;
    }

    public void setManagerNumberAll(String managerNumberAll) {
        this.managerNumberAll = managerNumberAll;
    }

    public String getPhoneAll() {
        return phoneAll;
    }

    public void setPhoneAll(String phoneAll) {
        this.phoneAll = phoneAll;
    }

    public String getPermissionsAll() {
        return permissionsAll;
    }

    public void setPermissionsAll(String permissionsAll) {
        this.permissionsAll = permissionsAll;
    }

    @Override
    public String toString() {
        return "ScenarioAllVo{" +
                "pageNum='" + pageNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", cityNameAll='" + cityNameAll + '\'' +
                ", couniyNameAll='" + couniyNameAll + '\'' +
                ", channelIdAll='" + channelIdAll + '\'' +
                ", channelNameAll='" + channelNameAll + '\'' +
                ", managerNumberAll='" + managerNumberAll + '\'' +
                ", phoneAll='" + phoneAll + '\'' +
                ", permissionsAll='" + permissionsAll + '\'' +
                '}';
    }

    public ScenarioAllVo(String pageNum, String pageSize, String cityNameAll, String couniyNameAll, String channelIdAll, String channelNameAll, String managerNumberAll, String phoneAll, String permissionsAll) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.cityNameAll = cityNameAll;
        this.couniyNameAll = couniyNameAll;
        this.channelIdAll = channelIdAll;
        this.channelNameAll = channelNameAll;
        this.managerNumberAll = managerNumberAll;
        this.phoneAll = phoneAll;
        this.permissionsAll = permissionsAll;
    }

    public ScenarioAllVo() {
    }
}

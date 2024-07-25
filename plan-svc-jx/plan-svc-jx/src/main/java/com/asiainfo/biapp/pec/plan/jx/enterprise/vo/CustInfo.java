package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.Date;

public class CustInfo {
    protected String customGroupId;//	客户群ID
    protected String customGroupName;//客户群名称
    protected String customGroupDesc;//客户群描述
    protected String createUserId;//创建人ID
    protected Date createtime;//创建时间
    protected String ruleDesc;//客户群规则描述
    protected String customSourceId;//客户群来源
    protected Integer customNum;//客户群数量
    protected Integer customStatusId;//客户群状态
    protected Date effectiveTime;//生效时间
    protected Date failTime;//失效时间
    protected Integer updateCycle;//客户群生成周期:1,一次性;2,月周期;3,日周期
    protected Integer isPushOther;
    protected String createUserName;  // 客户群创建人名称
    protected String cityId;//地市id
    protected String cityName;//地市名称
    protected String deptId;//部门id
    protected String deptName;//部门名称
    protected String cocCustFileName;//coc推送过来的文件名字
    protected String createType;//coc推送过来客户群创建规则

    private Integer actualCustomNum;//客户群实际数量

    protected String userCampType;//coc推送过来客户群创建规则
    protected String groupListType;//coc推送过来客户群创建规则

    public Integer getActualCustomNum() {
        return actualCustomNum;
    }

    public void setActualCustomNum(Integer actualCustomNum) {
        this.actualCustomNum = actualCustomNum;
    }

    public String getUserCampType() {
        return userCampType;
    }

    public void setUserCampType(String userCampType) {
        this.userCampType = userCampType;
    }

    public String getGroupListType() {
        return groupListType;
    }

    public void setGroupListType(String groupListType) {
        this.groupListType = groupListType;
    }

    /**
     * 场景ID,多个用","分隔
     */
    protected String sceneIds;
    /**
     * 场景名称,多个用","分隔
     */
    protected String sceneNames;

    // 是否使用智能推荐生成客群 0 原始操作生成的客群 1 使用智能推荐后，抽取数据第一次过滤生成的客群 2 在第一的基础上，过滤第二次生成的客群，不在页面展示
    protected String isIntelRec;

    public String getIsIntelRec() {
        return isIntelRec;
    }

    public void setIsIntelRec(String isIntelRec) {
        this.isIntelRec = isIntelRec;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCustomGroupId() {
        return customGroupId;
    }

    public void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    public String getCustomGroupName() {
        return customGroupName;
    }

    public void setCustomGroupName(String customGroupName) {
        this.customGroupName = customGroupName;
    }

    public String getCustomGroupDesc() {
        return customGroupDesc;
    }

    public void setCustomGroupDesc(String customGroupDesc) {
        this.customGroupDesc = customGroupDesc;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public String getCustomSourceId() {
        return customSourceId;
    }

    public void setCustomSourceId(String customSourceId) {
        this.customSourceId = customSourceId;
    }

    public Integer getCustomNum() {
        return customNum;
    }

    public void setCustomNum(Integer customNum) {
        this.customNum = customNum;
    }

    public Integer getCustomStatusId() {
        return customStatusId;
    }

    public void setCustomStatusId(Integer customStatusId) {
        this.customStatusId = customStatusId;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getFailTime() {
        return failTime;
    }

    public void setFailTime(Date failTime) {
        this.failTime = failTime;
    }

    public Integer getUpdateCycle() {
        return updateCycle;
    }

    public void setUpdateCycle(Integer updateCycle) {
        this.updateCycle = updateCycle;
    }

    public Integer getIsPushOther() {
        return isPushOther;
    }

    public void setIsPushOther(Integer isPushOther) {
        this.isPushOther = isPushOther;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCocCustFileName() {
        return cocCustFileName;
    }

    public void setCocCustFileName(String cocCustFileName) {
        this.cocCustFileName = cocCustFileName;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getSceneIds() {
        return sceneIds;
    }

    public void setSceneIds(String sceneIds) {
        this.sceneIds = sceneIds;
    }

    public String getSceneNames() {
        return sceneNames;
    }

    public void setSceneNames(String sceneNames) {
        this.sceneNames = sceneNames;
    }

}

package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;


import java.util.Map;

/**
 * @author ranpf
 * @title: GridRequestParameters
 * @date 2022/8/1 14:14
 */
public class GridRequestParameters {

    /** 活动名称*/
    private String subitemName;

    /** 策略开始时间 */
    private String startTime;
    /** 策略结束时间 */
    private String endTime;
    /** 策略创建人ID */
    private String createorId;
    /** 策略创建人名称 */
    private String createUserName;
    /** 月周期 */
    private String dayMask;
    /** 周执行周期 */
    private String weekMask;
    /** 天周期 */
    private String minutesMask;
    /** 营销用语 */
    private String execText;
    /** 审批人ID */
    private String approveId;
    /** 策略类型 */
    private String businessClass;
    /**活动目的*/
    private String subitemTarget;
    /** 策略名称 */
    private String campsegName;

    /**小区ID*/
    private String plotId;

    /**小区名称*/
    private String plotName;

    /** 渠道编码  */
    private String channelId;

    /**当活动性质为3时默认运营位为0 ,营销短信;1和2时默认是4 政企营销运营*/
    private String adivId;
    /**
     * 任务编码
     */
    private String taskId;

    /**1.短信预热活动
     2.营销方案活动
    3. 网格小区预热短信活动*/
    private String subitemNature;

    /**客户群ID*/
    private String customerId;
    /**产品ID*/
    private String planId;

    /**客群名称*/
    private String customerName;
    /**类型*/
    private String marketingType;
    /***/
    private String createTime;
    private String createCityId;
    private String createDeptId;
    private String frequency;
    private String testPhones;
    private String botherAvoidCust;
    private String botherAvoidName;

    private String approveInfo;
    private String boothId;//场景
    private String materialId;//素材

    private String materialName;

    private String campTypeId;//策略类型
    private String campPreview;//策略预演 1是, 0否

    private String optionSign;  //活动标志 0 是普通, 8 政企
    private String createType;  //活动标志 0 是普通,1,导入, 8 政企
    private String pushType;  //推送标志 0 是偏好,1全量推送
    private String optType;  //操作类型 0 保存,1 修改,2 审批通过
    private String campsegId;  //活动编码

    private Map<String,Object> channelExt;  //渠道扩展信息
    /** 渠道名称  */
    private String channelName;
    /** 事件ID  */
    private String cepEventId;
    /** 事件名称  */
    private String cepEventName;

    public Map<String, Object> getChannelExt() {
        return channelExt;
    }

    public void setChannelExt(Map<String, Object> channelExt) {
        this.channelExt = channelExt;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCepEventId() {
        return cepEventId;
    }

    public void setCepEventId(String cepEventId) {
        this.cepEventId = cepEventId;
    }

    public String getCepEventName() {
        return cepEventName;
    }

    public void setCepEventName(String cepEventName) {
        this.cepEventName = cepEventName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCampsegId() {
        return campsegId;
    }

    public void setCampsegId(String campsegId) {
        this.campsegId = campsegId;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getCampPreview() {
        return campPreview;
    }

    public void setCampPreview(String campPreview) {
        this.campPreview = campPreview;
    }

    public String getCampTypeId() {
        return campTypeId;
    }

    public void setCampTypeId(String campTypeId) {
        this.campTypeId = campTypeId;
    }

    public String getOptionSign() {
        return optionSign;
    }

    public void setOptionSign(String optionSign) {
        this.optionSign = optionSign;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getBotherAvoidName() {
        return botherAvoidName;
    }

    public void setBotherAvoidName(String botherAvoidName) {
        this.botherAvoidName = botherAvoidName;
    }

    public String getBoothId() {
        return boothId;
    }

    public void setBoothId(String boothId) {
        this.boothId = boothId;
    }

    public String getApproveInfo() {
        return approveInfo;
    }

    public void setApproveInfo(String approveInfo) {
        this.approveInfo = approveInfo;
    }

    public String getTestPhones() {
        return testPhones;
    }

    public void setTestPhones(String testPhones) {
        this.testPhones = testPhones;
    }

    public String getBotherAvoidCust() {
        return botherAvoidCust;
    }

    public void setBotherAvoidCust(String botherAvoidCust) {
        this.botherAvoidCust = botherAvoidCust;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getCreateCityId() {
        return createCityId;
    }

    public void setCreateCityId(String createCityId) {
        this.createCityId = createCityId;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMarketingType() {
        return marketingType;
    }

    public void setMarketingType(String marketingType) {
        this.marketingType = marketingType;
    }

    public String getSubitemName() {
        return subitemName;
    }

    public void setSubitemName(String subitemName) {
        this.subitemName = subitemName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateorId() {
        return createorId;
    }

    public void setCreateorId(String createorId) {
        this.createorId = createorId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getDayMask() {
        return dayMask;
    }

    public void setDayMask(String dayMask) {
        this.dayMask = dayMask;
    }

    public String getWeekMask() {
        return weekMask;
    }

    public void setWeekMask(String weekMask) {
        this.weekMask = weekMask;
    }

    public String getMinutesMask() {
        return minutesMask;
    }

    public void setMinutesMask(String minutesMask) {
        this.minutesMask = minutesMask;
    }

    public String getExecText() {
        return execText;
    }

    public void setExecText(String execText) {
        this.execText = execText;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(String businessClass) {
        this.businessClass = businessClass;
    }

    public String getSubitemTarget() {
        return subitemTarget;
    }

    public void setSubitemTarget(String subitemTarget) {
        this.subitemTarget = subitemTarget;
    }

    public String getCampsegName() {
        return campsegName;
    }

    public void setCampsegName(String campsegName) {
        this.campsegName = campsegName;
    }

    public String getPlotId() {
        return plotId;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAdivId() {
        return adivId;
    }

    public void setAdivId(String adivId) {
        this.adivId = adivId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSubitemNature() {
        return subitemNature;
    }

    public void setSubitemNature(String subitemNature) {
        this.subitemNature = subitemNature;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

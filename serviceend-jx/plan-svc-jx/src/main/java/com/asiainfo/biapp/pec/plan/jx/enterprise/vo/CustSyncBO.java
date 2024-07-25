package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.CustInfo;

import java.util.List;

public class CustSyncBO extends CustInfo {

    /**
     * 任务ID,push_log表中的 GP_ID
     */
    private String taskId;
    /**
     * 客户群清单最新数据日期
     */
    private String dataDate;
    /**
     * 推送至用户ID，多个用","分隔
     */
    private String pushToUserIds;
    /**
     * 清单和校验文件名称模板:MCD_GROUP_客户群ID_数据日期
     */
    private String fileNameFormat = "MCD_GROUP_%s_%s";

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 清单表列名
     */
    private List<String> columnNameList;
    /**
     * 清单表列类型
     */
    private List<String> columnTypeList;
    /**
     * 清单表名
     */
    private String cuserTable;
    /**
     * 清单文件和校验文件本地存入路径
     */
    private String localPath;
    /**
     * 异常消息
     */
    private String errorMsg;
    /**
     * 数据同步状态，mcd_custgroup_tab_list 表中的 DATA_STATUS
     */
    private int dataStatus;
    /**
     * mcd_custgroup_push_log 中的 DATA_STATUS
     */
    private int taskStatus;

    /**
     * 是否手工推送: 1-是，0-否
     */
    private String isManualPush;

    /**
     * 节点取模后的编号
     */
    private String modValue;




    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDataDate() {
        return dataDate;
    }


    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getPushToUserIds() {
        return pushToUserIds;
    }

    public void setPushToUserIds(String pushToUserIds) {
        this.pushToUserIds = pushToUserIds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public void setColumnNameList(List<String> columnNameList) {
        this.columnNameList = columnNameList;
    }

    public List<String> getColumnTypeList() {
        return columnTypeList;
    }

    public void setColumnTypeList(List<String> columnTypeList) {
        this.columnTypeList = columnTypeList;
    }

    public String getCuserTable() {
        return cuserTable;
    }

    public void setCuserTable(String cuserTable) {
        this.cuserTable = cuserTable;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getFileNameFormat() {
        return fileNameFormat;
    }

    public void setFileNameFormat(String fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getIsManualPush() {
        return isManualPush;
    }

    public void setIsManualPush(String isManualPush) {
        this.isManualPush = isManualPush;
    }

    public String getModValue() {
        return modValue;
    }

    public void setModValue(String modValue) {
        this.modValue = modValue;
    }
}

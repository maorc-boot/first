package com.asiainfo.biapp.pec.eval.jx.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "智慧网格评估请求参数")
public class SmartGridEvalReq {
    @ApiModelProperty(value = "地市ID：具体地市，-1-明细，0-汇总")
    private String cityId;
    @ApiModelProperty("区县ID：具体区县，-1-明细，0-汇总")
    private String countyId;
    @ApiModelProperty("网格ID：具体网格，-1-明细，0-汇总")
    private String gridId;
    @ApiModelProperty("视图类型: M-月视图, W-周视图, D-日视图")
    private String viewType;
    @ApiModelProperty(value = "活动搜索关键字")
    private String keyWordsCamp;
    @ApiModelProperty(value = "产品搜索关键字")
    private String keyWordsPlan;
    @ApiModelProperty(value = "接触类型，0-线下，1-线上")
    private String contactType;
    @ApiModelProperty(value = "接触方式，0-电话，1-短信")
    private String contactMode;
    @ApiModelProperty("数据周期开始时间,日视图:yyyyMMdd,月试图:yyyyMM")
    private String startDate;
    @ApiModelProperty("数据周期结束时间,日视图:yyyyMMdd,月试图:yyyyMM")
    private String endDate;
    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getKeyWordsCamp() {
        return keyWordsCamp;
    }

    public void setKeyWordsCamp(String keyWordsCamp) {
        this.keyWordsCamp = keyWordsCamp;
    }

    public String getKeyWordsPlan() {
        return keyWordsPlan;
    }

    public void setKeyWordsPlan(String keyWordsPlan) {
        this.keyWordsPlan = keyWordsPlan;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactMode() {
        return contactMode;
    }

    public void setContactMode(String contactMode) {
        this.contactMode = contactMode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}

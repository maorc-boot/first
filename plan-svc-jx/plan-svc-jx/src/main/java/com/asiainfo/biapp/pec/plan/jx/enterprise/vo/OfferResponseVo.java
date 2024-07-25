package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.List;

/**
 * @author mamp
 * @date 2022/7/30
 */
public class OfferResponseVo {
    /**
     * 结果编码
     */
    private String resultCode;
    /**
     * 结果描述
     */
    private String resultInfo;

    /**
     * 当前页码
     */
    private int pageIndex;
    /**
     * 每大小
     */
    private int pageSize;
    /**
     * 记录总数
     */
    private int totalCount;
    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 数据集合
     */
    private List<OfferVo> data;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<OfferVo> getData() {
        return data;
    }

    public void setData(List<OfferVo> data) {
        this.data = data;
    }
}

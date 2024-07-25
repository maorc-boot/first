package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

/**
 * @author mamp
 * @date 2022/7/30
 */
public class OfferRequestVo {
    /**
     * 产品编码
     */
    private String offerCode;
    /**
     * 产品名称
     */
    private String offerName;
    /**
     * 当前页
     */
    private int pageIndex = 1;
    /**
     * 每页大小
     */
    private int pageSize = 10;

    public OfferRequestVo(String offerCode, String offerName, int pageIndex, int pageSize) {
        this.offerCode = offerCode;
        this.offerName = offerName;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public OfferRequestVo() {
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OfferRequestVo{");
        sb.append("offerCode='").append(offerCode).append('\'');
        sb.append(", offerName='").append(offerName).append('\'');
        sb.append(", pageIndex=").append(pageIndex);
        sb.append(", pageSize=").append(pageSize);
        sb.append('}');
        return sb.toString();
    }
}

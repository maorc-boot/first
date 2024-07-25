package com.asiainfo.biapp.pec.approve.jx.enterprise.vo;



public class MarketResultVo {


    private String resultCode;


    private String resultInfo ;


    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MarketResultVo{" +
                "resultCode='" + resultCode + '\'' +
                ", resultInfo='" + resultInfo + '\'' +
                ", data=" + data +
                '}';
    }
}

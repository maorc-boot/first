package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;


public class Column {
    /**
     * 标签名称
     */
    private String columnName;
    /**
     * 标签类型名称
     */
    private String columnCnName;
    /**
     * 标签类型，文本，枚举等
     */
    private String columnDataType;
    /**
     * 标签字符最大长度
     */
    private String columnLength;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnCnName() {
        return columnCnName;
    }

    public void setColumnCnName(String columnCnName) {
        this.columnCnName = columnCnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Column{");
        sb.append("columnName='").append(columnName).append('\'');
        sb.append(", columnCnName='").append(columnCnName).append('\'');
        sb.append(", columnDataType='").append(columnDataType).append('\'');
        sb.append(", columnLength='").append(columnLength).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

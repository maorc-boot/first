package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.List;

public class Columns {

    private List<Column> column;

    public List<Column> getColumn() {
        return column;
    }

    public void setColumn(List<Column> column) {
        this.column = column;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Columns{");
        sb.append("column=").append(column);
        sb.append('}');
        return sb.toString();
    }
}

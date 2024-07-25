package com.asiainfo.biapp.pec.eval.jx.mybatis;

import org.apache.ibatis.type.DoubleTypeHandler;

import java.text.DecimalFormat;

/**
 * @author mamp
 * @date 2022/12/26
 */
public class DoubelFormatHandler extends DoubleTypeHandler {

    /**
     * Float 保留两位小数
     * @return
     */
    public static Double keepTwoDecimalFloat(Double f) {
        DecimalFormat decimalFormat=new DecimalFormat(".0000");
        return Double.parseDouble(decimalFormat.format(f));
    }
}

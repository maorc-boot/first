package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils
 * @className: Export
 * @author: chenlin
 * @description: excel文件导出工具
 * @date: 2023/6/20 10:35
 * @version: 1.0
 */
@Slf4j
public class ExportUtil {

    /*
     * @param fileName:导出的文件名
     * @param addTimestamp:是否添加时间戳
     * @param records: 泛型为map的集合
     * @return void:
     * @author chenlin
     * @description 将map对象导出为excel.xlsx文件
     * @date 2023/6/20 11:15
     */
    @SneakyThrows
    public static void mapToXlsx(String fileName, boolean addTimestamp, List<Map<String, Object>> records) {

        if (StringUtils.isBlank(fileName)) throw new BaseException("导出的文件名不可为空！");

        //设置时间戳
        String timestamp = "";
        if (addTimestamp) timestamp = "_" + new SimpleDateFormat("yyyyMMdd").format(new Date());

        //如果以.xls*结尾，只取.xls*的前部分文件名
        Matcher matcher = Pattern.compile("(.+)(\\.xls[x]?)$").matcher(fileName.toLowerCase());
        fileName = matcher.find() ? matcher.group(1) + timestamp + ".xlsx" : fileName + timestamp + ".xlsx";

        //将ascii码之外的内容进行转换，否则下载的文件名会出现异常
        fileName = URLUtil.encode(fileName);

        //将数据写入到writer
        ExcelWriter writer = ExcelUtil.getWriter(true).write(records, true);

        //为日期数据设置单元格格式，日期格式要在写入后才能设置，原因：未知
        short format = writer.getWorkbook().createDataFormat().getFormat("yyyy/mm/dd hh:mm");
        writer.getStyleSet().getCellStyleForDate().setDataFormat(format);

        //静态获取HttpServletResponse并设置响应头
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        //写出
        writer.flush(resp.getOutputStream(), true).close();
        resp.getOutputStream().close();
        log.info("文件名：{}导出成功！", URLUtil.decode(fileName));
    }

}

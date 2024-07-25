package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.common.Constant;
import com.asiainfo.biapp.pec.plan.jx.hmh5.contant.McdCallExeclContant;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.IMcdCallDetailsMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCallOutRecordingDetail;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdOutCallQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdIdentifierQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdBasiTermService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.IMcdCallDetailsService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdCallPermissConfVo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.McdOutDetailsVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class McdCallDetailsServiceImpl extends ServiceImpl<IMcdCallDetailsMapper, McdCallOutRecordingDetail> implements IMcdCallDetailsService {

    @Autowired
    private IMcdCallDetailsMapper callDetailsMapper;

    @Autowired
    private IMcdBasiTermService mcdBasiTermService;

    @Override
    public IPage queryCallDetailsList(McdOutCallQuery query, UserSimpleInfo user) {
        IPage<McdOutDetailsVo> page = new Page(query.getPageNum(),query.getPageSize());
        List<McdCallPermissConfVo> callLogPermissionConf = mcdBasiTermService.getCallLogPermissionConf(user.getUserId());
        if(CollectionUtil.isNotEmpty(callLogPermissionConf) && StrUtil.isEmpty(callLogPermissionConf.get(0).getPermission())) return page;
        return callDetailsMapper.queryCallDetailsList(page, query);
    }

    @Override
    public String exportExcel(McdOutCallQuery query,UserSimpleInfo user, HttpServletResponse response) {
        IPage<McdOutDetailsVo> resultList = new Page<>();
        final Workbook book = WorkbookUtil.createBook(true);
        String fileName = "外呼通话明细.xls";
        List<McdCallPermissConfVo> callLogPermissionConf = mcdBasiTermService.getCallLogPermissionConf(user.getUserId());
        if(CollectionUtil.isNotEmpty(callLogPermissionConf) && StrUtil.isNotEmpty(callLogPermissionConf.get(0).getPermission())) {
            IPage<McdOutDetailsVo> page = new Page(query.getPageNum(),query.getPageSize());
            resultList = callDetailsMapper.queryCallDetailsList(page, query);
        }

        Sheet sheet = book.createSheet("Sheet");
        Row row = sheet.createRow(Constant.SpecialNumber.ZERO_NUMBER);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        String[] excelHeader = {"业务场景","地市", "区县","网格编码","网格", "渠道编码", "渠道名称", "经理工号", "经理姓名", "唯一标识",
                "用户ID", "外显号码", "开始时间", "结束时间", "通话时长", "外呼结果", "被叫号码", "挂断类型", "录音文件"};

        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 3;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }

        if(CollectionUtil.isNotEmpty(resultList.getRecords())) {
            int count = 0;
            for (McdOutDetailsVo record : resultList.getRecords()) {
                count++;

                row = sheet.createRow(count);
                // 业务场景
                row.createCell(Constant.SpecialNumber.ZERO_NUMBER).setCellValue(record.getScenarioName());

                // 地市
                row.createCell(Constant.SpecialNumber.ONE_NUMBER).setCellValue(record.getCity());

                // 区县
                row.createCell(Constant.SpecialNumber.TWO_NUMBER).setCellValue(record.getCounty());

                // 网格编码
                row.createCell(Constant.SpecialNumber.THREE_NUMBER).setCellValue(record.getGridId());

                // 网格名称
                row.createCell(Constant.SpecialNumber.FOUR_NUMBER).setCellValue(record.getGridName());

                // 渠道编码
                row.createCell(Constant.SpecialNumber.FIVE_NUMBER).setCellValue(record.getChannelId());

                // 渠道名称
                row.createCell(Constant.SpecialNumber.SIX_NUMBER).setCellValue(record.getChannelName());

                // 经理工号
                row.createCell(McdCallExeclContant.SEVEN_NUMBER).setCellValue(record.getManagerid());

                // 经理姓名
                row.createCell(McdCallExeclContant.ENIGHT_NUMBER).setCellValue(record.getManagerName());

                // 唯一标识
                row.createCell(McdCallExeclContant.NINE_NUMBER).setCellValue(record.getOnlyId());

                // 用户ID
                row.createCell(McdCallExeclContant.TEN_NUMBER).setCellValue(record.getUserId());

                // 外显号码
                row.createCell(McdCallExeclContant.ELEVEN_NUMBER).setCellValue(record.getDisplay());

                // 开始时间
                row.createCell(McdCallExeclContant.TWELVE_NUMBER).setCellValue(record.getStartTime());

                // 结束时间
                row.createCell(McdCallExeclContant.THIRTEEN_NUMBER).setCellValue(record.getEndTime());

                // 通话时长
                row.createCell(McdCallExeclContant.FOURTEEN_NUMBER).setCellValue(record.getDuration());

                // 外呼结果
                row.createCell(McdCallExeclContant.FIFITEEN_NUMBER).setCellValue(record.getResult());

                // 被叫号码
                row.createCell(McdCallExeclContant.SIXTEEN_NUMBER).setCellValue(record.getCallee());

                // 挂断类型
                row.createCell(McdCallExeclContant.SEVENTEEN_NUMBER).setCellValue(record.getEndType());

                // 录音文件
                row.createCell(McdCallExeclContant.EIGHTEEN_NUMBER).setCellValue(record.getUrl());
            }
        }
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            book.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("策略列表导出失败", e);
            return "策略列表导出失败！";
        }
        return "策略列表导出成功！";
    }

    @Override
    public List<McdOutDetailsVo> callHttpData(McdIdentifierQuery query) {
        List<McdOutDetailsVo> callList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("callIdentifier", query.getCallQueryId());
        log.info("发送到URL的报文为：" + JSONUtil.toJsonStr(jsonObject));
        try {
//            String getUrl = callDetailsService.getUrl();
//            HttpSession session = request.getSession();
//            session.setAttribute("callurl", getUrl);
//            URL url = new URL("http://10.19.29.32:8081/hmh5/api/thirdpartycall/query"); //url地址
            String getUrl = callDetailsMapper.getUrl();
            URL url = new URL(getUrl); //url地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();
            try (OutputStream os = connection.getOutputStream()) {
                os.write(query.getCallQueryId().getBytes("UTF-8"));
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                log.info("返回来的报文："+sbf.toString());
                String resp = sbf.toString();
                JSONObject json = JSONObject.fromObject(resp);
                List<Map<String, Object>> list = json.getJSONArray("dataInfo");
                for (Map<String, Object> map : list) {
                    McdOutDetailsVo call = new McdOutDetailsVo();
                    call.setAppKey(MapUtil.getStr(map,"appKey",""));
                    call.setOnlyId(MapUtil.getStr(map,"callIdentifier",""));
                    call.setCaller(MapUtil.getStr(map,"caller",""));
                    call.setCallee(MapUtil.getStr(map,"callee",""));
                    call.setDisplay(MapUtil.getStr(map,"display",""));
                    call.setStartTime(MapUtil.getStr(map,"startTime",""));
                    call.setEndTime(MapUtil.getStr(map,"endTime",""));
                    call.setResult(MapUtil.getStr(map,"callResult",""));
                    call.setUrl(MapUtil.getStr(map,"audioUrl",""));
                    call.setAlertDuration(MapUtil.getStr(map,"alertDuration",""));
                    call.setDuration(MapUtil.getStr(map,"talkDuration",""));
                    call.setEndType(MapUtil.getStr(map,"endType",""));
                    callList.add(call);
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            log.error("外呼明细查询返回结果处理异常, 异常信息为：{}", e);
        }
        return callList;
    }
}

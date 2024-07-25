package com.asiainfo.biapp.pec.plan.jx.dna.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.constant.RedisDicKey;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.RestFullUtil;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup.*;
import com.asiainfo.biapp.pec.plan.jx.dna.constant.ConstantDNA;
import com.asiainfo.biapp.pec.plan.jx.dna.service.IDNACustomGroupService;
import com.asiainfo.biapp.pec.plan.util.MD5Util;
import com.asiainfo.biapp.pec.plan.vo.req.CustomActionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * description: dna客群信息获取service实现
 *
 * @author: lvchaochao
 * @date: 2023/12/12
 */
@Service
@Slf4j
public class DNACustomGroupServiceImpl implements IDNACustomGroupService {

    /**
     * 客群详情请求路径
     */
    @Value("${dna.custGroup.detailUrl}")
    private String detailServiceUrl;

    /**
     * 客群列表请求路径
     */
    @Value("${dna.custGroup.customUrl}")
    private String customServiceUrl;

    /**
     * 客群清单文件获取路径
     */
    @Value("${dna.custGroup.customerCalUrl}")
    private String customerCalUrl;

    /**
     * 标签信息获取路径
     */
    @Value("${dna.custGroup.variableReplaceUrl}")
    private String variableReplaceUrl;

    @Value("${dna.custGroup.localPath:D:\\}")
    private String localPath;

    @Value("${dna.custGroup.sftp.host:10.19.92.21}")
    private String host;

    @Value("${dna.custGroup.sftp.port:22022}")
    private int port;

    @Value("${dna.custGroup.sftp.username:work}")
    private String username;

    @Value("${dna.custGroup.sftp.password:ffdf4BE2tUdC8lQgO%d}")
    private String password;

    @Value("${dna.custGroup.sftp.encoding:utf-8}")
    private String encoding;

    /**
     * 江西获取dna客群列表
     *
     * @param req 入参信息
     * @return {@link IPage}<{@link CustgroupDetailVO}>
     */
    @Override
    public IPage<CustgroupDetailVO> getMoreMyCustom(CustomActionQuery req) {
        List<CustgroupDetailVO> list = new ArrayList<>();
        Page<CustgroupDetailVO> pager = new Page<>(req.getCurrent(), req.getSize());
        pager.setOrders(Collections.emptyList());
        pager.setOptimizeCountSql(true);
        pager.setHitCount(false);
        pager.setCountId(null);
        pager.setMaxLimit(null);
        pager.setSearchCount(true);
        String digest = null;
        CustomerRespDataVO resData = null;
        Map<String, Object> map = new HashMap<>();
        map.put("keyWord", req.getKeyWords());
        map.put("requestNo", UUID.randomUUID().toString());
        map.put("userId", req.getUserId());
        map.put("version", "1.0");
        map.put("pageSize", req.getSize());
        map.put("pageIndex", req.getCurrent());
        map.put("startTime", null);
        map.put("endTime", null);
        try {
            // 1. 入参加密
            digest = Base64.encodeBase64String(MD5Util.encode(String.valueOf(map)).getBytes());
        } catch (Exception e) {
            log.error("客户群数据请求报文加密失败！");
            return new Page<>();
        }
        map.put("sign", digest);
        try {
            // 2. 请求dna接口
            log.info("客户群数据发送DNA请求路径url：{},返回类型：String类型,参数：{}", customServiceUrl, JSONUtil.parseObj(map, false).toString());
            String resStr = RestFullUtil.getInstance().sendPost2(customServiceUrl, JSONUtil.parseObj(map, false).toString());
            log.debug("客户群数据发送DNA响应数据：{}", resStr);
            resData = JSONUtil.toBean(resStr, CustomerRespDataVO.class);
            log.info("客户群数据DNA响应封装后数据：{}", resData);
            if (!resData.getCode().equals("9000")) return new Page<>();
            List<CustomerDefVO> customerList = resData.getData().getCustomerList();
            // 3. 根据dna返回的数据构造客群对象返回信息
            buildCustomReturnObj(list, customerList);
        } catch (Exception ex) {
            log.error("客户群列表数据请求失败！");
        }
        pager.setRecords(list);
        pager.setTotal(resData.getData().getTotal());
        pager.setPages(resData.getData().getTotalPages());
        return pager;
    }

    /**
     * 调用DNA方提供接口获取客户群数据
     *
     * @param custgroupId 客户群id
     * @return CustgroupDetailVO
     */
    @Override
    public CustgroupDetailVO detailCustgroup(String custgroupId) {
        CustgroupDetailVO custgroupDetailVO = new CustgroupDetailVO();
        String digest = null;
        CustomerDetailRespDataVO resData = null;
        Map<String, Object> map = new HashMap<>();
        map.put("requestNo", UUID.randomUUID().toString());
        map.put("version", "1.0");
        map.put("pageSize", 12);
        map.put("pageIndex", 1);
        map.put("customerId", custgroupId);
        try {
            digest = Base64.encodeBase64String(MD5Util.encode(String.valueOf(map)).getBytes());
        } catch (Exception e) {
            log.error("客户群详情请求报文加密失败！");
            return custgroupDetailVO;
        }
        map.put("sign", digest);
        log.info("客户群详情发送DNA请求路径url：{},返回类型：String类型,参数：{}", detailServiceUrl, JSONUtil.parseObj(map, false).toString());
        try {
            String resStr = RestFullUtil.getInstance().sendPost2(detailServiceUrl, JSONUtil.parseObj(map, false).toString());
            log.debug("客户群详情DNA响应数据：{}", resStr);
            resData = JSONUtil.toBean(resStr, CustomerDetailRespDataVO.class);
            log.info("客户群详情数据DNA响应封装后数据：{}", resData);
        } catch (Exception ex) {
            log.error("客户群详情请求失败！");
        }
        if (!resData.getCode().equals("9000")) return custgroupDetailVO;
        CustomerDetailDataVO data = resData.getData();
        try {
            // 构造客群详情对象
            buildCustomGroupDetail(custgroupDetailVO, data);
        } catch (Exception e) {
            log.error("客户群详情DNA返回数据解析异常！", e);
        }
        return custgroupDetailVO;
    }

    /**
     * 标签变量替换信息
     *
     * @param keyWord 关键字
     * @return JSONObject
     */
    @Override
    public JSONObject getVariableInfo(String keyWord) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keyWord", keyWord);
        String jsonStr = HttpRequest.post(variableReplaceUrl)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(paramMap))
                .execute().body();
        log.info("标签变量替换信息接口请求返回：{}", jsonStr);
        return JSONUtil.parseObj(jsonStr, true);
    }

    /**
     * 下载客户群清单
     *
     * @param custId 客户群编码
     * @return 文件路径
     */
    @Override
    public Map<String, String> dowloadCustFile(String custId) {
        String localPath = RedisUtils.getDicValue(RedisDicKey.CUST_FILE_LOCAL_PATH); // 与coc客群推送本地地址保持一致  否则cgf获取客群bitmap文件会异常
        // DNA客户群实时目标客户清单计算接口请求参数封装
        Map<String, String> res = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("produceList", true);
        param.put("customerIds", new String[]{custId});
        RestFullUtil restUtil = RestFullUtil.getInstance();
        // sftp连接
        ChannelSftp channelSftp = null;
        try {
            String paramJson = JSONUtil.toJsonStr(param);
            log.info("DNA客户群实时目标客户清单计算接口url:{}", customerCalUrl);
            log.info("DNA客户群实时目标客户清单计算接口入参:{}", paramJson);
            // 接口返回结果
            String result = restUtil.sendPost2(customerCalUrl, paramJson);
            log.info("DNA客户群实时目标客户清单计算接口返回结果:{}", result);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            String resultCode = jsonObject.getStr("code");
            if (!"9000".equals(resultCode)) {
                log.warn("DNA客户群实时目标客户清单计算接口结果异常,请求参数:{},返回结果:{}", paramJson, result);
                return null;
            }
            // 清单文件全路径
            String listPath = jsonObject.getJSONObject("data").getStr("listPath");
            // 客群数量
            long count = jsonObject.getJSONObject("data").getLong("count");
            // 客户群清单为空处理
            if (StringUtils.isEmpty(listPath)) {
                log.warn("DNA客户群实时目标客户清单获取路径为空！");
                return null;
            }
            // 远程目录
            String dir = listPath.substring(0, listPath.lastIndexOf("/") + 1);
            // 文件名
            String fileName = listPath.substring(listPath.lastIndexOf("/") + 1);

            SftpUtils utils = new SftpUtils();
            // 连接sftp
            channelSftp = utils.connect(host, port, username, password);
            // 下载文件
            boolean r = utils.download(dir, fileName, localPath, channelSftp);
            if (!r) {
                log.error("下载客户群清单文件失败,custId:{},文件路径:{}, count:{}", custId, listPath, count);
            }
            res.put("listPath", listPath);
            res.put("fileName", fileName);
            res.put("localPath", localPath);
            res.put("encoding", encoding);
            res.put("count", Long.toString(count));
            return res;
            // return localPath + fileName;
        } catch (IOException e) {
            log.error("下载客户群清单异常,custId = {}", custId, e);
        } finally {
            if (null != channelSftp) {
                channelSftp.disconnect();
            }
        }
        return null;
    }

    /**
     * 构造客群详情对象
     *
     * @param custgroupDetailVO 详情返回的对象
     * @param data              接口详情的对象
     */
    private void buildCustomGroupDetail(CustgroupDetailVO custgroupDetailVO, CustomerDetailDataVO data) {
        custgroupDetailVO.setCustomGroupId(data.getId());
        custgroupDetailVO.setCustomGroupName(data.getName());
        custgroupDetailVO.setCustomGroupDesc(data.getDesc());
        custgroupDetailVO.setCustomNum(data.getCount());
        custgroupDetailVO.setActualCustomNum(data.getCount());
        custgroupDetailVO.setCustomStatusId(data.getStatus());
        custgroupDetailVO.setStatusDesc(data.getStatusDesc());
        custgroupDetailVO.setCustomSourceId(ConstantDNA.CUSTOM_SOURCE_DNA); // 数据来源1:coc 2:DNA 3:多波次
        custgroupDetailVO.setCreateUserId(data.getCreateUser());
        custgroupDetailVO.setPushTargetUser(data.getCreateUser());
        custgroupDetailVO.setCreateTime(DateUtil.parse(DateUtil.format(DateUtil.date(data.getCreateTime()), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        custgroupDetailVO.setEffectiveTime(DateUtil.parse(DateUtil.format(DateUtil.date(data.getUpdateTime()), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        custgroupDetailVO.setFailTime(DateUtil.parse(DateUtil.format(DateUtil.date(data.getFailTime()), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
        custgroupDetailVO.setUpdateCycle(data.getUpdateCycle());
        custgroupDetailVO.setUpdateCycleDesc(data.getUpdateCycleDesc());
        custgroupDetailVO.setListTableName(null);
        custgroupDetailVO.setDataDate(Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(data.getDataTime())));
        custgroupDetailVO.setRuleDesc("");
        custgroupDetailVO.setSyncTimes(1);
        custgroupDetailVO.setCustType(0);
        custgroupDetailVO.setIsPushOther(null);
        custgroupDetailVO.setFileName(null);
        custgroupDetailVO.setCreateUserName(data.getCreatorName());
        custgroupDetailVO.setHasCustomAttrs("无");
//            custgroupDetailVO.setCustomAttrs(new ArrayList<>());
    }

    /**
     * 根据dna返回的数据构造客群对象返回信息
     *
     * @param list         返回前端的客群对象信息
     * @param customerList dna返回的客群数据
     */
    private void buildCustomReturnObj(List<CustgroupDetailVO> list, List<CustomerDefVO> customerList) {
        customerList.forEach(v -> {
            try {
                CustgroupDetailVO custgroup = new CustgroupDetailVO();
                custgroup.setCustomGroupId(v.getId());
                custgroup.setCustomGroupName(v.getName());
                custgroup.setCustomGroupDesc(v.getDesc());
                custgroup.setCustomNum(v.getCount());
                custgroup.setActualCustomNum(v.getCount());
                custgroup.setCustomStatusId(v.getStatus());
                custgroup.setCustomSourceId(ConstantDNA.CUSTOM_SOURCE_DNA); // 数据来源1:coc 2:DNA 3:多波次
                custgroup.setCreateUserId(v.getCreateUser());
                custgroup.setPushTargetUser(v.getCreateUser());
                custgroup.setCreateTime(DateUtil.parse(DateUtil.format(DateUtil.date(v.getCreateTime()), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                custgroup.setEffectiveTime(DateUtil.parse(DateUtil.format(DateUtil.date(v.getUpdateTime()), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                custgroup.setFailTime(DateUtil.parse(DateUtil.format(DateUtil.date(v.getFailTime()), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                custgroup.setUpdateCycle(v.getUpdateCycle());
                custgroup.setListTableName(null);
                custgroup.setDataDate(Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(v.getDataTime())));
                custgroup.setRuleDesc("");
                custgroup.setSyncTimes(1);
                custgroup.setCustType(0);
                custgroup.setIsPushOther(null);
                custgroup.setFileName(null);
                custgroup.setCreateUserName(v.getCreatorName());
                custgroup.setHasCustomAttrs("");
                custgroup.setCustomAttrs(new ArrayList<>());
                list.add(custgroup);
            } catch (Exception e) {
                log.error("客户群信息DNA返回数据解析异常！", e);
            }
        });
    }
}

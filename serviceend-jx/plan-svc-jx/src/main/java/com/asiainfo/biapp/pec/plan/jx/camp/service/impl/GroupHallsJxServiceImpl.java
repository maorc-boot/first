package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.iopws.util.SftpFileDown;
import com.asiainfo.biapp.pec.plan.common.RedisDicKey;
import com.asiainfo.biapp.pec.plan.dao.GroupHallsDao;
import com.asiainfo.biapp.pec.plan.dao.McdCampDefDao;
import com.asiainfo.biapp.pec.plan.model.CampInfoVo;
import com.asiainfo.biapp.pec.plan.model.CampsegSubVo;
import com.asiainfo.biapp.pec.plan.model.IopGrouphallsMaterialVo;
import com.asiainfo.biapp.pec.plan.model.IopMobileInfo;
import com.asiainfo.biapp.pec.plan.service.IGroupHallsJxService;
import com.asiainfo.biapp.pec.plan.service.impl.IopMonitorPointcut;
import com.asiainfo.biapp.pec.plan.util.MD5Util;
import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RefreshScope
public class GroupHallsJxServiceImpl implements IGroupHallsJxService {

    private static final Map<String, Object> map = new HashMap<String, Object>();

    @Value("${profile}")
    private String profile;

    @Value("${HallVer.timeout:24}")
    private Long timeout;

    @Value("${HallVer.switch: false}")
    private Boolean hallVerSwitch;

    @Resource
    private McdCampDefDao mcdCampDefDao;

    @Autowired
    private IopMonitorPointcut pointcut;

    @Resource
    private GroupHallsDao groupHallsDao;


    @Override
    public void sendCampsegInfoById(String campsegPid) {
        try {
            //activityBO营销活动对象属性
            CampBaseInfoVO campsegBaseInfo = mcdCampDefDao.getCampsegBaseInfo(campsegPid);
            // 获取运营位类型
            String adivType = groupHallsDao.queryAdivType(campsegPid);
            //转化为CampInfoVo对象
            CampInfoVo campVo = new CampInfoVo();
            campVo = getCampInfoVo(campsegBaseInfo, adivType);
            //campaignBO子活动对象属性（包含时机、客户群、产品、渠道等）
            List<CampsegSubVo> campsegSub = mcdCampDefDao.getCampsegSubListById(campsegPid);
            log.info("933活动详情:{}", JSONUtil.toJsonStr(campsegSub));
            getCampSubInfoVo(campsegSub);
            if (null == campsegSub || campsegSub.size() == 0) {
                log.info("未查询到子活动，直接返回");
                return;
            }
            log.info("923/926-CampsegSubVo：{}", campsegSub);
            campVo.setCampsegSub(campsegSub);
            //判读素材在本地是否存在，若不存在，则下载到本地
           /* IopGrouphallsMaterialVo iopGrouphallsMaterialVo =
                    groupHallsDao.queryMaterialByChannelIdAndCampsegIdGrouphalls(campsegPid);
            String resourceUrl = iopGrouphallsMaterialVo.getResourceUrl();
            File file = new File(resourceUrl);
            if (!file.exists()) {
                this.downFile(iopGrouphallsMaterialVo.getId(), resourceUrl);
            }
            uploadCampMaterial(campVo);*/
            pointcut.uploadCampsegInfo(campVo);
        } catch (Exception e) {
            log.error("同步策略信息发生异常:", e);
        }
    }



    /**
     * 审核数据复制
     *
     * @param baseInfo
     * @param campsegBaseInfo
     */
    private void copyAuditToVo(CampBaseInfoVO baseInfo, CampBaseInfoVO campsegBaseInfo) {
        campsegBaseInfo.setAuditUser(baseInfo.getAuditUser());
        campsegBaseInfo.setAuditPhone(baseInfo.getAuditPhone());
        campsegBaseInfo.setAuditEmail(baseInfo.getAuditEmail());
        campsegBaseInfo.setAuditOpion(baseInfo.getAuditOpion());
        campsegBaseInfo.setAuditDept(baseInfo.getAuditDept());
    }





    private void downFile(String materialId, String resourceUrl) {
        SftpFileDown sf = new SftpFileDown();
        ChannelSftp sftp = new ChannelSftp();
        try {
            String sftpServerIp = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_IP");
            Integer sftpServerPort =
                    Integer.parseInt(RedisUtils.getDicValue("MCD_MATERIAL_SFTP_PORT"));
            String sftpUserName = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_USERNAME");
            String sftpUserPwd = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_PASSWORD");
            String sftpPath = RedisUtils.getDicValue("MCD_MATERIAL_SFTP_PATH");
            String sftpLocalPath = RedisUtils.getDicValue("MCD_MATERIAL_LOCAL_PATH");
            String fileName = resourceUrl.split("/")[resourceUrl.split("/").length - 1];

            log.info("1.素材id是：" + materialId);
            sftp = sf.connect(sftpServerIp, sftpServerPort, sftpUserName, sftpUserPwd);
            log.info(
                "2.sftp登录成功！ip=" + sftpServerIp + ";port=" + sftpServerPort+ ";userName="+ sftpUserName + ",sftpPath："+ sftpPath+ ",sftpLocalPath:" + sftpLocalPath);
            sf.download(sftpPath, fileName, sftpLocalPath + fileName, sftp);
            log.info("3.下载成功！fileName=" + fileName);
        } catch (Exception e) {
            log.error("下载素材到本地目录出错：" + e);
        } finally {
            sf.disconnect(sftp);
        }
    }

    /**
     * 上传素材 - 集团手厅(923)流程一
     *
     * @param campVo
     * @return
     * @throws Exception
     */
    private void uploadCampMaterial(CampInfoVo campVo) throws Exception {
        IopGrouphallsMaterialVo material = groupHallsDao.queryMaterialByChannelIdAndCampsegIdGrouphalls(campVo.getCampsegPid());
        extracted(campVo, material);
    }

    private void extracted(CampInfoVo campVo, IopGrouphallsMaterialVo material) throws SftpException {
        log.info("营销活动{}开始上传物料", campVo.getCampsegPid());
        SftpFileDown sfDat = new SftpFileDown();
        ChannelSftp sftpDat = new ChannelSftp();
        try {
            //物料名称-本地名称 路径+名称
            String materialLocalPathName = material.getResourceUrl();
            log.info("物料名称：" + materialLocalPathName);
            //物料-上传之后名称
            String materialSftpName = (StrUtil.isEmpty(material.getBgPicGo2Url()) ? campVo.getCampsegSub().get(0).getCampsegId() : campVo.getCampsegSub().stream().
                    map(CampsegSubVo::getCampsegId).filter(campsegId -> campsegId.contains(material.getBgPicGo2Url()))
                    .findAny().orElse(campVo.getCampsegSub().get(0).getCampsegId())) + "_" + material.getId() + ".jpg";

            log.info("物料sftp上名称:" + materialSftpName);
            //sftp信息
            String ftpServer = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SFTP_IP);
            String ftpServerPort = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SFTP_PORT);
            String ftpUserName = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SFTP_USER);
            String ftpUserPwd = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_SFTP_PASSWORD);
            String ftpStorePath = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_MATERIAL_SFTP_PATH);// ftp目录
            //sftp连接
            sftpDat = sfDat.connect(ftpServer, Integer.parseInt(ftpServerPort), ftpUserName, ftpUserPwd);
            log.info("1、sftp连接成功");
            //创建上传目录
            String filePath = campVo.getActivityId();//活动编码
            String[] strs = filePath.split("/");
            StringBuilder sb = new StringBuilder(ftpStorePath);
            for (String str : strs) {
                sb.append(str).append("/");
                if (sfDat.isDirExist(sftpDat, sb.toString())) {
                    sftpDat.mkdir(sb.toString());
                }
            }
            sftpDat.cd(ftpStorePath + filePath);
            log.info("2、sftp目录创建完成");
            //sftp上传
            sfDat.uploadNewFileName(ftpStorePath + filePath, materialLocalPathName,materialSftpName, sftpDat);
            log.info("3、物料上传成功！");

            String bgId = material.getBgId();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bgId) && ("xz".equals(profile) || "hlj".equals(profile))) {
                //物料名称-本地名称 路径+名称
                String materialLocalPathNameBg = material.getBgPicUrl();
                log.info("物料背景图片名称：" + materialLocalPathName);
                //物料-上传之后名称
                String materialSftpNameBg = campVo.getCampsegSub().get(0).getCampsegId() + "_" + bgId + ".jpg";

                //sftp连接
                sftpDat = sfDat.connect(ftpServer, Integer.parseInt(ftpServerPort), ftpUserName, ftpUserPwd);
                log.info("bg1、sftp连接成功");
                sftpDat.cd(ftpStorePath + filePath);
                log.info("bg2、sftp目录创建完成");
                //sftp上传
                sfDat.uploadNewFileName(ftpStorePath + filePath, materialLocalPathNameBg,materialSftpNameBg, sftpDat);
                log.info("bg3、物料背景图片上传成功！");
            }
        }catch(Exception e ) {
            log.error("物料上传失败！",e);
            throw e;
        }finally {
            sfDat.disconnect(sftpDat);
            log.info("4、sftp关闭完成！");
        }
    }

    /**
     * v1.4 版本获取app的版本号,并且封装成上传活动信息时需要的格式
     *
     * @return
     */
    String getAppVersion() {
        String version = "";
        try {
            Map<String, Object> requestParam = new HashMap<String, Object>();
            requestParam.put("serviceName", "chan1000003_ver_query");
            Map<String, Object> header = new HashMap<String, Object>();
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            long currentTimeStamp = System.currentTimeMillis();
            header.put("version", "1.0");
            header.put("timestamp", String.valueOf(currentTimeStamp));
            String digest = Base64.encodeBase64String(MD5Util.encode(String.valueOf(currentTimeStamp) + "CM_201709").getBytes());
            header.put("digest", digest);
            header.put("conversationId", formatter.format(currentTime) + "888888");
            requestParam.put("header", header);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("iopCode", "1000003");
            requestParam.put("data", data);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(requestParam, httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            String queryAppVersionUrl = RedisUtils.getDicValue("grouphalls.query.app.version.url");
            ResponseEntity<String> result = restTemplate.postForEntity(queryAppVersionUrl, httpEntity, String.class);
            String response = result.getBody();
            log.info("--getAppVersion--response--"+response);
            JSONObject jsonObject = JSONObject.fromObject(response);
            JSONArray jsonArray = new JSONArray();
            if("0000".equals(jsonObject.getString("responseCode"))){
                JSONArray ja = jsonObject.getJSONArray("resultList");
                JSONObject jObj = new JSONObject();
                for (int i = 0; i < ja.size(); i++) {
                    jObj = new JSONObject();
                    jObj.put("platformCode", ja.getJSONObject(i).get("platformCode"));
                    jObj.put("version", ja.getJSONObject(i).get("platformVersion"));
                    jsonArray.add(jObj);
                }
            }else{
                log.info("--getAppVersion--返回异常--");
            }
            version = jsonArray.toString();
        } catch (Exception e) {
            log.error("获取版本失败：", e);
        }
        return version;
    }

    public static void main(String[] args) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jObj = new JSONObject();
        jObj.put("platformCode", "1");
        jObj.put("version", "[\"4.8.0\",\"4.8.1\"]");
        jsonArray.add(jObj);
        System.out.println(jsonArray.toString());

    }




    //修改数据库中序列号的当前值
    protected void saveCurrentSequency(String key, int sequence, boolean insert) {
        try {
            IopMobileInfo mobileInfo = new IopMobileInfo();
            mobileInfo.setSequenceKey(key);
            mobileInfo.setSequenceValue(sequence);
            if(insert){
                groupHallsDao.insertIopMobleInfo(mobileInfo);
            }else{
                groupHallsDao.updateIopMobleInfo(mobileInfo);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    //从数据库中取得当前的重传序列号，没有时为0，并修改数据库序列号
    protected Integer getCurrentSequence(String key) {
        return groupHallsDao.selectCurrentSequence(key);
    }

    private synchronized static Object getLock(String key) {
        if (null == map.get(key)) {
            map.put(key, new Object());
        }
        return map.get(key);
    }



    /**
     * 获取CampInfoVo对象
     * @param campsegBaseInfo
     * @return
     */
    public CampInfoVo getCampInfoVo(CampBaseInfoVO campsegBaseInfo, String adivType){
        CampInfoVo campVo = new CampInfoVo();
        campVo.setCampsegPid(campsegBaseInfo.getCampsegId());
        campVo.setActivityName(campsegBaseInfo.getCampsegName());
        campVo.setCampsegDesc(campsegBaseInfo.getCampsegDesc());
        campVo.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(campsegBaseInfo.getStartDate()));
        campVo.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(campsegBaseInfo.getEndDate()));
        campVo.setActivityType(adivType);
        campVo.setActivityObjective(campsegBaseInfo.getActivityObjective());
        campVo.setExecStatus(String.valueOf(campsegBaseInfo.getCampsegStatId()));
        String iopCode = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_IOPCODE);
        iopCode = StringUtils.isNotBlank(iopCode) ? iopCode : "2891";
        if("nx".equals(profile)){
            campVo.setActivityId(iopCode + campsegBaseInfo.getCampsegRootId());
        }else{
            campVo.setActivityId(iopCode + campsegBaseInfo.getCampsegId());
        }
        // v1.4新增
        campVo.setAuditUser("admin");
        campVo.setAuditDept("省公司");
        campVo.setAuditOpion("本省部门经理已审批并同意内容发布在一级电子渠道");
        return campVo;
    }

    /**
     * 获取CampInfoVo对象
     * @param campsegBaseInfo
     * @return
     */
    public CampInfoVo getCampInfoVo(CampBaseInfoVO campsegBaseInfo) {
        CampInfoVo campVo = new CampInfoVo();
        campVo.setCampsegPid(campsegBaseInfo.getCampsegId());
        campVo.setActivityName(campsegBaseInfo.getCampsegName());
        campVo.setCampsegDesc(campsegBaseInfo.getCampsegDesc());
        if (ObjectUtil.isNotNull(campsegBaseInfo.getStartDate())) {
            campVo.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(campsegBaseInfo.getStartDate()));
        }else {
            campVo.setStartTime("");//必有，无返空
        }
        if (ObjectUtil.isNotNull(campsegBaseInfo.getEndDate())) {
            campVo.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(campsegBaseInfo.getEndDate()));
        }else {
            campVo.setEndTime("");//必有，无返空
        }
        campVo.setActivityType(campsegBaseInfo.getActivityType());
        campVo.setActivityObjective(campsegBaseInfo.getActivityObjective());
        campVo.setExecStatus(String.valueOf(campsegBaseInfo.getCampsegStatId()));
        String iopCode = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_IOPCODE);
        iopCode = StringUtils.isNotBlank(iopCode) ? iopCode : "2891";
        campVo.setActivityId(iopCode + campsegBaseInfo.getCampsegId());

        // v1.4新增
        campVo.setAuditUser("admin");
        campVo.setAuditDept("省公司");
        campVo.setAuditOpion("本省部门经理已审批并同意内容发布在一级电子渠道");

        // v1.7.1版本兼容
        if (StrUtil.isNotBlank(campsegBaseInfo.getAuditUser())) {
            campVo.setAuditUser(campsegBaseInfo.getAuditUser());
        }else {
            campVo.setAuditUser("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getAuditDept())) {
            campVo.setAuditDept(campsegBaseInfo.getAuditDept());
        }else {
            campVo.setAuditDept("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getAuditOpion())) {
            campVo.setAuditOpion(campsegBaseInfo.getAuditOpion());
        }else {
            campVo.setAuditOpion("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getAuditPhone())) {
            campVo.setAuditPhone(campsegBaseInfo.getAuditPhone());
        }else {
            campVo.setAuditPhone("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getAuditEmail())) {
            campVo.setAuditEmail(campsegBaseInfo.getAuditEmail());
        }else {
            campVo.setAuditEmail("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getEleBusinessType())) {
            campVo.setBusinessType(campsegBaseInfo.getEleBusinessType());
        }else {
            campVo.setBusinessType("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getEleActivityObjective())) {
            campVo.setActivityObjective(campsegBaseInfo.getEleActivityObjective());
        }else {
            campVo.setActivityObjective("");//必有，无返空
        }
        if (StrUtil.isNotBlank(campsegBaseInfo.getEleActivityType())) {
            campVo.setActivityType(campsegBaseInfo.getEleActivityType());
        }else {
            campVo.setActivityType("");//必有，无返空
        }
        return campVo;
    }

    /**
     * 获取CampInfoVo对象
     * @param campsegSubVos
     * @return
     */
    public void getCampSubInfoVo(List<CampsegSubVo> campsegSubVos) {
        String iopCode = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_IOPCODE);
        String channelCode = RedisUtils.getDicValue(RedisDicKey.GROUPHALLS_CHANNELCODE);
        String versionOpen = RedisUtils.getDicValue("grouphalls.version.switch");
        String appVersion = null;
        if ("1".equals(versionOpen)) {
            appVersion = getAppVersion();
        }

        for (CampsegSubVo campsegSubVo : campsegSubVos) {
            campsegSubVo.setCampsegId(iopCode + campsegSubVo.getCampsegId() + channelCode + campsegSubVo.getChannelAdivId());
            // 版本获取失败时用默认版本号
            if (StringUtils.isEmpty(appVersion)) {
                campsegSubVo.setIssueVersion("[{\"platformCode\":\"1\",\"version\":[\"4.8.0\",\"4.8.5\",\"4.9.0\",\"4.9.5\",\"5.0.0\",\"5.0.5\",\"5.0.6\",\"5.1.0\",\"5.2.0\",\"5.2.6\",\"5.3.0\",\"5.3.1\",\"5.3.5\",\"5.5.0\",\"5.6.0\",\"5.7.0\",\"5.8.0\",\"5.8.2\"]}" +
                        ",{\"platformCode\":\"2\",\"version\":[\"4.8.0\",\"4.8.5\",\"4.9.0\",\"4.9.5\",\"5.0.0\",\"5.0.5\",\"5.1.0\",\"5.2.0\",\"5.2.6\",\"5.3.0\",\"5.5.0\",\"5.5.1\",\"5.6.0\",\"5.7.0\",\"5.8.0\",\"5.8.2\",\"5.8.3\",\"5.8.6\"]}]");
            } else {
                campsegSubVo.setIssueVersion(appVersion);
            }
            campsegSubVo.setChannelId(channelCode);
        }
    }




}

package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.client.pec.approve.model.CmpApprovalProcess;
import com.asiainfo.biapp.client.pec.approve.model.RecordPageDTO;
import com.asiainfo.biapp.client.pec.approve.model.SubmitProcessQuery;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.jx.api.PecApproveFeignClient;
import com.asiainfo.biapp.pec.plan.jx.api.dto.SubmitProcessJxDTO;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CmpApproveProcessRecordJx;
import com.asiainfo.biapp.pec.plan.jx.hmh5.enums.Hmh5BlacklistEnum;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdHmh5BlacklistTaskMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdHmh5BlacklistTask;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdFrontBlackListService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdHmh5BlacklistTaskService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApprRecord;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.BlacklistApproveJxQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.vo.SmsTemplateApprRecord;
import com.asiainfo.biapp.pec.plan.util.IdUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户通黑名单批量导入任务 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2024-05-28
 */
@Service
@Slf4j
public class McdHmh5BlacklistTaskServiceImpl extends ServiceImpl<McdHmh5BlacklistTaskMapper, McdHmh5BlacklistTask> implements McdHmh5BlacklistTaskService {

    @Value("${hmh5.blacklist.sftp.ip}")
    private String ftpServer;
    @Value("${hmh5.blacklist.sftp.port}")
    private int port;
    @Value("${hmh5.blacklist.sftp.user}")
    private String user;
    @Value("${hmh5.blacklist.sftp.password}")
    private String pwd;
    @Value("${hmh5.blacklist.sftp.path}")
    private String ftpDir;
    @Value("${hmh5.blacklist.localPath}")
    private String localFilePath;

    @Autowired
    private McdHmh5BlacklistTaskMapper mcdHmh5BlacklistTaskMapper;

    @Autowired
    private PecApproveFeignClient approveFeignClient;

    @Autowired
    private McdFrontBlackListService mcdFrontBlackListService;

    @Override
    public ActionResponse createOrUpdate(MultipartFile multiFile, String taskId, String taskName, String remark, UserSimpleInfo user) {
        McdHmh5BlacklistTask task = new McdHmh5BlacklistTask();
        String filename = multiFile.getOriginalFilename();
        try {
            Map<String, Object> map = saveUploadFile(multiFile);
            Date now = new Date();
            if (StrUtil.isEmpty(taskId)) {
                taskId = IdUtils.generateId();
                task.setCreateTime(now);
            }
            task.setTaskId(taskId);
            task.setTaskName(taskName);
            task.setTaskStatus(0);
            task.setTaskFileName(String.valueOf(map.get("newFileName")));
            if (user != null) {
                task.setCreateUserId(user.getUserId());
                task.setCreateUserName(user.getUserName());
            }
            task.setUpdateTime(now);
            task.setRemark(remark);
            task.setUserNum(Integer.valueOf(String.valueOf(map.get("rows"))));
        } catch (Exception e) {
            log.error("导入文件失败:", e);
            task.setTaskStatus(6);
            this.saveOrUpdate(task);
            return ActionResponse.getFaildResp("文件=" + filename +"导入失败");
        }
        this.saveOrUpdate(task);
        return ActionResponse.getSuccessResp();
    }


    public void importBlacklistByTaskId(String taskId) throws Exception {
        McdHmh5BlacklistTask hmh5BlacklistTask = this.getById(taskId);
        if(null == hmh5BlacklistTask){
            log.error("任务不存在:{}",taskId);
            return ;
        }
        // 文件名称
        String taskFileName = hmh5BlacklistTask.getTaskFileName();
        // 任务创建人
        String createUserId = hmh5BlacklistTask.getCreateUserId();
        File file = new File(localFilePath + taskFileName);
        if(!file.exists()){
            // 如果本地不存在文件，从sftp下载
            SftpUtils sfDat = new SftpUtils();
            ChannelSftp  channelSftp = sfDat.connect(ftpServer, port, user, pwd);
            boolean download = sfDat.download(ftpDir, taskFileName, localFilePath, channelSftp);
            if(!download){
                throw new BaseException("文件下载失败");
            }
        }
        mcdFrontBlackListService.impOrDelBlacklistFile(file,createUserId);

    }
    /**
     * 客户通黑名单提交审批
     *
     * @param req  请求入参
     * @param user 用户
     */
    @Override
    public void submitBlacklist(SubmitProcessQuery req, UserSimpleInfo user) {
        String taskId = req.getBusinessId();
        McdHmh5BlacklistTask mcdHmh5BlacklistTask = mcdHmh5BlacklistTaskMapper.selectById(taskId);
        Assert.notNull(mcdHmh5BlacklistTask, "当前黑名单任务不存在");
        final ActionResponse<Object> submit = approveFeignClient.submit(req);
        log.info("客户通黑名单提交审批结果->{}", JSONUtil.toJsonStr(submit));
        if (ResponseStatus.SUCCESS.equals(submit.getStatus())) {
            log.info("【客户通黑名单】submit approval success, flowId->{}", submit.getData());
            if (StringUtils.isBlank(mcdHmh5BlacklistTask.getInstanceId()) || Hmh5BlacklistEnum.DRAFT.getId().equals(mcdHmh5BlacklistTask.getTaskStatus())) {
                final Long id = (Long) submit.getData();
                mcdHmh5BlacklistTask.setInstanceId(Long.toString(id));
                mcdHmh5BlacklistTask.setTaskStatus(Hmh5BlacklistEnum.APPROVING.getId());
                mcdHmh5BlacklistTaskMapper.updateById(mcdHmh5BlacklistTask);
            }
        } else {
            throw new BaseException("【客户通黑名单】提交审批失败");
        }
    }

    /**
     * 获取客户通黑名单审批流程实例节点下级审批人
     *
     * @param submitProcessDTO submitProcessDTO
     * @return SubmitProcessDTO
     */
    @Override
    public ActionResponse<SubmitProcessJxDTO> getBlacklistApprover(SubmitProcessJxDTO submitProcessDTO) {
        if (submitProcessDTO.getProcessId() == null){
            final ActionResponse<CmpApprovalProcess> blacklist = approveFeignClient.getApproveConfig(submitProcessDTO.getApprovalType());
            log.info("【客户通黑名单】getApproveConfig返回 ====> {}", JSONUtil.toJsonStr(blacklist));
            // feign调用失败的情况
            if (ResponseStatus.SUCCESS.getCode() != blacklist.getStatus().getCode())
                throw new BaseException(blacklist.getMessage());
            // 首次获取审批流程节点的必要参数
            JSONObject triggerParm = new JSONObject();
            triggerParm.putOpt("channelId", "");  //不设置此值无法查询到结果
            submitProcessDTO.setProcessId(blacklist.getData().getProcessId());
            submitProcessDTO.setBerv(blacklist.getData().getBerv());
            submitProcessDTO.setTriggerParm(triggerParm);
        }
        log.info("【客户通黑名单】getNodeApprover入参 ====> {}", JSONUtil.toJsonStr(submitProcessDTO));
        final ActionResponse<SubmitProcessJxDTO> submitprocessdto = approveFeignClient.getNodeApprover(submitProcessDTO);
        log.info("【客户通黑名单】SubmitProcessDTO -【{}】", submitprocessdto);
        return submitprocessdto;
    }

    /**
     * 客户通黑名单审批列表
     *
     * @param req 客户通黑名单审批列表分页(或根据条件)查询入参
     * @return {@link IPage}<{@link BlacklistApprRecord}>
     */
    @Override
    public IPage<BlacklistApprRecord> approveBlacklistRecord(BlacklistApproveJxQuery req) {
        RecordPageDTO dto = new RecordPageDTO();
        dto.setCurrent(req.getCurrent());
        dto.setSize(req.getSize());
        List<BlacklistApprRecord> blacklistApprRecordList  = mcdHmh5BlacklistTaskMapper.qryApprRecord(Collections.EMPTY_SET, req);
        if(CollectionUtils.isEmpty(blacklistApprRecordList)){
            log.info("客户通黑名单审批列表为空！");
            return new Page<>();
        }
        Map<String, BlacklistApprRecord> blacklistMap = blacklistApprRecordList.stream().collect(Collectors.toMap(BlacklistApprRecord::getTaskId, Function.identity()));
        dto.setList(Lists.newArrayList(blacklistMap.keySet()));
        final ActionResponse<Page<CmpApproveProcessRecordJx>> userRecord = approveFeignClient.getUserRecordNew(dto);
        log.info("【客户通黑名单】当前用户待审记录->{}", JSONUtil.toJsonStr(userRecord));
        Assert.isTrue(ResponseStatus.SUCCESS.equals(userRecord.getStatus()), userRecord.getMessage());
        final IPage<CmpApproveProcessRecordJx> data = userRecord.getData();
        final Set<String> taskIdSet = data.getRecords().stream().map(CmpApproveProcessRecordJx::getBusinessId).collect(Collectors.toSet());
        blacklistApprRecordList = mcdHmh5BlacklistTaskMapper.qryApprRecord(taskIdSet, req);
        blacklistMap = blacklistApprRecordList.stream().collect(Collectors.toMap(BlacklistApprRecord::getTaskId, Function.identity()));
        IPage<BlacklistApprRecord> apprRecordIPage = new Page<>(req.getCurrent(), req.getSize(), blacklistApprRecordList.size());
        List<BlacklistApprRecord> listRecord = new ArrayList<>();
        apprRecordIPage.setRecords(listRecord);
        for (CmpApproveProcessRecordJx record : data.getRecords()) {
            if (null != blacklistMap.get(record.getBusinessId())) {
                listRecord.add(convertToAppr(record, blacklistMap.get(record.getBusinessId())));
            }
        }
        return apprRecordIPage;
    }

    /**
     * 信息转换
     *
     * @param record 审批信息
     * @param blacklistApprRecord 客户通黑名单对象信息
     * @return {@link SmsTemplateApprRecord}
     */
    public BlacklistApprRecord convertToAppr(CmpApproveProcessRecordJx record, BlacklistApprRecord blacklistApprRecord) {
        final BlacklistApprRecord apprRecord = new BlacklistApprRecord();
        // 模板相关信息
        apprRecord.setTaskId(blacklistApprRecord.getTaskId());
        apprRecord.setTaskName(blacklistApprRecord.getTaskName());
        apprRecord.setUserName(blacklistApprRecord.getUserName());
        apprRecord.setCreateTime(blacklistApprRecord.getCreateTime());
        apprRecord.setCreateUserId(blacklistApprRecord.getCreateUserId());
        // 审批对象相关需要的信息
        apprRecord.setId(record.getId());
        apprRecord.setBusinessId(record.getBusinessId());
        apprRecord.setInstanceId(record.getInstanceId());
        apprRecord.setNodeId(record.getNodeId());
        apprRecord.setNodeName(record.getNodeName());
        apprRecord.setNodeType(record.getNodeType());
        apprRecord.setNodeBusinessName(record.getNodeBusinessName());
        apprRecord.setApprover(record.getApprover());
        apprRecord.setApproverName(record.getApproverName());
        apprRecord.setDealOpinion(record.getDealOpinion());
        apprRecord.setDealStatus(record.getDealStatus());
        apprRecord.setDealTime(record.getDealTime());
        apprRecord.setPreRecordId(record.getPreRecordId());
        apprRecord.setEventId(record.getEventId());
        apprRecord.setCreateDate(record.getCreateDate());
        apprRecord.setCreateBy(record.getCreateBy());
        apprRecord.setModifyDate(record.getModifyDate());
        apprRecord.setModifyBy(record.getModifyBy());
        return apprRecord;
    }


    /**
     * 上传黑名单文件到sftp
     *
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String, Object> saveUploadFile(MultipartFile file) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        SftpUtils sfDat = null;
        ChannelSftp channelSftp = null;
        try {
            String id = UUID.randomUUID().toString().replace("-", "");
            String filename = file.getOriginalFilename();
            String newFileName = id + "_" + filename;
            if (!localFilePath.endsWith(File.separator)) {
                localFilePath += File.separator;
            }
            File path = new File(localFilePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!ftpDir.endsWith(File.separator)) {
                ftpDir += File.separator;
            }
            //保存到本地
            file.transferTo(new File(localFilePath, newFileName));
            //上传到sftp
            sfDat = new SftpUtils();
            channelSftp = sfDat.connect(ftpServer, port, user, pwd);
            boolean upload = sfDat.upload(ftpDir, newFileName, localFilePath, channelSftp);
            if (!upload) {
                throw new Exception("error on upload!");
            }
            // 获取文件非空行数
            int rows = countNonEmptyRows(localFilePath + newFileName);
            log.info("saveUploadFile-->文件路径={}，导入rows={}", localFilePath + newFileName, rows - 1);
            resMap.put("newFileName", newFileName);
            // 去掉表头
            resMap.put("rows", rows - 1);
            return resMap;
        } finally {
            if (sfDat != null) {
                try {
                    sfDat.disconnect(channelSftp);
                } catch (JSchException e) {
                    log.info("关闭SFTP异常！！！");
                }
            }
        }
    }

    /**
     * 获取文件非空行数
     *
     * @param filePath 文件路径
     * @return int
     */
    public static int countNonEmptyRows(String filePath) {
        log.info("countNonEmptyRows-->filePath={}", filePath);
        int rowCount = 0;
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = null;
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx are supported.");
            }
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (hasData(row)) {
                    rowCount++;
                }
            }
        } catch (IOException e) {
            log.error("countNonEmptyRows-->获取文件非空行数异常：",e);
        }
        return rowCount;
    }

    /**
     * 判断当前行是否有内容
     *
     * @param row 行
     * @return boolean
     */
    private static boolean hasData(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return true;
            }
        }
        return false;
    }

}

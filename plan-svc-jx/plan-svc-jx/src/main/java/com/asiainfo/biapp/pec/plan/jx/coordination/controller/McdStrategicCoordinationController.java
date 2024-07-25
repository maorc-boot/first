package com.asiainfo.biapp.pec.plan.jx.coordination.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.common.jx.enums.CampCoordinateStatus;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdPlanDef;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskListModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCoordinatePlanModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.*;
import com.asiainfo.biapp.pec.plan.jx.coordination.response.ApprTaskRecord;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.*;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCampCoordinationTaskVo;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanTypeVo;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.McdCoordinatePlanVo;
import com.asiainfo.biapp.pec.plan.jx.coordination.vo.StrategicCoordinationBaseInfo;
import com.asiainfo.biapp.pec.plan.util.MpmUtil;
import com.asiainfo.biapp.pec.plan.util.SftpUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "江西策略统筹服务")
@RequestMapping("/mcd/strategic/coordination")
public class McdStrategicCoordinationController {


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Value("${plan.campCoordinate.properties.sftp.host:10.19.92.24}")
    private String host;
    @Value("${plan.campCoordinate.properties.sftp.port:22022}")
    private int port;
    @Value("${plan.campCoordinate.properties.sftp.username:iop-dev}")
    private String username;
    @Value("${plan.campCoordinate.properties.sftp.password:Iop@)@#)$)$2344}")
    private String password;
    @Value("${plan.campCoordinate.properties.sftp.serverPath:/home/iop-dev/sftp/802/server/}")
    private String serverPath;
    @Value("${plan.campCoordinate.properties.localFilePath:/home/iop-dev/sftp/802/local/}")
    private String localFilePath;
    @Value("${plan.campCoordinate.properties.unifySftpPath:/home/iop-dev/upload/sucai/}")
    private String sftpPath;

    public static final String SFTP_IP = "MCD_MATERIAL_SFTP_IP";
    public static final String SFTP_PASSWORD = "MCD_MATERIAL_SFTP_PASSWORD";
    public static final String SFTP_PORT = "MCD_MATERIAL_SFTP_PORT";
    public static final String SFTP_USERNAME = "MCD_MATERIAL_SFTP_USERNAME";

    @Resource
    private McdStrategicCoordinationService mcdStrategicCoordinationService;

    @Resource
    private McdCampCoordinationTaskService mcdCampCoordinationTaskService;

    @Resource
    private McdCampCoordinationTaskListService mcdCampCoordinationTaskListService;

    @Autowired
    private IMcdCoordinatePlanService mcdCoordinatePlanService;

    @Resource
    private IMcdCampCoordinationApproveService mcdCampCoordinationApproveService;

    @ApiOperation("江西策略池策略分页查询")
    @PostMapping("/queryStrategicCoordination")
    public ActionResponse<Page<StrategicCoordinationBaseInfo>>  queryStrategicCoordinationBaseInfos (@RequestBody McdStrategicCoordinationReq req){

        log.info("江西策略池策略分页查询入参: {}"+ req);

        return ActionResponse.getSuccessResp(mcdStrategicCoordinationService.queryStrategicCoordinationBaseInfos(req));
    }

    @ApiOperation("江西策略池融合计算全部选择")
    @PostMapping("/selectAllStrategicCoordination")
    public ActionResponse<List<StrategicCoordinationBaseInfo>>  selectAllStrategicCoordination (@RequestBody McdStrategicCoordinationReq req){

        log.info("江西策略池融合计算全部选择查询入参: {}"+ req);

        return ActionResponse.getSuccessResp(mcdStrategicCoordinationService.selectAllStrategicCoordinationList(req));
    }


    @ApiOperation("保存策略池融合计算任务")
    @PostMapping("/saveStrategicCoordinationTask")
    public ActionResponse  saveStrategicCoordinationTask (@RequestBody @Valid McdSaveStrategicCoordinationReq req, HttpServletRequest request){
        String dataTime = DATE_FORMAT.format(new Date());
        log.info("江西保存策略池融合计算任务开始");

        List<McdCampCoordinationTaskListModel>  taskModelList = req.getList();
        if (taskModelList.isEmpty()){
            return ActionResponse.getFaildResp("没有可保存的关系数据!");
        }
        McdCampCoordinationTaskModel taskModel = req.getTaskModel();
        UserSimpleInfo user = UserUtil.getUser(request);
        if (Objects.isNull(user)){
            user = new UserSimpleInfo();
            user.setUserId("admin01");
            user.setUserName("管理员1");
        }
        String taskId = MpmUtil.generateCampsegAndTaskNo();
        taskModel.setTaskId(taskId);
        taskModel.setCreateUserId(user.getUserId());
        taskModel.setCreateUserName(user.getUserName());
        taskModel.setExecStatus(10);//待执行
        taskModel.setCreateTime(dataTime);


        boolean flag = mcdCampCoordinationTaskService.save(taskModel);
        if (flag){
            return saveCampCoordinationTaskList(taskModelList,taskId);
        }
        return ActionResponse.getFaildResp();
    }


    @ApiOperation("查询计算结果主任务列表")
    @PostMapping("/queryCampCoordinationTask")
    public ActionResponse<Page<McdCampCoordinationTaskVo>>  queryCampCoordinationTask (@RequestBody McdPageQuery req){
        Page<McdCampCoordinationTaskVo> pageResult = new Page<>();
        List<McdCampCoordinationTaskVo> voList = new ArrayList<>();
        log.info("江西查询计算结果任务列表入参: {}", JSONUtil.toJsonStr(req));
        LambdaQueryWrapper<McdCampCoordinationTaskModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(McdCampCoordinationTaskModel::getCreateTime).eq(McdCampCoordinationTaskModel::getTaskPId, 0);
        Page<McdCampCoordinationTaskModel> page = new Page<>();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        Page<McdCampCoordinationTaskModel> taskModelPage = mcdCampCoordinationTaskService.page(page, queryWrapper);
        // 处理是不是有待审批的子任务的逻辑
        delHasChildTaskLogic(pageResult, voList, taskModelPage);

        return ActionResponse.getSuccessResp(pageResult);
    }

    /**
     * 处理是不是有待审批的子任务的逻辑
     *
     * @param pageResult    pageResult
     * @param voList        转换后的集合
     * @param taskModelPage taskModelPage
     */
    private void delHasChildTaskLogic(Page<McdCampCoordinationTaskVo> pageResult, List<McdCampCoordinationTaskVo> voList, Page<McdCampCoordinationTaskModel> taskModelPage) {
        // 主任务id集合
        List<String> taskIds = taskModelPage.getRecords().stream().map(McdCampCoordinationTaskModel::getTaskId).collect(Collectors.toList());
        // 获取主任务下的子任务信息
        List<ApprTaskRecord> apprTaskRecords = mcdCampCoordinationApproveService.approveChildTaskRecord(taskIds, true, CampCoordinateStatus.APPROVAL_PENDIING.getCode());
        // 按taskPId分组  获取子任务信息
        Map<String, List<ApprTaskRecord>> collect = apprTaskRecords.stream().collect(Collectors.groupingBy(ApprTaskRecord::getTaskPId, Collectors.toList()));
        taskModelPage.getRecords().forEach(record -> {
            // 子任务信息
            List<ApprTaskRecord> childTasks = collect.get(record.getTaskId());
            McdCampCoordinationTaskVo vo = new McdCampCoordinationTaskVo();
            BeanUtil.copyProperties(record, vo);
            // 计算耗时
            double sec = NumberUtil.div(record.getCalcTime(), 1000, 3, RoundingMode.HALF_UP); // 换算秒 保留三位 四舍五入
            double min = NumberUtil.div(sec, 60, 2, RoundingMode.HALF_UP); // 换算分钟 小于1分钟用秒为单位，大于60分钟用小时做单位
            if (min < 60) {
                if (min <= 1) {
                    vo.setCalcTime(NumberUtil.roundStr(NumberUtil.mul(min, 60), 2) + "s"); // 单位秒
                } else {
                    vo.setCalcTime(min + "min"); // 单位分钟
                }
            } else {
                vo.setCalcTime(NumberUtil.div(min, 60, 2, RoundingMode.HALF_UP) + "h"); // 单位小时
            }
            if (CollectionUtil.isNotEmpty(childTasks)) {
                vo.setHasChildTasks(true); // 该主任务下有待审批的子任务信息
            }
            voList.add(vo);
        });
        pageResult.setCurrent(taskModelPage.getCurrent());
        pageResult.setSize(taskModelPage.getSize());
        pageResult.setTotal(taskModelPage.getTotal());
        pageResult.setRecords(voList);
    }

    @ApiOperation(value = "计算结果子任务列表查询", notes = "根据主任务id查询：计算结果子任务列表查询")
    @PostMapping("/childTaskRecord")
    public ActionResponse childTaskRecord(@RequestBody McdIdQuery query) {
        log.info("统筹子任务列表查询入参={}", JSONUtil.toJsonStr(query));
        List<ApprTaskRecord> apprTaskRecords = mcdCampCoordinationApproveService.approveChildTaskRecord(Collections.singletonList(query.getId()), false, null);
        log.info("统筹子任务列表查询返回={}", JSONUtil.toJsonStr(apprTaskRecords));
        return ActionResponse.getSuccessResp(apprTaskRecords);
    }

    @ApiOperation("删除策略池融合计算任务及策略关系数据")
    @PostMapping("/delCampCoordinationTaskList")
    public ActionResponse  delCampCoordinationTaskList (@RequestBody McdCampCoordinationTaskModel model ){

        if (StringUtils.isEmpty(model.getTaskId())){
            return ActionResponse.getFaildResp("删除任务ID为空!");
        }
        log.info("江西删除策略池融合计算任务及策略关系数据开始,任务ID: "+ model.getTaskId());

        mcdCampCoordinationTaskService.removeByIds(Arrays.asList(model.getTaskId().split(StrUtil.COMMA)));

        LambdaUpdateWrapper<McdCampCoordinationTaskListModel>  updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(McdCampCoordinationTaskListModel::getTaskId, Arrays.asList(model.getTaskId().split(StrUtil.COMMA)));

        mcdCampCoordinationTaskListService.remove(updateWrapper);

        return ActionResponse.getSuccessResp("删除任务数据成功!");
    }

    @ApiOperation("推送任务数据结果至第三方sftp")
    @PostMapping("/pushCampCoordinationFile")
    public ActionResponse  pushCampCoordinationFile (@RequestBody McdCampCoordinationTaskModel model ){

        if (StringUtils.isEmpty(model.getTaskId())){
            return ActionResponse.getFaildResp("推送任务ID为空!");
        }
        log.info("江西推送任务数据结果至第三方sftp开始,任务ID: "+ model.getTaskId());
        //先到统一平台下载文件,再上传
        String fileName = model.getPushFileName();
        String  fullFileName = getFullName(localFilePath,fileName);
        File file = new File(fullFileName);
        if (!file.exists()) {
            this.downFile(fileName,model.getTaskId());
        }
        ChannelSftp channelSftp = null;
        try {
            SftpUtils sftpUtils = new SftpUtils();
            channelSftp = sftpUtils.connect(host, port, username, password, "UTF-8");

            File custFile = new File(localFilePath + fileName);
            if (!custFile.exists()) {
                return ActionResponse.getFaildResp("没有可上传的文件!");
            }
            // 上传客户群清单文件
           boolean flag =  sftpUtils.upload(serverPath, localFilePath + fileName, channelSftp);

            if (flag){
                LambdaUpdateWrapper<McdCampCoordinationTaskModel>  updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(McdCampCoordinationTaskModel::getExecStatus,CampCoordinateStatus.DONE.getId())
                             .eq(McdCampCoordinationTaskModel::getTaskId,model.getTaskId());
                mcdCampCoordinationTaskService.update(updateWrapper);
            }

        } catch (Exception e) {
            log.error("策略统筹上传文件失败,taskId:{}", model.getTaskId());
            log.error("策略统筹上传文件异常",e);

        } finally {
            if (null != channelSftp) {
                channelSftp.disconnect();
            }
        }

        return ActionResponse.getSuccessResp("任务推送成功!");
    }


    /**
     * 导出样例文件
     *
     * @param model 条件入参
     * @param response
     */
    @PostMapping(path = "/exportExampleFile")
    @ApiOperation(value = "江西:导出样例文件", notes = "导出样例文件")
    public void exportMaterialsFile(@RequestBody ApprTaskRecord model, HttpServletResponse response) {
        log.info("exportMaterialsFile-->导出样例文件入参={}", JSONUtil.toJsonStr(model));
        String fileName = model.getExportFileName();
       /* try {
            fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        }catch (Exception e){
            log.error("策略统筹任务文件名称处理异常",e);
        }

        fileName = fileName.replaceAll("%2B", "+");*/

       if (StringUtils.isEmpty(fileName)){
           log.error("没有可导出的样例文件!");
           fileName = model.getPushFileName();
           log.info("导出生成的txt文件名称为: "+ fileName);
           if (StringUtils.isEmpty(fileName)){
               log.error("策略统筹任务:"+ model.getTaskId()+" 没有可导出的文件!");
               return;
           }
       }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        String  fullFileName = getFullName(localFilePath,fileName);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        byte[] bytes = new byte[2048];
        try {
            File file = new File(fullFileName);
            if (!file.exists()) {
                this.downFile(fileName,model.getTaskId());
            }
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            log.error("策略统筹任务样例文件导出异常",e);
        }finally {
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("流关闭异常",e);
                }
            }
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("流关闭异常",e);
                }
            }
        }

    }


    /**
     * 重新计算
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "策略统筹重新计算接口")
    @PostMapping("/recountCampTask")
    public ActionResponse recountCampTask(@RequestBody CampCoordinationStatusQuery req) {

        LambdaUpdateWrapper<McdCampCoordinationTaskModel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(McdCampCoordinationTaskModel::getExecStatus, CampCoordinateStatus.PENDING.getId())
                .eq(McdCampCoordinationTaskModel::getTaskId,req.getTaskId());

        boolean flag = mcdCampCoordinationTaskService.update(updateWrapper);

        return ActionResponse.getSuccessResp(flag);
    }

    @ApiOperation("推送任务数据结果至第三方sftp任务")
    @PostMapping("/pushCampCoordinationFileTask")
    public void   pushCampCoordinationFileTask (){

        LambdaQueryWrapper<McdCampCoordinationTaskModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdCampCoordinationTaskModel::getExecStatus,CampCoordinateStatus.APPROVAL_COMPLETED.getId())
                    .isNotNull(McdCampCoordinationTaskModel::getPushFileName)
                    .orderByDesc(McdCampCoordinationTaskModel::getCreateTime);
        List<McdCampCoordinationTaskModel> taskModelList = mcdCampCoordinationTaskService.list(queryWrapper);
        if (taskModelList.isEmpty()){
            log.info("当前无推送的任务!");
            return;
        }

        for (McdCampCoordinationTaskModel model : taskModelList) {

            log.info("江西推送任务数据结果至第三方sftp开始,任务ID: "+ model.getTaskId());
            //先到统一平台下载文件,再上传
            String fileName = model.getPushFileName();
            String  fullFileName = getFullName(localFilePath,fileName);
            File file = new File(fullFileName);
            if (!file.exists()) {
                this.downFile(fileName,model.getTaskId());
            }
            ChannelSftp channelSftp = null;
            try {
                SftpUtils sftpUtils = new SftpUtils();
                channelSftp = sftpUtils.connect(host, port, username, password, "UTF-8");

                File custFile = new File(localFilePath + fileName);
                if (!custFile.exists()) {
                     log.error("没有可上传的文件!");
                    continue;
                }
                // 上传客户群清单文件
                boolean flag =  sftpUtils.upload(serverPath, localFilePath + fileName, channelSftp);

                if (flag){
                    LambdaUpdateWrapper<McdCampCoordinationTaskModel>  updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(McdCampCoordinationTaskModel::getExecStatus,CampCoordinateStatus.DONE.getId())
                            .eq(McdCampCoordinationTaskModel::getTaskId,model.getTaskId());
                    mcdCampCoordinationTaskService.update(updateWrapper);
                }

            } catch (Exception e) {
                log.error("策略统筹上传文件失败,taskId:{}", model.getTaskId());
                log.error("策略统筹上传文件异常",e);

            } finally {
                if (null != channelSftp) {
                    channelSftp.disconnect();
                }
            }
        }


    }

    private String  getFullName(String filePath,String fileName){

        if (filePath.endsWith("/")){
            return filePath+fileName;
        }else {
            return filePath+"/"+fileName;
        }
    }

    /**
     *  文件下载到本地服务器
     * @param fileName
     * @param taskId
     */
    private void downFile(String fileName,String taskId) {
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            String sftpServerIp = RedisUtils.getDicValue(SFTP_IP);
            int sftpServerPort = Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
            String sftpUserName = RedisUtils.getDicValue(SFTP_USERNAME);
            String sftpUserPwd =  RedisUtils.getDicValue(SFTP_PASSWORD);


            log.info("1.下载统筹任务文件id是：" +taskId);
            sftp = sftpUtils.connect(sftpServerIp, sftpServerPort, sftpUserName, sftpUserPwd,"UTF-8");
            log.info( "2.sftp登录成功！ip=" + sftpServerIp + ";port=" + sftpServerPort+
                            ";userName="+ sftpUserName + ",sftpPath："+ sftpPath+
                            ",sftpLocalPath:" + localFilePath);
           boolean flag = sftpUtils.download(sftpPath, sftpPath +fileName, localFilePath + fileName, sftp);

            log.info("3.下载统筹任务文件成功,flag: "+flag+" fileName: " + fileName);
        } catch (Exception e) {
            log.error("下载统筹文件到本地目录出错：" + e);
        }finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常", e);
                }
            }
        }
    }



    private ActionResponse  saveCampCoordinationTaskList (List<McdCampCoordinationTaskListModel> listModels,String  taskId ){

        log.info("江西保存策略池融合计算任务与策略关系数据开始,size: "+ listModels.size());
        List<String> planIdList = new ArrayList<>();
        int customTotalNum = 0;

        for (McdCampCoordinationTaskListModel taskListModel : listModels) {

            taskListModel.setTaskId(taskId);
            try {
                mcdCampCoordinationTaskListService.save(taskListModel);
                customTotalNum+=taskListModel.getCustomNum();
                if (!planIdList.contains(taskListModel.getPlanId())){
                    planIdList.add(taskListModel.getPlanId());
                }
            }catch (Exception e){
                log.error("江西保存策略池融合计算任务与策略关系数据异常",e);
            }

        }

        LambdaUpdateWrapper<McdCampCoordinationTaskModel>  updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(McdCampCoordinationTaskModel::getCustomTotalNum,customTotalNum)
                .set(McdCampCoordinationTaskModel::getOriginalCustomNum, customTotalNum)
                .set(McdCampCoordinationTaskModel::getPlanTotalNum,planIdList.size())
                .eq(McdCampCoordinationTaskModel::getTaskId,taskId);
        mcdCampCoordinationTaskService.update(updateWrapper);

        return ActionResponse.getSuccessResp("保存任务数据成功!");

    }

    @ApiOperation(value = "查询产品系列类型集合")
    @PostMapping("/qryPlanTypeList")
    public ActionResponse qryPlanTypeList() {
        List<McdCoordinatePlanTypeVo> mcdCoordinatePlanTypeVoList = mcdCoordinatePlanService.qryPlanTypeList();
        return ActionResponse.getSuccessResp(mcdCoordinatePlanTypeVoList);
    }

    @ApiOperation(value = "产品配置列表查询")
    @PostMapping("/qryPlanConfList")
    public ActionResponse qryPlanConfList(@RequestBody PlanConfListQuery query) {
        log.info("产品配置列表查询入参对象：{}", JSONUtil.toJsonStr(query));
        Page<McdCoordinatePlanModel> mcdCoordinatePlanModelPage = mcdCoordinatePlanService.qryPlanConfList(query);
        return ActionResponse.getSuccessResp(mcdCoordinatePlanModelPage);
    }

    @ApiOperation(value = "查询该产品系列下所有已配置了优先级的产品(新增操作不分页)", notes = "新增操作--查询该产品系列下所有已配置了优先级的产品(不分页)")
    @PostMapping("/qryPlanConfListByPlanTypeOnSave")
    public ActionResponse qryPlanConfListByPlanTypeOnSave(@RequestBody @Validated PlanConfListQuery query) {
        log.info("新增操作--查询该产品系列下所有已配置了优先级的产品入参(不分页)：{}", JSONUtil.toJsonStr(query));
        List<McdCoordinatePlanModel> mcdCoordinatePlanModelList = mcdCoordinatePlanService.qryPlanConfListByPlanTypeOnSave(query);
        return ActionResponse.getSuccessResp(mcdCoordinatePlanModelList);
    }

    @ApiOperation(value = "查询该产品系列下所有已配置了优先级的产品(修改操作分页)", notes = "修改操作--查询该产品系列下所有已配置了优先级的产品(分页处理)")
    @PostMapping("/qryPlanConfListByPlanTypeOnUpdate")
    public ActionResponse qryPlanConfListByPlanTypeOnUpdate(@RequestBody @Validated PlanConfListQuery query) {
        log.info("修改操作--查询该产品系列下所有已配置了优先级的产品入参(分页处理)：{}", JSONUtil.toJsonStr(query));
        Page<McdCoordinatePlanModel> mcdCoordinatePlanModelPage = mcdCoordinatePlanService.qryPlanConfListByPlanType(query);
        return ActionResponse.getSuccessResp(mcdCoordinatePlanModelPage);
    }

    @ApiOperation(value = "展示未配置产品系列以及产品优先级的产品", notes = "新增操作--展示未配置产品系列以及产品优先级的产品")
    @PostMapping("/qryNotCfgPlanTypeAndPri")
    public ActionResponse qryNotCfgPlanTypeAndPri(@RequestBody PlanConfListQuery query) {
        log.info("展示未配置产品系列以及产品优先级的产品入参对象：{}", JSONUtil.toJsonStr(query));
        Page<McdPlanDef> mcdCoordinatePlanModelPage = mcdCoordinatePlanService.qryNotCfgPlanTypeAndPri(query);
        return ActionResponse.getSuccessResp(mcdCoordinatePlanModelPage);
    }

    @ApiOperation(value = "产品配置新增")
    @PostMapping("/savePlanConf")
    public ActionResponse savePlanConf(@RequestBody List<McdCoordinatePlanModel> mcdCoordinatePlanModelList, HttpServletRequest request) {
        log.info("产品配置新增入参对象：{}", JSONUtil.toJsonStr(mcdCoordinatePlanModelList));
        boolean save = false;
        boolean update = false;
        UserSimpleInfo user = UserUtil.getUser(request);
        // 需要更新的集合
        List<McdCoordinatePlanVo> updateList = new ArrayList<>();
        // 需要新增的集合
        List<McdCoordinatePlanVo> saveList = new ArrayList<>();
        buildData2Db(mcdCoordinatePlanModelList, user, updateList, saveList);
        log.info("产品配置新增saveList={}，更新updateList={}", JSONUtil.toJsonStr(saveList), JSONUtil.toJsonStr(updateList));
        if (CollectionUtil.isNotEmpty(saveList)) {
            save = mcdCoordinatePlanService.saveBatch(saveList);
        }
        if (CollectionUtil.isNotEmpty(updateList)) {
            update = mcdCoordinatePlanService.updateBatchById(updateList);
        }
        if (save || update) {
            return ActionResponse.getSuccessResp("新增或更新成功");
        } else {
            return ActionResponse.getFaildResp("新增或更新失败");
        }
    }

    @ApiOperation(value = "产品配置修改优先级")
    @PostMapping("/updatePriority")
    public ActionResponse updatePriority(@RequestBody UpdatePriorityQuery query) {
        log.info("产品配置修改优先级入参对象：{}", JSONUtil.toJsonStr(query));
        LambdaUpdateWrapper<McdCoordinatePlanVo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(McdCoordinatePlanVo::getPriority, query.getPriority()).eq(McdCoordinatePlanVo::getPlanId, query.getPlanId());
        boolean update = mcdCoordinatePlanService.update(wrapper);
        // List<McdCoordinatePlanVo> updateList = new ArrayList<>();
        // query.forEach(queryVo ->{
        //     McdCoordinatePlanVo model = new McdCoordinatePlanVo();
        //     model.setPlanId(queryVo.getPlanId());
        //     model.setPriority(queryVo.getPriority());
        //     updateList.add(model);
        // });
        // boolean update = mcdCoordinatePlanService.updateBatchById(updateList);
        if (update) {
            return ActionResponse.getSuccessResp("修改优先级成功");
        } else {
            return ActionResponse.getFaildResp("修改优先级失败");
        }
    }

    @ApiOperation(value = "产品配置删除")
    @PostMapping("/delPlanConfList")
    public ActionResponse delPlanConfList(@RequestBody McdIdQuery query) {
        log.info("产品配置删除入参对象：{}", JSONUtil.toJsonStr(query));
        boolean remove = mcdCoordinatePlanService.removeById(query.getId());
        if (remove) {
            return ActionResponse.getSuccessResp("删除成功");
        } else {
            return ActionResponse.getFaildResp("删除失败");
        }
    }

    /**
     * 构建新增或更新的数据
     *
     * @param mcdCoordinatePlanModelList mcdCoordinatePlanModelList
     * @param user                       用户
     * @param updateList                 更新数据集合
     * @param saveList                   保存数据集合
     */
    private void buildData2Db(@RequestBody List<McdCoordinatePlanModel> mcdCoordinatePlanModelList, UserSimpleInfo user, List<McdCoordinatePlanVo> updateList, List<McdCoordinatePlanVo> saveList) {
        mcdCoordinatePlanModelList.forEach(list -> {
            // 新增的系列产品信息
            if (list.isAdd()) {
                McdCoordinatePlanVo model = new McdCoordinatePlanVo();
                BeanUtil.copyProperties(list, model);
                model.setOperator(user.getUserName());
                saveList.add(model);
            } else {
                // 原有的系列产品需更新优先级
                McdCoordinatePlanVo model = new McdCoordinatePlanVo();
                BeanUtil.copyProperties(list, model);
                model.setOperator(user.getUserName());
                model.setUpdateTime(DateUtil.parse(DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                updateList.add(model);
            }
        });
    }

}

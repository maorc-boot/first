package com.asiainfo.biapp.pec.plan.jx.channelconfig.controller;


import cn.hutool.core.io.FileUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.model.McdChannelPromptInfoModel;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.request.McdDownloadChanPromptFileReq;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.service.McdChannelPromptInfoService;
import com.asiainfo.biapp.pec.plan.jx.channelconfig.vo.McdChanPromptQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mcd/chan/prompt")
@Api(tags = "江西渠道文字提示和附件配置")
public class McdChannelPromptInfoController {

    public static final String LOCAL_PATH = "MCD_CHAN_PROMPT_FILE_LOCAL_PATH";
    public static final String SFTP_IP = "MCD_MATERIAL_SFTP_IP";
    public static final String SFTP_PASSWORD = "MCD_MATERIAL_SFTP_PASSWORD";
    public static final String SFTP_PATH = "MCD_MATERIAL_SFTP_PATH";
    public static final String SFTP_PORT = "MCD_MATERIAL_SFTP_PORT";
    public static final String SFTP_USERNAME = "MCD_MATERIAL_SFTP_USERNAME";


    @Resource
    private McdChannelPromptInfoService mcdChannelPromptInfoService;


    @PostMapping("/queryChannelPrompt")
    @ApiOperation("渠道文字提示查询接口")
    public ActionResponse<IPage<McdChannelPromptInfoModel>> queryChannelPrompt(@RequestBody McdChanPromptQuery req){

        LambdaQueryWrapper<McdChannelPromptInfoModel>  promptWrapper = new LambdaQueryWrapper<>();
        promptWrapper.eq(StringUtils.isNotEmpty(req.getChannelId()),McdChannelPromptInfoModel::getChannelId,req.getChannelId())
                     .like(StringUtils.isNotEmpty(req.getKeyWords()),McdChannelPromptInfoModel::getFileName,req.getKeyWords());
        Page<McdChannelPromptInfoModel> page = new Page<>();
        page.setCurrent(req.getPageNum());
        page.setSize(req.getPageSize());
        Page<McdChannelPromptInfoModel> channelPromptInfoModelPage = mcdChannelPromptInfoService.page(page, promptWrapper);

        return ActionResponse.getSuccessResp(channelPromptInfoModelPage);

    }


    @PostMapping("/saveOrUpdateChannelPrompt")
    @ApiOperation("渠道文件提示及附件配置保存或修改")
    public ActionResponse saveOrUpdateChannelPrompt(@RequestBody McdChannelPromptInfoModel model, HttpServletRequest request){

        UserSimpleInfo user = UserUtil.getUser(request);
        model.setCreateUserId(user.getUserId());
        boolean flag = mcdChannelPromptInfoService.saveOrUpdate(model);

        return ActionResponse.getSuccessResp(flag);
    }


    @PostMapping("/deleteChannelPrompt")
    @ApiOperation("删除渠道文字提示及附件配置信息")
    public ActionResponse deleteChannelPrompt(@RequestBody McdChanPromptQuery req){

        if (StringUtils.isEmpty(req.getChannelId())){
            return ActionResponse.getFaildResp("渠道ID为空了");
        }

        boolean flag = mcdChannelPromptInfoService.removeById(req.getChannelId());
        return ActionResponse.getSuccessResp(flag);

    }


    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link ActionResponse}
     */
    @ApiOperation("渠道文字提示及附件配置上传附件接口")
    @PostMapping("/uploaChanPromptFile")
    public ActionResponse uploaChanPromptFile(@RequestParam("file") MultipartFile file) {
        String uploadLocalPath = RedisUtils.getDicValue(LOCAL_PATH);

        Map<String, String> resMap = new HashMap<>();
        try {
            // 获取文件的名称
            String filename = file.getOriginalFilename();
            if (!uploadLocalPath.endsWith(File.separator)) {
                uploadLocalPath += File.separator;
            }
            // 配置的目录路径不存在则创建目录
            File f = new File(uploadLocalPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            String rootFilePath = uploadLocalPath + filename;
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
            resMap.put("fileUrl", uploadLocalPath + filename);
            resMap.put("fileName", filename);

            File dest = new File(rootFilePath);
            if (!dest.exists()) {
                dest.createNewFile();
            }

            file.transferTo(dest);
            String sftpDir = RedisUtils.getDicValue(SFTP_PATH);
            upLoadFileToSftp(sftpDir,filename,uploadLocalPath);
            log.info("jx uploaChanPromptFile upload success!!!");

        } catch (IOException e) {
            log.error("文件上传异常：", e);
            return ActionResponse.getFaildResp("文件上传异常");
        }
        return ActionResponse.getSuccessResp(resMap);
    }

    /**
     * sftp 上传文件
     *
     * @param sftpDir
     * @param fileName
     * @param filePath
     */
    private boolean upLoadFileToSftp(String sftpDir,String fileName,String filePath) {
        boolean upload = false;

        String sftpServer = RedisUtils.getDicValue(SFTP_IP);
        int sftpPort = Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
        String username = RedisUtils.getDicValue(SFTP_USERNAME);
        String password = RedisUtils.getDicValue(SFTP_PASSWORD);

        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            sftp = sftpUtils.connect(sftpServer, sftpPort, username, password);
            log.info("连接sftp服务器");
            upload = sftpUtils.upload(sftpDir, fileName, filePath, sftp);
            log.info("文件上传sftp服务器: sftpDir:{},fileName:{},filePath{}",sftpDir,fileName,filePath);
        } catch (Exception e) {
            log.error("上传sftp异常", e);
        } finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常",e);
                }
            }
        }
        return upload;
    }

    @ApiOperation("渠道文字提示及附件下载接口")
    @PostMapping("/downloadChanPromptFile")
    public Object downloadChanPromptFile(@RequestBody McdDownloadChanPromptFileReq req,HttpServletResponse response) throws Exception {
        String fullFileName = req.getFileUrlAndName();
        if (StringUtils.isEmpty(fullFileName)){
            log.error("渠道文字提示及附件下载全路径入参为空!");
            return null;
        }

        log.info("开始下载渠道文字提示及附件,名称"+ fullFileName);
        String fileName  = fullFileName.substring(fullFileName.lastIndexOf("/")+1);

        fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
        log.info("fileName::{}",fileName);
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        log.info("fileName::{}",fileName);
        fileName = fileName.replaceAll("%2B", "+");
        log.info("fileName::{}",fileName);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        String  fileNameNoEncode = java.net.URLDecoder.decode(fileName, "UTF-8");
        log.info("fileName::{}",fileNameNoEncode);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        byte[] bytes = new byte[2048];
        try {
            String localDirectory = RedisUtils.getDicValue(LOCAL_PATH);
            if (!localDirectory.endsWith(File.separator)) {
                localDirectory = localDirectory + File.separator;
            }
            File file = new File(localDirectory + fileNameNoEncode);
            if (!file.exists()) {
                // 下载文件到本地路径
                this.downFile(localDirectory, fullFileName);
                // 以下载到本地路径为准，生成文件并放入流转中输出
                file = new File(localDirectory + fileNameNoEncode);
            }
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            log.error("渠道文字提示及附件下载异常",e);
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
        return null;
    }


    /**
     *  预览文件下载到本地服务器
     * @param resourceUrl
     */
    private void downFile(String sftpLocalPath, String resourceUrl) {
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            String sftpServerIp = RedisUtils.getDicValue(SFTP_IP);
            int sftpServerPort =
                    Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
            String sftpUserName = RedisUtils.getDicValue(SFTP_USERNAME);
            String sftpUserPwd = RedisUtils.getDicValue(SFTP_PASSWORD);
            String sftpPath = RedisUtils.getDicValue(SFTP_PATH);
            // String sftpLocalPath =RedisUtils.getDicValue(LOCAL_PATH);

            String fileName = resourceUrl.split("/")[resourceUrl.split("/").length - 1];

            sftp = sftpUtils.connect(sftpServerIp, sftpServerPort, sftpUserName, sftpUserPwd);
            log.info("1.sftp登录成功！ip=" + sftpServerIp + ";port=" + sftpServerPort+
                            ";userName="+ sftpUserName + ",sftpPath："+ sftpPath+
                            ",sftpLocalPath:" + sftpLocalPath);
            String sftpPathDest = sftpPath.substring(0, sftpPath.lastIndexOf("/") + 1);
            log.info("文件上传的sftp路径={}", sftpPathDest);
            sftpUtils.download(sftpPathDest, fileName, sftpLocalPath, sftp);
            log.info("2.下载成功！fileName=" + fileName);
        } catch (Exception e) {
            log.error("下载文件到本地目录出错：" + e);
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
}

package com.asiainfo.biapp.pec.element.jx.service.impl;

import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.element.jx.service.UploadService;
import com.asiainfo.biapp.pec.element.jx.vo.UploadFileResult;
import com.asiainfo.biapp.pec.element.util.CommonUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author mamp
 * @date 2023/1/3
 */
@Service
@Slf4j
@RefreshScope
public class UploadServiceImpl implements UploadService {

    @Value("${mcd.common.attachment.host:10.19.92.24}")
    private String sftpIp;
    @Value("${mcd.common.attachment.port:22022}")
    private int sftpPort;
    @Value("${mcd.common.attachment.user:iop-dev}")
    private String sftpUserName;
    @Value("${mcd.common.attachment.password:Iop@)@#)!202301}")
    private String sftpPassword;
    @Value("${mcd.common.attachment.serverPath:/home/iop-dev/upload/server}")
    private String sftpServerPath;
    @Value("${mcd.common.attachment.localPath:D:}")
    private String localPath;
    @Value("${file.upload.maxSize:30.0}")
    private double maxSize;

    /**
     * 上传文件
     *
     * @param file    文件
     * @param request
     * @return
     */
    @Override
    public UploadFileResult uploadFile(MultipartFile file, HttpServletRequest request) {

        UploadFileResult result = new UploadFileResult();
        if (file.isEmpty()) {
            result.setFileName(null);
            result.setStatus(0);
            result.setErrorMsg("文件为空");
            return result;
        }
        double fileSize = file.getSize() / 1024.0 / 1024.0;
        // 校验文件大小
        if (fileSize > maxSize) {
            result.setStatus(0);
            result.setErrorMsg("文件不能大于" + maxSize + "M");
            return result;
        }
        try {
            final String originalFilename = file.getOriginalFilename();
            log.info("originalFilename = " + originalFilename);

            String uploadFilePath = localPath;

            String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + "_"
                    + CommonUtil.generateId().substring(12) + "."
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String osName = System.getProperty("os.name");

            String filePath = null;
            if (osName.toLowerCase().startsWith("win")) {
                filePath = uploadFilePath.replace("/", File.separator).replace("\\", File.separator);
            } else {
                filePath = uploadFilePath;
            }
            // 配置的目录路径不存在则创建目录
            File f = new File(filePath);
            if (!f.exists()) {
                f.mkdirs();
            }
            String wholeFilePath = CommonUtil.addSlash(filePath) + fileName;
            log.info("wholeFilePath" + wholeFilePath);
            log.info("start to upload file：{}", wholeFilePath);
            File dest = new File(wholeFilePath);
            if (!dest.exists()) {
                dest.createNewFile();
            }
            file.transferTo(dest);
            String sftpDir = sftpServerPath;
            upLoadFileToSftp(sftpDir, fileName, filePath);
            result.setFileName(sftpServerPath + "/" + fileName);
            result.setStatus(1);
            result.setErrorMsg("");
        } catch (Exception e) {
            result.setFileName("");
            result.setStatus(0);
            result.setErrorMsg("上传失败");
            log.error("上传失败", e);
        }
        return result;
    }


    /**
     * sftp 上传文件
     *
     * @param sftpDir
     * @param fileName
     * @param filePath
     */
    private boolean upLoadFileToSftp(String sftpDir, String fileName, String filePath) {
        boolean upload = false;

        String sftpServer = sftpIp;
        String username = sftpUserName;
        String password = sftpPassword;

        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            sftp = sftpUtils.connect(sftpServer, sftpPort, username, password);
            log.info("连接sftp服务器");
            upload = sftpUtils.upload(sftpDir, fileName, filePath, sftp);
            log.info("文件上传sftp服务器: sftpDir:{},fileName:{},filePath{}", sftpDir, fileName, filePath);
        } catch (Exception e) {
            log.error("上传sftp异常", e);
        } finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常", e);
                }
            }
        }
        return upload;
    }
}

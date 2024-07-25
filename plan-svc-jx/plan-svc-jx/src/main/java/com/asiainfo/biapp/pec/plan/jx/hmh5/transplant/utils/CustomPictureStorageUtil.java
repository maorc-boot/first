package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils;

import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RefreshScope
@Component
@Slf4j
public class CustomPictureStorageUtil {
    @Value("${mcd.common.attachment.host}")
    private String sftpIp;
    @Value("${mcd.common.attachment.port}")
    private Integer sftpPort;
    @Value("${mcd.common.attachment.user}")
    private String sftpUsername;
    @Value("${mcd.common.attachment.password}")
    private String sftpPassword;
    @Value("${mcd.common.attachment.localPath}")
    private String localPath;
    @Value("${mcd.common.attachment.serverPath}")
    private String sftpPath;
    @Value("${mcd.common.attachment.maxSize}")
    private Double limitSize;
    public static final String UPLOADRESULT = "uploadResultFileName";
    public static final String STATUS = "status";
    public static final String ERROR = "error";

    //检查ftp配置是否正确
    public Boolean checkSftpProperties() {
        if ((StringUtils.isBlank(sftpIp) || !sftpIp.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")))
            return false;
        if (sftpPort == null || sftpPort < 1024 || sftpPort > 65535)
            return false;
        if (StringUtils.isBlank(sftpUsername) || StringUtils.isBlank(sftpPassword))
            return false;

        //windows系统的话就不检查本地路径
        String osName = System.getProperty("os.name");
        if (!osName.toLowerCase().startsWith("win")) {
            if (StringUtils.isBlank(localPath) || !localPath.startsWith("/"))   //不考虑文件分隔符为 \ 的情况
                return false;
        }

        if (StringUtils.isBlank(sftpPath) || !sftpPath.startsWith("/"))
            return false;
        if (limitSize == null || limitSize <= 0)
            return false;
        return true;
    }

    //将多个文件路径字符串连接起来
    public String unionFilePath(String... paths) {
        if (paths == null || paths.length == 0) return null;
        StringBuilder unionStr = new StringBuilder();
        String tmp;
        for (int i = 0; i < paths.length; i++) {
            tmp = paths[i];
            if (StringUtils.isNotBlank(tmp)) {
                if (tmp.startsWith("/")) tmp = tmp.substring(1);
                if (!tmp.endsWith("/")) tmp += "/";
                unionStr.append(tmp);
            }
        }
        return "/" + unionStr;
    }

    /**
     * @param lastFolder: 文件保存的上一级目录
     * @param file:       文件
     * @return Map<String, String>
     * @author 11437
     * @description 上传文件到本地服务器
     * @date 2023/6/1 20:13
     */
    public Map<String, String> uploadImage(String lastFolder, MultipartFile file) {
        return uploadImage(lastFolder, null, file);
    }

    public Map<String, String> uploadImage(String lastFolder, String suffix, MultipartFile file) {
        Map<String, String> map = new HashMap<String, String>() {{
            put(STATUS, "0");
        }};
        String fileName = file.getOriginalFilename().toLowerCase();

        if (!checkSftpProperties()) {    //如果配置出现了改动，会存在这种情况
            map.put(ERROR, "没有获取到配置信息，请联系管理员！");
        } else if (fileName.length() > 32) {
            map.put(ERROR, fileName + "名称过长，请不要超过32位");
        } else if (file.isEmpty()) {
            map.put(ERROR, fileName + "文件为空");
        } else if ((file.getSize() / 1024.0 / 1024.0) > limitSize) {
            map.put(ERROR, fileName + "文件不能大于" + limitSize + "M!");
        } else if (!fileName.matches(".*(\\.jpg|\\.png|\\.jpeg)$")) {   //检查格式是否匹配
            map.put(ERROR, fileName + "文件名格式不匹配！");
        }
        if (map.containsKey(ERROR)) return map;

        //生成下一级路径
        String localDir = unionFilePath(localPath, lastFolder);

        //生成新的文件名
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        String id = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String fileNewName = id + (StringUtils.isNotBlank(suffix) ? suffix : "") + fileType;

        //文件路径不存在则创建
        File targetFile = new File(localDir, fileNewName);
        if (!targetFile.getParentFile().exists()) targetFile.getParentFile().mkdirs();

        //上传本地
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            map.put(ERROR, "图片上传到本地失败，请检查路径配置！");
            log.error("图片上传到本地失败：" + e);
            return map;
        }
        //没有出错就上传ftp
        boolean toSftpResult = upLoadFileToSftp(unionFilePath(sftpPath, lastFolder), localDir, fileNewName);
        if (toSftpResult) {
            map.put(STATUS, "1");
            map.put(UPLOADRESULT, targetFile.getName());
        } else {
            map.put(ERROR, "上传sftp失败");
        }
        return map;
    }

    private boolean upLoadFileToSftp(String sftpDir, String filePath, String fileName) {
        boolean upload = false;
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            sftp = sftpUtils.connect(sftpIp, sftpPort, sftpUsername, sftpPassword);
            log.info("连接sftp服务器");

            //因sftp的mkdir方法不能一次创建多级目录，故采用这种方法
            String[] dirs = sftpDir.replaceFirst("/", "").split(",");
            String tmp = "";
            for (int i = 0; i < dirs.length; i++) {
                tmp += "/" + dirs[i];
                try {
                    sftp.cd(tmp);
                } catch (Exception e) {
                    sftp.mkdir(tmp);
                }
            }
            //上传文件到sftp服务器
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

    /**
     * @param lastFolder:  文件的上级目录
     * @param pictureName: 文件名称
     * @param response:
     * @return void
     * @author 11437
     * @description 下载文件
     * @date 2023/6/1 20:13
     */
    public void downloadImage(String lastFolder, String pictureName, HttpServletResponse response) {
        String resourceUrl = unionFilePath(localPath, lastFolder) + pictureName;
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            response.reset();   //清空下载文件的空白行（空白行是因为有的前端代码编译后产生的）
            response.setContentType("image/jpg");
            response.setHeader("Content-disposition", "inline");

            File targetFile = new File(resourceUrl);
            if (!targetFile.exists()) this.downFile(lastFolder, pictureName);   //本地不存在图片则下载到本地
            stream.write(FileUtils.readFileToByteArray(targetFile));    //输出流开始写出文件
            stream.flush(); //刷新流
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {   //关闭流
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 预览文件下载到本地
     */
    private void downFile(String lastFolder, String fileName) {
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            sftp = sftpUtils.connect(sftpIp, sftpPort, sftpUsername, sftpPassword);
            String ftpDir = unionFilePath(sftpPath, lastFolder);
            String localDir = unionFilePath(localPath, lastFolder);
            log.info("1.sftp登录成功！ip=" + sftpIp + ";port=" + sftpPort +
                    ";userName=" + sftpUsername + ",sftpPath：" + ftpDir +
                    ",LocalPath:" + localDir);

            sftpUtils.download(ftpDir, fileName, localDir, sftp);
            log.info("2.下载成功！文件保存在：{}", localDir + fileName);
        } catch (Exception e) {
            log.error("下载素材到本地目录出错：" + e);
        } finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常", e);
                }
            }
        }
    }


    public Map<String, String> deleteImage(String lastFolder, String pictureName) {
        Map<String, String> map = new HashMap<String, String>() {{
            put(STATUS, "0");
        }};
        if (!checkSftpProperties()) {    //如果配置出现了改动，会存在这种情况
            map.put(ERROR, "没有获取到配置信息，请联系管理员！");
        } else if (!pictureName.matches(".*(\\.jpg|\\.png|\\.jpeg)$")) {   //检查格式是否匹配
            map.put(ERROR, "文件：{" + pictureName + "}删除失败，不是有效的图片文件格式！");
        }
        if (map.containsKey(ERROR)) return map;

        String fileName = unionFilePath(localPath, lastFolder) + pictureName;
        File file = new File(fileName);
        //本地有的话先删除
        if (file.exists()) file.delete();

        //从stp服务器删除
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            sftp = sftpUtils.connect(sftpIp, sftpPort, sftpUsername, sftpPassword);
            sftp.rm(unionFilePath(sftpPath, lastFolder) + pictureName);
        } catch (Exception e) {
            map.put(ERROR, e.getMessage());
            return map;
        } finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常", e);
                }
            }
        }
        map.put(STATUS, "1");
        return map;
    }

}

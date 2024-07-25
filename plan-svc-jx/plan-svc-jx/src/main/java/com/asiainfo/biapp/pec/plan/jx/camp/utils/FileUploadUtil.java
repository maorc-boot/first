package com.asiainfo.biapp.pec.plan.jx.camp.utils;

import cn.hutool.core.io.FileUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 文件上传类
 *
 * @author: lvchaochao
 * @date: 2022/12/30
 */
@Slf4j
@RefreshScope
@Component
public class FileUploadUtil {

    @Value("${uploadfile.serverPath:D:\\}")
    private String serverPath;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link ActionResponse}
     */
    public ActionResponse uploadFile(MultipartFile file) {
        Map<String, String> resMap = new HashMap<>();
        try {
            // 获取文件的名称
            String filename = file.getOriginalFilename();
            if (!serverPath.endsWith(File.separator)) {
                serverPath += File.separator;
            }
            String rootFilePath = serverPath + filename;
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
            resMap.put("url", serverPath + filename);
            resMap.put("fileName", filename);
            log.info("jx file upload success!!!");
        } catch (IOException e) {
            log.error("文件上传异常：", e);
            return ActionResponse.getFaildResp("文件上传异常");
        }
        return ActionResponse.getSuccessResp(resMap);
    }
}


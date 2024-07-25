package com.asiainfo.biapp.pec.element.jx.service;

import com.asiainfo.biapp.pec.element.jx.vo.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mamp
 * @date 2023/1/3
 */
public interface UploadService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return
     */
    UploadFileResult uploadFile(MultipartFile file, HttpServletRequest request);
}

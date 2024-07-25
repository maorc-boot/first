package com.asiainfo.biapp.pec.element.jx.controller;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.element.jx.service.UploadService;
import com.asiainfo.biapp.pec.element.jx.vo.UploadFileResult;
import com.asiainfo.biapp.pec.element.service.IMcdDimMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mamp
 * @date 2023/1/3
 */

@RestController
@RequestMapping("/api/jx/file")
@Api(value = "江西:文件上传服务", tags = {"江西:文件上传服务"})
@Slf4j
public class UploadController {


    @Resource
    private IMcdDimMaterialService materialService;

    @Resource
    private UploadService uploadService;

    /**
     * 上传文件(素材附件等)
     *
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传 : sftp", notes = "文件上传服务 : sftp")
    @PostMapping(path = "/upload")
    public ActionResponse<UploadFileResult> uploadMaterialPictureBySftp(HttpServletRequest request,
                                                                        @RequestParam(value = "file") MultipartFile file) {
        log.info("开始上传文件:{}", file.getName());
        UploadFileResult result = uploadService.uploadFile(file, request);
        if (0 == result.getStatus()) {
            return ActionResponse.getFaildResp(result.getErrorMsg());
        } else if (1 == result.getStatus()) {
            return ActionResponse.getSuccessResp(result);
        } else {
            return ActionResponse.getFaildResp("上传异常");
        }
    }
}

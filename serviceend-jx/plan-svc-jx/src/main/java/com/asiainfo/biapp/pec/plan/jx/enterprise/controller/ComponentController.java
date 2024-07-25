package com.asiainfo.biapp.pec.plan.jx.enterprise.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mamp
 * @date 2022/8/5
 */
@Api(tags = "江西-政企:上传活动图片")
@RestController
@RequestMapping("/api")
public class ComponentController {

    /**
     * 1.10 上传活动图片接口
     *
     * @param multiFile
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传活动图片接口", notes = "上传活动图片接口")
    @RequestMapping("/component/fileUpload/upload")
    public Object queryApprove(@RequestParam(value = "subitemId", required = false) MultipartFile[] multiFile,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", "0");
        result.put("resultInfo", "OK");
        result.put("data", new ArrayList<>());
        return result;
    }

}

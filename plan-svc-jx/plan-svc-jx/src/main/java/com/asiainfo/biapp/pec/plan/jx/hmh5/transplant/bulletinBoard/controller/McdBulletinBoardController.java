package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller;


import com.asiainfo.biapp.pec.common.jx.util.UserUtilJx;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.ModifyBulletinParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.PublishBulletinParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.SelectBulletinBoardParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.service.McdBulletinBoardService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 江西客户通公告栏表 前端控制器
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@RestController
@RequestMapping("/mcdBulletinBoard")
@Slf4j
@Validated
@Api(tags = "客户通公告栏相关")
@DataSource("khtmanageusedb")
public class McdBulletinBoardController {

    @Autowired
    private McdBulletinBoardService bulletinService;

    @ApiOperation("根据条件查询公告")
    @GetMapping
    public ActionResponse selectAllBulletins(SelectBulletinBoardParam bulletinParam) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("userId：{}的用户正在查询公告，请求参数为：{}", userId, bulletinParam);
        Page bulletins = bulletinService.selectAllBulletins(bulletinParam);
        return ActionResponse.getSuccessResp(bulletins);
    }

    @ApiOperation("发布公告操作")
    @PostMapping
    public ActionResponse publishBulletin(@RequestBody @Validated PublishBulletinParam publishParam) {
        UserSimpleInfo user = UserUtilJx.getUser();
        log.info("userId：{}的用户正在发布公告！", user.getUserId());
        bulletinService.publishBulletin(user, publishParam);
        return ActionResponse.getSuccessResp("公告发布成功！");
    }

    @ApiOperation("修改公告操作")
    @PutMapping
    public ActionResponse modifyBulletin(@RequestBody @Validated ModifyBulletinParam modifyParam) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("userId：{}的用户正在更新公告，公告id：{}！", userId, modifyParam.getBulletinCode());
        bulletinService.modifyBulletin(userId, modifyParam);
        return ActionResponse.getSuccessResp("公告更新成功！");
    }

    @ApiOperation("公告上下线操作")
    @ApiImplicitParam(name = "bulletinCode", value = "公告的bulletinCode", required = true)
    @PutMapping("/modifyBulletinStatus")
    public ActionResponse modifyBulletinStatus(
            @RequestParam("bulletinCode") @NotBlank(message = "公告bulletinCode不能为空！") String bulletinCode
    ) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("userId：{}的用户正在更新公告的状态，公告id：{}！", userId, bulletinCode);
        bulletinService.modifyBulletinStatus(userId, bulletinCode);
        return ActionResponse.getSuccessResp("公告状态更新成功！");
    }

    @ApiOperation("根据bulletinCode删除公告")
    @ApiImplicitParam(name = "bulletinCode", value = "要删除公告的bulletinCode", required = true)
    @DeleteMapping
    public ActionResponse deleteBulletin(
            @RequestParam("bulletinCode") @NotBlank(message = "bulletinCode至少从1开始！") String bulletinCode
    ) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("userId：{}的用户正在删除公告，公告id：{}！", userId, bulletinCode);
        bulletinService.deleteBulletin(userId, bulletinCode);
        return ActionResponse.getSuccessResp("公告删除成功！");
    }

    @ApiOperation("为公告上传图片")
    @ApiImplicitParam(name = "pictureFiles", value = "选择图片", required = true)
    @PostMapping("/uploadBulletinPictures")
    public ActionResponse uploadBulletinPictures(
            @RequestParam("pictureFiles")
            @Size(min = 1, max = 3, message = "最多同时上传3张图片，格式只能为png|jpg|jpeg！")
            MultipartFile[] pictureFiles
    ) throws Exception {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("userId：{}的用户正在为公告上传图片！", userId);
        List<Map<String, String>> pictureNames = bulletinService.uploadBulletinPictures(userId, pictureFiles);
        return ActionResponse.getSuccessResp(pictureNames);
    }

    @ApiOperation("根据图片名下载图片")
    @ApiImplicitParam(name = "pictureName", value = "图片名称，从公告查询列表获取", required = true)
    @GetMapping("/downloadImage/{pictureName}")
    public void downloadImage(@PathVariable String pictureName, HttpServletResponse response) throws Exception {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("用户id：{}正在下载图片：{}!", userId, pictureName);
        bulletinService.downloadImage(pictureName, response);
    }

    @ApiOperation("删除公告图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pictures", value = "删除的图片列表（以”,“分割），如果图片已经与发布的公告关联，则bulletinCode也需要填写", required = true),
            @ApiImplicitParam(name = "bulletinCode", value = "公告的bulletinCode"),
    })
    @DeleteMapping("/deleteImage")
    public ActionResponse deleteImage(
            @NotBlank(message = "公告bulletinCode不能为空！") String bulletinCode,
            @RequestParam @Size(min = 1, message = "请输入图片文件名！") List<String> pictures
    ) {
        String userId = UserUtilJx.getUser().getUserId();
        log.info("用户id：{}正在删除图片：{}!", userId, pictures.toString());
        bulletinService.deleteImage(userId, bulletinCode, pictures);
        return ActionResponse.getSuccessResp("图片删除成功！");
    }


}


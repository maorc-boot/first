package com.asiainfo.biapp.pec.element.jx.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.element.dto.request.DimChannelRequest;
import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelJx;
import com.asiainfo.biapp.pec.element.jx.query.DimChannelPageListRequestJx;
import com.asiainfo.biapp.pec.element.jx.service.IMcdDimChannelJxService;
import com.asiainfo.biapp.pec.element.jx.vo.DimChannelListRequestJx;
import com.asiainfo.biapp.pec.element.model.McdDimChannel;
import com.asiainfo.biapp.pec.element.service.IMcdDimChannelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 渠道表 前端控制器
 * </p>
 *
 * @author ranpf
 * @since 2022-12-13
 */

@RestController
@RequestMapping("/api/mcdDimChannel/jx")
@Api(value = "江西:渠道管理服务", tags = {"渠道管理服务"})
@Slf4j
public class McdDimChannelJxController {


    @Resource
    private IMcdDimChannelJxService mcdDimChannelJxService;

    @Resource
    private IMcdDimChannelService mcdDimChannelService;

    @Resource
    private HttpServletRequest request;

    @Value("${intellpushprism.supportai.channels}")
    private String supportaiChannels;

    /**
     * 江西新增或更新渠道(添加线上,线下渠道类型)
     *
     * @param mcdDimChannel 入参对象
     * @return true/false
     */
    @PostMapping(path = "/saveOrUpdateDimChannel")
    @ApiOperation(value = "江西:新增或更新渠道", notes = "江西:新增或更新渠道")
    public ActionResponse saveOrUpdateDimChannel(@RequestBody McdDimChannelJx mcdDimChannel) {
        log.info("start saveOrUpdateDimChanneljx param:{}", new JSONObject(mcdDimChannel));
        return ActionResponse.getSuccessResp(mcdDimChannelJxService.saveOrUpdate(mcdDimChannel));
    }

    /**
     * 渠道信息(或根据条件)列表分页查询
     */
    @PostMapping(path = "/pagelistDimChannel")
    @ApiOperation(value = "江西:渠道列表(或根据条件)分页查询", notes = "江西:渠道列表(或根据条件)分页查询")
    public ActionResponse<IPage<McdDimChannelJx>> pagelistDimChannel(@RequestBody DimChannelPageListRequestJx req) {
        log.info("start pagelistDimChannel param {}", new JSONObject(req));
        return ActionResponse.getSuccessResp(mcdDimChannelJxService.pagelistDimChannel(req));
    }


    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link ActionResponse}
     */
    @ApiOperation("渠道附件配置上传接口")
    @PostMapping("/uploaChanPromptFile")
    public ActionResponse uploaChanPromptFile(@RequestParam("file") MultipartFile file) {
        String serverPath = RedisUtils.getDicValue("MCD_CHAN_PROMPT_FILE_LOCAL_PATH");
        Map<String, String> resMap = new HashMap<>();
        try {
            // 获取文件的名称
            String filename = file.getOriginalFilename();
            if (!serverPath.endsWith(File.separator)) {
                serverPath += File.separator;
            }
            String rootFilePath = serverPath + filename;
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
            resMap.put("fileUrl", serverPath + filename);
            resMap.put("fileName", filename);
            log.info("jx uploaChanPromptFile upload success!!!");
        } catch (IOException e) {
            log.error("文件上传异常：", e);
            return ActionResponse.getFaildResp("文件上传异常");
        }
        return ActionResponse.getSuccessResp(resMap);
    }


    /**
     * 渠道查询文字提示信息查询查询
     */
    @PostMapping(path = "/queryDimChannelInfo")
    @ApiOperation(value = "江西:渠道查询文字提示信息查询", notes = "江西:渠道查询文字提示信息查询")
    public ActionResponse<McdDimChannelJx> queryDimChannelInfo(@RequestBody DimChannelRequest req) {
        log.info("start queryDimChannelInfo param {}", new JSONObject(req));
        if (StringUtils.isEmpty(req.getChannelId())) {
            log.error("渠道查询文字提示信息渠道ID入参为空!");
            return ActionResponse.getFaildResp("渠道ID入参为空!");
        }
        LambdaQueryWrapper<McdDimChannelJx> channelWrapper = new LambdaQueryWrapper<>();
        channelWrapper.eq(McdDimChannelJx::getChannelId, req.getChannelId());

        return ActionResponse.getSuccessResp(mcdDimChannelJxService.getOne(channelWrapper));
    }

    /**
     * 根据上线状态渠道信息列表查询
     */
    @PostMapping(path = "/listDimChannel")
    @ApiOperation(value = "江西:在线渠道列表查询", notes = "在线渠道列表查询")
    public ActionResponse<List<McdDimChannel>> getDimChannelList(
            @RequestBody DimChannelListRequestJx dimChannelListRequest) {
        Set<String> testUser = RedisUtils.getMembers("TEST_USER");
        String userid = UserUtil.getUserId(request);
        if (testUser.contains(userid)) {
            dimChannelListRequest.setOnlineStatus(null);
        }
        log.info("start getDimChannelList param {}", new JSONObject(dimChannelListRequest));
        List<String> channels = null;
        if (StrUtil.isEmpty(userid)) {
            channels = new ArrayList<>();
        } else {
            channels = mcdDimChannelJxService.listChannelByUserId(userid);
        }
       //  List<McdDimChannel> channelList = mcdDimChannelService.getDimChannelList(dimChannelListRequest);
        List<McdDimChannelJx> channelList = mcdDimChannelJxService.getDimChannelList(dimChannelListRequest);
        if (CollectionUtil.isNotEmpty(channels)) {
            List<String> finalChannels = channels;
            List<McdDimChannelJx> collect = channelList.stream().filter(chn -> finalChannels.contains(chn.getChannelId())).collect(Collectors.toList());
            // ActionResponse<List<McdDimChannel>> supportaiChannels = getAISupportChannels(dimChannelListRequest, collect);
            // if (supportaiChannels != null) return supportaiChannels;
            return ActionResponse.getSuccessResp(collect);
        }
        // ActionResponse<List<McdDimChannel>> supportaiChannels1 = getAISupportChannels(dimChannelListRequest, channelList);
        // if (supportaiChannels1 != null) return supportaiChannels1;
        return ActionResponse.getSuccessResp(channelList);
    }

    /**
     * 过滤保留支持AI推荐的渠道信息==>目前全部渠道开放AI 所以不需要过滤了
     *
     * @param dimChannelListRequest dimChannelListRequest
     * @param channelList channelList
     * @return {@link ActionResponse}<{@link List}<{@link McdDimChannel}>>
     */
    private ActionResponse<List<McdDimChannel>> getAISupportChannels(@RequestBody DimChannelListRequestJx dimChannelListRequest, List<McdDimChannelJx> channelList) {
        if (ObjectUtil.isNotEmpty(dimChannelListRequest.getIsSupportAI()) && 1 == dimChannelListRequest.getIsSupportAI()) {
            // 仅保留支持ai的渠道信息
            List<McdDimChannelJx> supportaiChannels = channelList.stream().filter(chn -> this.supportaiChannels.contains(chn.getChannelId())).collect(Collectors.toList());
            return ActionResponse.getSuccessResp(supportaiChannels);
        }
        return null;
    }

}

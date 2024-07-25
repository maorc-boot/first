package com.asiainfo.biapp.pec.plan.jx.specialuser.controller;


import cn.hutool.core.date.DatePattern;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.common.Assert;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.DateUtil;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.assembler.JxSpecialUserAssembler;
import com.asiainfo.biapp.pec.plan.jx.specialuser.model.JxMcdSpecialUse;
import com.asiainfo.biapp.pec.plan.jx.specialuser.service.JxSpecialUseService;
import com.asiainfo.biapp.pec.plan.vo.SpecialUser;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseMod;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseQuery;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseSave;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 特殊客户群表 前端控制器-江西
 * </p>
 *
 * @author imcd
 * @since 2023-03-30
 */
@Api(tags = "江西：特殊客户管理")
@Slf4j
@RestController
@RequestMapping("api/jx/action/specialuse")
public class JxMcdSpecialUseController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JxSpecialUseService jxSpecialUseService;

    @Resource
    private JxSpecialUserAssembler assembler;


    /**
     * 页面输入添加
     *
     * @param save
     * @return
     */
    @ApiOperation(value = "江西：添加特殊用户信息", notes = "江西：添加特殊用户信息")
    @PostMapping("save")
    public ActionResponse saveSpecialUser(@RequestBody @Valid SpecialUseSave save) {
        //base64解码参数
        save.setPhoneNos(new String(Base64.getDecoder().decode(save.getPhoneNos())));
        final Map<String, List<String>> phoneListMap = assembler.convertPhone(save.getPhoneNos());
        final List<String> phoneList = phoneListMap.get("1");
        //未保存号码
        final List<String> notPhone = phoneListMap.get("0");
        if (CollectionUtils.isEmpty(phoneList)) {
            return ActionResponse.getFaildResp("没有需要保存的号码，未保存号码[" + String.join(",", notPhone) + "]不合规");
        }
        if (save.getStartTime() == null) {
            save.setStartTime(new Date());
        }
        UserSimpleInfo user = UserUtil.getUser(request);
        final List<JxMcdSpecialUse> mcdSpecialUses = assembler.convertToModels(save, phoneList, user);
        jxSpecialUseService.saveBatchSpecialUser(mcdSpecialUses);

        return ActionResponse.getSuccessResp("保存号码" + mcdSpecialUses.size() + "条，未保存号码[" + String.join(",", notPhone) + "]不合规");
    }

    /**
     * 页面导入添加
     *
     * @param multiFile
     * @param userType
     * @param channelId
     * @return
     */
    @ApiOperation(value = "江西：导入特殊用户信息", notes = "江西：导入特殊用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneFile", value = "导入文件"),
            @ApiImplicitParam(name = "userType", value = "用户类型"),
            @ApiImplicitParam(name = "channelId", value = "渠道"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")
    })
    @PostMapping("import")
    public ActionResponse importSpecialUser(@RequestParam(value = "phoneFile") MultipartFile multiFile,
                                            @RequestParam(value = "userType") String userType,
                                            @RequestParam(value = "channelId") String channelId,
                                            @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN) Date startTime,
                                            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN) Date endTime) {
        if (null == startTime) {
            startTime = new Date();
        }
        if (null == endTime) {
            endTime = DateUtil.addYear(new Date(), 1);
        }
        Assert.notNull(multiFile, "导入文件不能为空");
        Assert.isTrue(StringUtils.isNotBlank(multiFile.getOriginalFilename()), "导入文件的文件名为空");
        Assert.isTrue(multiFile.getOriginalFilename().endsWith(".csv") || multiFile.getOriginalFilename().endsWith(".txt"),
                "请使用正确的文件格式导入");
        UserSimpleInfo user = UserUtil.getUser(request);
        try {
            String msg = jxSpecialUseService.importSpecialUser(multiFile, userType, channelId, startTime, endTime, user);
            return ActionResponse.getSuccessResp(msg);
        } catch (Exception e) {
            log.error("导入文件失败:", e);
            return ActionResponse.getFaildResp(e.getMessage());
        }
    }


    /**
     * 删除
     *
     * @param del
     * @return
     */
    @ApiOperation(value = "江西：删除特殊用户信息", notes = "江西：删除特殊用户信息")
    @PostMapping("del")
    public ActionResponse delSpecialUser(@RequestBody @Valid SpecialUseSave del) {
        jxSpecialUseService.delSpecialUser(del);
        return ActionResponse.getSuccessResp();
    }


    /**
     * 修改---此处根据目前传参，最好的方式就是先删除在新增
     *
     * @param mod
     * @return
     */
    @ApiOperation(value = "江西：修改特殊用户信息", notes = "江西：修改特殊用户信息")
    @PostMapping("mod")
    public ActionResponse modSpecialUser(@RequestBody @Valid SpecialUseMod mod) {
        UserSimpleInfo user = UserUtil.getUser(request);
        try {
            jxSpecialUseService.modSpecialUser(mod, user);
        } catch (Exception e) {
            log.error("修改特殊用户信息失败:", e);
            return ActionResponse.getFaildResp(e.getMessage());
        }
        return ActionResponse.getSuccessResp();
    }


    /**
     * 查询
     *
     * @param specialUseQuery
     * @return
     */
    @ApiOperation(value = "江西：查询特殊用户列表", notes = "江西：查询特殊用户列表")
    @PostMapping("page")
    public IPage<SpecialUser> pageSpecialUser(@RequestBody SpecialUseQuery specialUseQuery) {
        //base64解码参数
        if (StringUtils.isNotBlank(specialUseQuery.getPhoneNo())) {
            specialUseQuery.setPhoneNo(new String(Base64.getDecoder().decode(specialUseQuery.getPhoneNo())));
        }
        return jxSpecialUseService.pageSpecialUser(specialUseQuery);
    }

}

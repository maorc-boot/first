package com.asiainfo.biapp.cmn.jx.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import com.asiainfo.biapp.cmn.jx.model.McdSysUser;
import com.asiainfo.biapp.cmn.jx.query.UserPageQueryJx;
import com.asiainfo.biapp.cmn.jx.service.McdSysUserService;
import com.asiainfo.biapp.cmn.jx.service.UserJxService;
import com.asiainfo.biapp.cmn.jx.util.HmacSHAEncoder;
import com.asiainfo.biapp.cmn.jx.vo.UserJxVO;
import com.asiainfo.biapp.cmn.model.User;
import com.asiainfo.biapp.cmn.model.UserRoleRelation;
import com.asiainfo.biapp.cmn.service.IUserRoleRelationService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mamp
 * @date 2022/12/12
 */
@Slf4j
@RestController
@RequestMapping("/api/jx/user")
@Api(tags = "江西:用户管理")
public class UserJxController {

    private static String DAYIN_APP_ID = "DAYIN_APP_ID";
    private static String DAYIN_APP_KEY = "DAYIN_APP_KEY";
    private static String HMACSHA256 = "HmacSHA256";

    @Autowired
    private UserJxService userJxService;

    @Autowired
    private McdSysUserService userService;

    @Autowired
    private IUserRoleRelationService iUserRoleRelationService;

    @Autowired
    private HttpServletResponse response;

    @PostMapping("/pagelist")
    @ApiOperation(value = "分页查询所有", notes = "可根据用户名称或账号模糊查询")
    public IPage<UserJxVO> pageList(@RequestBody UserPageQueryJx dto) {
        return userJxService.pageList(dto);
    }

    @PostMapping("/save")
    @ApiOperation(value = "添加", notes = "需要用户信息，不能为空")
    @Transactional
    public ActionResponse insert(@RequestBody McdSysUser dto, HttpServletRequest request) {
        log.info("添加用户:{}", dto);
        //1.检查参数
        if (dto == null) {
            return ActionResponse.getFaildResp("添加用户参数为空");
        }
        //如果传入了ID则判断ID是否已经存在
        List<McdSysUser> list = userService.list(Wrappers.<McdSysUser>lambdaQuery().eq(McdSysUser::getUserId, dto.getUserId()).eq(McdSysUser::getDelFlag, 0));
        if (list.size() > 0) {
            return ActionResponse.getFaildResp("检查到用户账号已存在,请重新尝试添加");
        }
        String userId = UserUtil.getUserId(request) == null ? "admin" : UserUtil.getUserId(request);
        dto.setCreateBy(userId);
        List<UserRoleRelation> urrList = new ArrayList<>();

        if (StrUtil.isNotEmpty(dto.getRoleIds())) {
            for (String s : dto.getRoleIds().split(",")) {
                UserRoleRelation roleRelation = new UserRoleRelation();
                roleRelation.setRoleId(Long.valueOf(s));
                roleRelation.setUserId(dto.getUserId());
                urrList.add(roleRelation);
            }
            iUserRoleRelationService.saveBatch(urrList);
        }
        // 密码加密
        String encode = new BCryptPasswordEncoder().encode(dto.getPwd() == null ? "123456" : dto.getPwd());
        dto.setPwd(encode);
        if (userService.save(dto)) {
            return ActionResponse.getSuccessResp("添加成功");
        }
        return ActionResponse.getFaildResp("添加失败");
    }

    @Transactional
    @ApiOperation(value = "修改", notes = "需要用户信息，不能为空")
    @PostMapping("/update")
    public ActionResponse update(@RequestBody McdSysUser dto, HttpServletRequest request) {
        if (StrUtil.isBlank(dto.getId())) {
            return ActionResponse.getFaildResp("参数异常");
        }
        String userId = UserUtil.getUserId(request) == null ? "admin" : UserUtil.getUserId(request);
        List<UserRoleRelation> urrList = new ArrayList<>();
        QueryWrapper<UserRoleRelation> wrapper = new QueryWrapper<>();
        // 先删除再插入
        wrapper.lambda().eq(UserRoleRelation::getUserId, dto.getUserId());
        iUserRoleRelationService.remove(wrapper);
        if (StrUtil.isNotEmpty(dto.getRoleIds())) {
            for (String s : dto.getRoleIds().split(",")) {
                UserRoleRelation roleRelation = new UserRoleRelation();
                roleRelation.setRoleId(Long.valueOf(s));
                roleRelation.setUserId(dto.getUserId());
                urrList.add(roleRelation);
            }
            iUserRoleRelationService.saveBatch(urrList);
        }
        //设置默认非空属性
        dto.setUpdateBy(userId);
        // 密码加密
        String encode = new BCryptPasswordEncoder().encode(dto.getPwd() == null ? "123456" : dto.getPwd());
        dto.setPwd(encode);
        if (userService.updateById(dto)) {
            return ActionResponse.getSuccessResp("修改成功");
        }
        return ActionResponse.getFaildResp("修改失败");
    }

    @ApiOperation(value = "根据用户账号查询", notes = "需要有用户userID")
    @PostMapping("/getByUserID")
    public ActionResponse<User> selectByUserID(@RequestBody McdIdQuery dto) {

        try {
            UserJxVO userJxVO = userJxService.getUserById(dto.getId());
            if (null == userJxVO) {
                return ActionResponse.getFaildResp(null);
            }
            //返回所有所属角色id
            List<UserRoleRelation> list = iUserRoleRelationService.list(Wrappers.<UserRoleRelation>lambdaQuery().eq(StrUtil.isNotBlank(userJxVO.getUserId()), UserRoleRelation::getUserId, userJxVO.getUserId()));
            //将所有角色ID填充进返回类，前端可以获得角色名
            userJxVO.setRoleIds(list.stream().map(t -> t.getRoleId().toString()).collect(Collectors.joining(",")));
            return ActionResponse.getSuccessResp(userJxVO);
        } catch (Exception e) {
            log.error("根据用户账号查询出错：" + e);
            throw new BaseException("根据用户账号查询出错");
        }
    }

    /**
     * 用户清单数据导出
     *
     * @param cityId
     * @param departmentId
     * @param startTime
     * @param endTime
     * @param userId
     * @param userName
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出用户清单数据", notes = "导出用户清单数据")
    public void moreExportCampInfoList(@Param("cityId") String cityId, @Param("departmentId") String departmentId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") String userId, @Param("userName") String userName) {

        UserPageQueryJx queryJx = new UserPageQueryJx();
        queryJx.setCityId(cityId);
        queryJx.setDepartmentId(departmentId);
        queryJx.setStartTime(startTime);
        queryJx.setEndTime(endTime);
        queryJx.setUserId(userId);
        queryJx.setUserName(userName);
        queryJx.setCurrent(1);
        queryJx.setSize(999999);
        try {
            log.info("开始导出用户清单清单数据");
            //获取列表
            List<List<String>> lists = userJxService.exportUser(queryJx);
            //获取表头
            List<String> header = Lists.newArrayList("地市", "用户ID", "用户昵称", "用户部门", "角色名称", "创建时间");
            //获取文件名
            String fileName = StrUtil.format("用户清单_{}.xls", DateUtil.now());

            log.info("活动清单数据查询成功，正在导出...");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLUtil.encode(fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            final ServletOutputStream outputStream = response.getOutputStream();
            //写表头，写内容
            ExcelUtil.getWriter().writeHeadRow(header).write(lists).flush(outputStream).close();
            log.info("活动清单数据导出成功");
        } catch (Exception e) {
            log.error("导出活动清单数据失败", e);
        }
    }

    @ApiOperation(value = "查询用户调查问卷访问记录数", notes = "查询用户调查问卷访问记录数")
    @PostMapping("/countMonthSurveyLog")
    public ActionResponse countMonthSurveyLog(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) {
        try {
            if (StrUtil.isEmpty(userId)) {
                userId = UserUtil.getUserId(request);
            }
            return ActionResponse.getSuccessResp(userJxService.countMonthSurveyLog(userId));
        } catch (Exception e) {
            log.error("查询用户调查问卷访问记录数出错：", e);
            throw new BaseException("查询用户调查问卷访问记录数出错");
        }
    }

    @ApiOperation(value = "记录用户调查问卷访问日志", notes = "记录用户调查问卷访问日志")
    @PostMapping("/insertSurveyLog")
    public ActionResponse insertSurveyLog(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) {
        try {
            if (StrUtil.isEmpty(userId)) {
                userId = UserUtil.getUserId(request);
            }
            return ActionResponse.getSuccessResp(userJxService.insertSurveyLog(userId));
        } catch (Exception e) {
            log.error("记录用户调查问卷访问日志出错：", e);
            throw new BaseException("记录用户调查问卷访问日志出错");
        }
    }

    @ApiOperation(value = "获取大音平台Sign", notes = "获取大音平台Sign")
    @GetMapping("/getDayinSign")
    public ActionResponse getDayinSign(@RequestParam(value = "userId", required = false) String userId, HttpServletRequest request) {
        ActionResponse resp = ActionResponse.getSuccessResp();
        try {
            String appId = RedisUtils.getDicValue(DAYIN_APP_ID);
            String appKey = RedisUtils.getDicValue(DAYIN_APP_KEY);
            log.info("appId:{},appKey:{}", appId, appKey);
            if (StrUtil.isEmpty(appId) || StrUtil.isEmpty(appKey)) {
                resp.setStatus(ResponseStatus.ERROR);
                resp.setMessage("大音平台AppID或者AppKey未配置");
                return resp;
            }
            if (StrUtil.isEmpty(userId)) {
                userId = UserUtil.getUserId(request);
            }
            long time = System.currentTimeMillis();
            String secret = HexUtil.encodeHexStr(appKey.getBytes(StandardCharsets.UTF_8));
            String sign = HmacSHAEncoder.hmacSHA(appId + time, secret, HMACSHA256);
            log.info("sign:{}", sign);
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("appId",appId);
            jsonObject.set("timestamp",time);
            jsonObject.set("sign",sign);
            jsonObject.set("user",userId);
            String jsonObjectStr = jsonObject.toString();
            log.info("jsonObject:{}",jsonObjectStr);
            String encode = Base64.encode(jsonObjectStr.getBytes(StandardCharsets.UTF_8));
            resp.setData(encode);
            return resp;
        } catch (Exception e) {
            log.error("获取大音平台Sign出错：", e);
            throw new BaseException("获取大音平台Sign出错");
        }
    }

}


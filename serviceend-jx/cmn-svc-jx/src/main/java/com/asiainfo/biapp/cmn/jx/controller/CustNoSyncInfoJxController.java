package com.asiainfo.biapp.cmn.jx.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.cmn.jx.model.McdNoticeReadUserListModel;
import com.asiainfo.biapp.cmn.jx.query.DelNoticeInfoRequest;
import com.asiainfo.biapp.cmn.jx.service.McdNoticeReadUserService;
import com.asiainfo.biapp.cmn.model.NoticeInfo;
import com.asiainfo.biapp.cmn.service.ICalendarInfoService;
import com.asiainfo.biapp.cmn.service.IUserRoleRelationService;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.McdIdQuery;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * description: 周期客群未同步通知的控制器
 *
 * @author: ranpf
 * @date: 2023/2/6
 */
@RestController
@RequestMapping("/api/calendar-info/jx")
@Slf4j
@Api(tags = "江西:周期客群未同步通知")
public class CustNoSyncInfoJxController {


    @Autowired
    private ICalendarInfoService iCalendarInfoService;


    @Resource
    private IUserRoleRelationService userRoleRelationService;

    @Resource
    private McdNoticeReadUserService mcdNoticeReadUserService;


    @ApiOperation(value = "江西查询周期客群未同步通知数据", notes = "江西查询周期客群未同步通知数据")
    @PostMapping("/getCustNoticeInfoByUser")
    public ActionResponse getCustNoticeInfoByUser(HttpServletRequest request) {

        String userId = UserUtil.getUserId(request);
        boolean flag = isAdmin(request);

        LambdaQueryWrapper<NoticeInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeInfo::getNoticeType, 4)
               .eq(!flag,NoticeInfo::getCreateUser, userId)
               .orderByDesc(NoticeInfo::getCreateTime);
        List<NoticeInfo> calendarInfoList = iCalendarInfoService.list(wrapper);
        LambdaQueryWrapper<McdNoticeReadUserListModel> listModelWrapper = new LambdaQueryWrapper<>();

        for (NoticeInfo noticeInfo : calendarInfoList) {
            listModelWrapper.clear();
            listModelWrapper.eq(McdNoticeReadUserListModel::getNoticeId,noticeInfo.getNoticeId())
                    .eq(McdNoticeReadUserListModel::getReadUserId,userId);
            List<McdNoticeReadUserListModel> listModels = mcdNoticeReadUserService.list();
            if (listModels != null && listModels.size() > 0){
                noticeInfo.setIsRead(1);
            }else {
                noticeInfo.setIsRead(0);
            }
        }

        return ActionResponse.getSuccessResp(calendarInfoList);
    }


    @ApiOperation(value = "江西修改周期客群未同步提醒数据为已读", notes = "江西修改周期客群未同步提醒数据为已读")
    @PostMapping("/updateCustNoticeInfoByUser")
    public ActionResponse updateCalendarInfoByUser(@RequestBody NoticeInfo dto,HttpServletRequest request) {
        String userId = UserUtil.getUserId(request);
        McdNoticeReadUserListModel userListModel = new McdNoticeReadUserListModel();
        userListModel.setNoticeId(dto.getNoticeId());
        userListModel.setReadUserId(userId);
        boolean isSucc = mcdNoticeReadUserService.save(userListModel);
        if (isSucc) {
            return ActionResponse.getSuccessResp("修改成功");
        }
        return ActionResponse.getSuccessResp("修改失败");
    }



    @ApiOperation(value = "删除上线通知", notes = "删除上线通知")
    @PostMapping("/remove")
    public ActionResponse remove(@RequestBody McdIdQuery req,HttpServletRequest request) {

        String userId = UserUtil.getUserId(request);
        NoticeInfo byId = iCalendarInfoService.getById(req.getId());
        boolean isAdmin = isAdmin(request);
        if (isAdmin || (byId !=null  && byId.getCreateUser().equals(userId))){
            boolean isSucc = iCalendarInfoService.removeById(req.getId());
            if (isSucc) {
                return ActionResponse.getSuccessResp("删除成功!");
            }
        }

        return ActionResponse.getSuccessResp("删除失败,不是创建人或管理员!");
    }


    @ApiOperation(value = "江西删除系统通知数据接口", notes = "江西删除系统通知数据接口")
    @PostMapping("/delelteCustNoticeInfo")
    public ActionResponse delelteCustNoticeInfo(@RequestBody DelNoticeInfoRequest req,HttpServletRequest request) {
        if (StrUtil.isBlank(req.getNoticeId()+"")) {
            return ActionResponse.getSuccessResp("参数异常");
        }
        boolean b = iCalendarInfoService.removeById(req.getNoticeId());
        if (b) {
            return ActionResponse.getSuccessResp("删除成功");
        }
        return ActionResponse.getSuccessResp("删除失败");
    }

    private boolean isAdmin(HttpServletRequest request){
        String roleId = RedisUtils.getDicValue("MCD_ADMIN_ROLE_ID");
        String userId = UserUtil.getUserId(request);
        boolean flag = false;
        if (StringUtils.isNotEmpty(roleId)) {
            List<String> userIdList = userRoleRelationService.findUserIDByRoleID(Long.parseLong(roleId));
            flag = userIdList.contains(userId);
        }

        return flag;
    }

    @ApiOperation(value = "查询上线通知:日历、通知、帮助前10且已发布(状态为1)的数据", notes = "查询日历、通知、帮助前10且已发布(状态为1)的数据")
    @PostMapping("/getTopTenNoticeInfo")
    public ActionResponse getTopTenNoticeInfo(HttpServletRequest request) {
        String userId = UserUtil.getUserId(request);
        LambdaQueryWrapper<NoticeInfo> wrapper = new LambdaQueryWrapper<>();
        // 通知管理展示日历、通知、帮助前10的且已发布(状态为1)的数据
        wrapper.eq(NoticeInfo::getStatus, 1)
                .in(NoticeInfo::getNoticeType,1,2,3)
                .eq(NoticeInfo::getIsShowTip,1)
                .orderByDesc(NoticeInfo::getCalendarTime).last("limit 10");

        List<NoticeInfo> calendarInfoList = iCalendarInfoService.list(wrapper);
        LambdaQueryWrapper<McdNoticeReadUserListModel> listModelWrapper = new LambdaQueryWrapper<>();
        for (NoticeInfo noticeInfo : calendarInfoList) {
            listModelWrapper.clear();
            listModelWrapper.eq(McdNoticeReadUserListModel::getNoticeId,noticeInfo.getNoticeId())
                            .eq(McdNoticeReadUserListModel::getReadUserId,userId);
             List<McdNoticeReadUserListModel> listModels = mcdNoticeReadUserService.list(listModelWrapper);
             if (listModels != null && listModels.size() > 0){
                 noticeInfo.setIsRead(1);
             }else {
                 noticeInfo.setIsRead(0);
             }
        }
        return ActionResponse.getSuccessResp(calendarInfoList);
    }

}

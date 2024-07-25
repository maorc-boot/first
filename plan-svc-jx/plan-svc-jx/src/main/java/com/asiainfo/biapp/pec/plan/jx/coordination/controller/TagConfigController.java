package com.asiainfo.biapp.pec.plan.jx.coordination.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.*;
import cn.hutool.core.collection.CollectionUtil;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.TagConfigReq;
import com.asiainfo.biapp.pec.plan.jx.coordination.req.UpdateTagOrderByQuery;
import com.asiainfo.biapp.pec.plan.jx.coordination.service.TagConfigService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(value = "标签配置", tags = "标签配置")
@RestController
@RequestMapping("/orchestrate/tag")
@Slf4j
public class TagConfigController {

    @Resource
    private TagConfigService tagConfigService;

    @ApiOperation(value = "查询客户类型")
    @PostMapping("/queryTagType")
    public ActionResponse queryCustType() {
        List<McdCoordinateCustType> mcdCoordinateCustTypeList = tagConfigService.queryCustType();
        return ActionResponse.getSuccessResp(mcdCoordinateCustTypeList);
    }


    @ApiOperation(value = "标签配置分页查询", notes = "标签配置分页查询")
    @PostMapping("/queryTag")
    public ActionResponse queryTag(@RequestBody TagConfigReq req) {
        log.info("标签配置分页查询入参: {}", JSONUtil.toJsonStr(req));
        Page<McdCoordinateTagModel> mcdCoordinateTagModelPage = tagConfigService.queryTag(req);
        return ActionResponse.getSuccessResp(mcdCoordinateTagModelPage);
    }


    @ApiOperation(value = "新增标签配置-查询已接入标签", notes = "展示标签配置表中所有状态为不可用的标签")
    @PostMapping("/queryAccessLabel")
    public ActionResponse queryAccessLabel(@RequestBody TagConfigReq req) {

        log.info("新增标签配置-查询已接入标签: {}", JSONUtil.toJsonStr(req));
       List<McdCoordinateTagModel> mcdCoordinateTagModelList = tagConfigService.queryAccessLabel(req);

        return ActionResponse.getSuccessResp(mcdCoordinateTagModelList);
    }




    @ApiOperation(value = "新增标签配置-保存", notes = "新增标签配置-保存")
    @PostMapping("/saveTag")
    public ActionResponse saveTag(@RequestBody List<McdCoordinateTagModel> mcdCoordinateTagModelList, HttpServletRequest request) {
        log.info("标签配置新增对象: {}", JSONUtil.toJsonStr(mcdCoordinateTagModelList));
        boolean save = false;
        boolean update = false;
        UserSimpleInfo user = UserUtil.getUser(request);
        String userName = user.getUserName();

        List<McdCoordinateTag> saveList = new ArrayList<>();
        List<McdCoordinateTag> updateList = new ArrayList<>();

        mcdCoordinateTagModelList.forEach(list -> {
            if (list.isAdd()) {
                //新增标签
                McdCoordinateTag tag = new McdCoordinateTag();
                BeanUtils.copyProperties(list, tag);
                tag.setOperator(userName);
                tag.setStatus(1);
                tag.setUpdateTime(DateUtil.parse(DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                saveList.add(tag);
            } else {
                //修改标签
                McdCoordinateTag tag = new McdCoordinateTag();
                BeanUtils.copyProperties(list, tag);
                tag.setOperator(userName);
                tag.setUpdateTime(DateUtil.parse(DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                updateList.add(tag);
            }
        });
        log.info("标签新增saveList{},更新updateList{}", JSONUtil.toJsonStr(saveList), JSONUtil.toJsonStr(updateList));
        if (CollectionUtil.isNotEmpty(saveList)) {
            save = tagConfigService.updateBatchById(saveList);
        }
        if (CollectionUtil.isNotEmpty(updateList)) {
            update = tagConfigService.updateBatchById(updateList);

        }
        if (save || update) {
            return ActionResponse.getSuccessResp("新增或更新成功");
        } else {
            return ActionResponse.getFaildResp("新增或更新失败");
        }
    }


    @ApiOperation(value = "标签配置删除",notes = "修改状态为不可用")
    @PostMapping("/delTagConfig")
    private ActionResponse delTagConfig(@RequestBody UpdateTagOrderByQuery query) {
        log.info("标签配置删除入参对象：{}", JSONUtil.toJsonStr(query));
        LambdaUpdateWrapper<McdCoordinateTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(McdCoordinateTag::getStatus,0).eq(McdCoordinateTag::getTagId, query.getTagId());
        wrapper.set(McdCoordinateTag::getUpdateTime,DateUtil.parse(DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss")).eq(McdCoordinateTag::getTagId, query.getTagId());
        boolean update = tagConfigService.update(wrapper);
        if (update) {
            return ActionResponse.getSuccessResp("删除成功");
        } else {
            return ActionResponse.getFaildResp("删除失败");
        }
    }

    @ApiOperation(value = "查询所有已配置的标签(不分页)", notes = "查询所有已配置的标签(不分页)")
    @PostMapping("/queryAllTag")
    public ActionResponse queryAllTag() {
        log.info("修改操作--查询所有已配置的标签(不分页)：{}");
        List<McdCoordinateTagModel> mcdCoordinateTagModelsList = tagConfigService.queryAllTag();
        return ActionResponse.getSuccessResp(mcdCoordinateTagModelsList);
    }

    @ApiOperation(value = "标签配置修改优先级")
    @PostMapping("/updateTagOrderBy")
    public ActionResponse updateTagOrderBy(@RequestBody UpdateTagOrderByQuery query) {
        log.info("标签配置修改优先级入参对象：{}", JSONUtil.toJsonStr(query));
        LambdaUpdateWrapper<McdCoordinateTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(McdCoordinateTag::getOrderBy, query.getOrderBy()).eq(McdCoordinateTag::getTagId, query.getTagId());
        wrapper.set(McdCoordinateTag::getUpdateTime, DateUtil.parse(DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss")).eq(McdCoordinateTag::getTagId, query.getTagId());
        boolean update = tagConfigService.update(wrapper);

        if (update) {
            return ActionResponse.getSuccessResp("修改优先级成功");
        } else {
            return ActionResponse.getFaildResp("修改优先级失败");
        }
    }
}

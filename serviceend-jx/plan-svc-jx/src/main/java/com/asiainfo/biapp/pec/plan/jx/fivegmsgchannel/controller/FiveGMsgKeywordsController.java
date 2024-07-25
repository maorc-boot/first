package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.dao.IFiveGMsgKeywordsDao;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.service.IFiveGMsgKeywordsService;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveGMsgKeywordsVo;
import com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo.FiveMsgVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 江西：5g消息关键词控制器
 *
 * @author lvcc
 * @date 2023/02/13
 */
@RestController
@RequestMapping("/action/jx/msg5g_keywords")
@Api(tags = "江西：5g消息关键词")
@Slf4j
public class FiveGMsgKeywordsController {

    @Autowired
    private IFiveGMsgKeywordsService fiveGMsgKeywordsService;

    @Autowired
    private IFiveGMsgKeywordsDao fiveGMsgKeywordsDao;

    /**
     * 获取5g消息渠道关键词数据
     *
     * @return Map<String, Object>
     */
    @ApiOperation(value = "获取5g消息渠道关键词数据")
    @PostMapping("/getFiveGKeywordsInfo")
    public IPage<FiveGMsgKeywordsVo> getFiveGKeywordsInfo(@RequestBody FiveMsgVo fiveMsgVo) {
        log.info("获取5g消息渠道关键词数据入参：{}", JSONUtil.toJsonStr(fiveMsgVo));
        Page<FiveGMsgKeywordsVo> page = new Page<>(fiveMsgVo.getCurrent(), fiveMsgVo.getSize());
        LambdaQueryWrapper<FiveGMsgKeywordsVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(fiveMsgVo.getApplicationNum()), FiveGMsgKeywordsVo::getApplicationNum, fiveMsgVo.getApplicationNum())
                .like(StrUtil.isNotEmpty(fiveMsgVo.getKeyWords()), FiveGMsgKeywordsVo::getKeywordsName, fiveMsgVo.getKeyWords())
                .orderByDesc(FiveGMsgKeywordsVo::getCreateTime);
        return fiveGMsgKeywordsService.page(page, queryWrapper);
    }

    /**
     * 新增或修改5g消息渠道关键词数据
     *
     * @return Map<String, Object>
     */
    @ApiOperation(value = "新增或修改5g消息渠道关键词数据")
    @PostMapping("/updateFiveGKeywordsInfo")
    public ActionResponse updateFiveGKeywordsInfo(@RequestBody FiveGMsgKeywordsVo fiveGMsgKeywordsVo, HttpServletRequest request) {
        log.info("新增或修改5g消息渠道关键词数据入参：{}", JSONUtil.toJsonStr(fiveGMsgKeywordsVo));
        UserSimpleInfo user = UserUtil.getUser(request);
        LambdaQueryWrapper<FiveGMsgKeywordsVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FiveGMsgKeywordsVo::getApplicationNum, fiveGMsgKeywordsVo.getApplicationNum())
                    .eq(FiveGMsgKeywordsVo::getKeywordsName, fiveGMsgKeywordsVo.getKeywordsName());
        // 应用号与关键词一对一
        FiveGMsgKeywordsVo selectOne = fiveGMsgKeywordsDao.selectOne(queryWrapper);
        FiveGMsgKeywordsVo vo = new FiveGMsgKeywordsVo();
        BeanUtils.copyProperties(fiveGMsgKeywordsVo, vo, "createUser");
        vo.setCreateUser(user.getUserId());
        // 转换入参(TEMPLATE_TYPE FALLBACK_TYPE)
        buildInsertParam(fiveGMsgKeywordsVo, vo);
        if (ObjectUtil.isEmpty(selectOne) && StrUtil.isEmpty(fiveGMsgKeywordsVo.getKeywordsId())) {
            // 新增关键词
            boolean save = fiveGMsgKeywordsService.save(vo);
             if(save) {
                 return ActionResponse.getSuccessResp("新增成功");
            } else {
                 return ActionResponse.getFaildResp("新增失败");
             }
        } else {
            // 更新关键词（关键词名不允许修改）
            // vo.setKeywordsId(fiveGMsgKeywordsVo.getKeywordsId());
            boolean updateById = fiveGMsgKeywordsService.updateById(vo);
            if(updateById) {
                return ActionResponse.getSuccessResp("修改成功");
            } else {
                return ActionResponse.getFaildResp("修改失败");
            }
        }
    }

    /**
     * 模板类型、回落类型值转换入库
     *
     * @param fiveGMsgKeywordsVo fiveGMsgKeywordsVo
     * @param vo vo
     */
    private void buildInsertParam(FiveGMsgKeywordsVo fiveGMsgKeywordsVo, FiveGMsgKeywordsVo vo) {
        // 1 静态、 2 交互动态、 3 主动动态、 4 空白
        switch (fiveGMsgKeywordsVo.getTemplateType()) {
            case "1":
                vo.setTemplateType("静态");
                break;
            case "2":
                vo.setTemplateType("交互动态");
                break;
            case "3":
                vo.setTemplateType("主动动态");
                break;
            default:
                vo.setTemplateType("空白");
                break;
        }
        // 0 文本回落；1 视频回落 ；2 短小2.0回落； -1 不回落
        switch (fiveGMsgKeywordsVo.getFallBackType()) {
            case "0":
                vo.setFallBackType("文本回落");
                break;
            case "1":
                vo.setFallBackType("视频回落");
                break;
            case "2":
                vo.setFallBackType("短小2.0回落");
                break;
            default:
                vo.setFallBackType("不回落");
                break;
        }
    }

    /**
     * 删除5g消息渠道关键词数据
     *
     * @param fiveMsgVo fiveMsgVo
     * @return 是否删除成功
     */
    @ApiOperation(value = "删除5g消息渠道关键词数据")
    @PostMapping("/deleteFiveGKeywordsInfo")
    public ActionResponse deleteFiveGKeywordsInfo(@RequestBody FiveMsgVo fiveMsgVo) {
        log.info("删除5g消息渠道关键词数据入参：{}", JSONUtil.toJsonStr(fiveMsgVo));
        int deleteById = fiveGMsgKeywordsDao.deleteById(fiveMsgVo.getKeywordsId());
        return deleteById > 0 ? ActionResponse.getSuccessResp("删除成功") : ActionResponse.getFaildResp("删除失败");
    }

}

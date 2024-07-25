package com.asiainfo.biapp.pec.element.jx.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.enums.ResponseStatus;
import com.asiainfo.biapp.pec.element.jx.entity.McdPlanLabel;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanLabelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 产品标签表 前端控制器
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
@RestController
@RequestMapping("/mcd-plan-label")
@Slf4j
@Api(value = "江西:产品标签(分类)）", tags = {"江西:产品分类标签(分类)"})
public class McdPlanLabelController {

    @Autowired
    private McdPlanLabelService mcdPlanLabelService;

    @GetMapping("/queryLabelByKey")
    @ApiOperation(value = "根据labelKey查询标签 ", notes = "labelKey的值有:产品业务分类-PROD_CLASS_BUSI,商品类型-OFFER_TYPE,产商品类型-ITEM_TYPE")

    public ActionResponse<McdPlanLabel> queryLabelByKey(@RequestParam("labelKey") String labelKey) {
        log.info("根据labelKey查询标签,labelKey {}", labelKey);
        ActionResponse resp = ActionResponse.getSuccessResp();

        try {
            QueryWrapper<McdPlanLabel> wrapper = new QueryWrapper();
            wrapper.lambda().eq(McdPlanLabel::getLabelKey, labelKey);
            resp.setData(mcdPlanLabelService.list(wrapper));
        } catch (Exception e) {
            log.error("根据labelKey查询标签异常,labelKey {}", labelKey, e);
            resp.setStatus(ResponseStatus.ERROR);
        }
        return resp;
    }


}


package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdExcellentCampPageQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampDefJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMcdCampSaleSituationService;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IMtlEvalInfoPlanDJxService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CampChannel;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdExcellentRecommendCamp;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.MyMCDJxVO;
import com.asiainfo.biapp.pec.plan.vo.SaleSituationVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ranpf
 * @ClassName HomeController
 * @date 2022/12/27 11:10
 * @Description 首页
 */
@Api(tags = "江西:首页")
@Slf4j
@RestController
@RequestMapping("/api/home/jx")
@RefreshScope
public class HomeJxController {

    @Autowired
    private IMcdCampDefJxService iMcdCampDefJxService;

    @Autowired
    private IMtlEvalInfoPlanDJxService mtlEvalInfoPlanDJxService;

    @Autowired
    private IMcdCampSaleSituationService mcdCampSaleSituationService;

    @ApiOperation(value = "江西:我的营销策略", notes = "我的营销策略")
    @PostMapping("/queryTacticsByUserId")
    public ActionResponse<List<MyMCDJxVO>> queryTacticsByUserId(HttpServletRequest request) {
        String userid = UserUtil.getUserId(request);
        //按创建时间展示最新5条
        List<MyMCDJxVO> myMCDVOIPage = iMcdCampDefJxService.listMyMCD(userid);
        return ActionResponse.getSuccessResp(myMCDVOIPage);
    }

    @ApiOperation(value = "江西:获取触发与办理总数", notes = "江西:获取触发与办理总数")
    @PostMapping("/querySaleSituation")
    public ActionResponse<SaleSituationVO> querySaleSituation(HttpServletRequest request) {
        String userCity = UserUtil.getUserCity(request);
        userCity = StrUtil.equals(userCity,"999")?"":userCity;
        SaleSituationVO saleSituationVO = mtlEvalInfoPlanDJxService.querySaleSituation(userCity);
        return ActionResponse.getSuccessResp(saleSituationVO);
    }


    @PostMapping("/excellentRecomCamp")
    @ApiOperation(value = "江西优秀营销策略推荐查询接口")
    public ActionResponse<Page<McdExcellentRecommendCamp>> getRecommendCamps(@RequestBody McdExcellentCampPageQuery req, HttpServletRequest request ) {

        String cityId = UserUtil.getUserCity(request);

        Page page = new Page();
        page.setSize(req.getSize());
        page.setCurrent(req.getCurrent());
        try {
            Page<McdExcellentRecommendCamp> campPage= mcdCampSaleSituationService.getRecommendCamp(page, cityId);

            campPage.getRecords().forEach(recomCamp->{
                List<CampChannel> channels = mcdCampSaleSituationService.getCampChannel(recomCamp.getCampsegId());
                recomCamp.setChannels(channels);
            });

            return ActionResponse.getSuccessResp(campPage);

        } catch (Exception e) {
            log.error("获取优秀策略异常", e);
           return ActionResponse.getFaildResp("获取优秀策略异常");
        }

    }

}

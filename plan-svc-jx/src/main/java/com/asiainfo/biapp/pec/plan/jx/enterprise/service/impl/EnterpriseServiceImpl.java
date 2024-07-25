package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;

import com.asiainfo.biapp.pec.core.utils.DateTool;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.dao.McdCustgroupDefDao;
import com.asiainfo.biapp.pec.plan.jx.enterprise.mapper.EnterPriseMapper;
import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdCampDef;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.EnterpriseService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.ApproveUserVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.OfferVo;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.OrgVo;
import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    private EnterPriseMapper enterPriseMapper;

    @Resource
    private McdCustgroupDefDao custgroupDefDao;

    @Override
    public List<OfferVo> queryEnterprisePlan(Map<String, Object> map) throws Exception {
        return enterPriseMapper.queryEnterprisePlan(map);
    }

    @Override
    public List<ApproveUserVo> queryApprove() throws Exception {
        String roleId = RedisUtils.getDicValue("APPROVE");
        return enterPriseMapper.queryApprove(roleId);
    }

    @Override
    public List<OrgVo> queryCityList() {
        return enterPriseMapper.queryCityList();
    }

    @Override
    public List<Map<String, Object>> getCampDetails(String campsegId) throws Exception {
        if(StringUtils.isEmpty(campsegId)){
            return null;
        }
        return enterPriseMapper.getCampDetails(campsegId);
    }

    @Override
    public McdCampDef getCampSegInfo(String campSegId) throws Exception {
        return enterPriseMapper.queryCampSegInfo(campSegId);
    }

    @Override
    public long getTargetIdBySubitemId(String subitemId) {
        return enterPriseMapper.queryTargetIdBySubitemId(subitemId);
    }

    @Override
    public McdCustgroupDef saveZqCustDefInfo(long targetId, String custgroupId, String createUserId) {
        Date startTime = new Date();
        McdCustgroupDef custInfoBean = new McdCustgroupDef();
        try {
            custInfoBean.setCustomGroupId(custgroupId);
            custInfoBean.setCustomGroupName("[标签]" + targetId + "集团成员");
            custInfoBean.setCustomGroupDesc("[标签]" + targetId + "集团成员");
            custInfoBean.setRuleDesc("营销目标:[指标" + targetId + "],[集团成员]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowMonth = DateTool.getStringDate(new Date());
            custInfoBean.setCreateTime(startTime);
            custInfoBean.setCreateUserId(createUserId);
            custInfoBean.setEffectiveTime(sdf.parse(getTargetDateByDateNew(nowMonth,0)));
            custInfoBean.setFailTime(sdf.parse(getTargetDateByDateNew(nowMonth,180)));
            SimpleDateFormat sdfd = new SimpleDateFormat("yyyyMMdd");
            String dataDate = sdfd.format(System.currentTimeMillis());
            custInfoBean.setDataDate(Integer.valueOf(dataDate));
            custInfoBean.setCustType(3);
            custInfoBean.setUpdateCycle(1);
            custInfoBean.setPushTargetUser(createUserId);
            //初始化客户群基础信息 状态为1
            custInfoBean.setCustomStatusId(1);
            //保存客户群信息
            custgroupDefDao.insert(custInfoBean);
            log.info("政企客群定义表保存完成！");
        } catch (Exception e) {
            log.error("保存/更新政企客户群基础信息异常", e);
        }
        return custInfoBean;
    }

    public String getTargetDateByDateNew(String strData,int due) {
        String preDate = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = sdf.parse(strData);
        } catch (Exception e) {

        }
        c.setTime(date);
        int curDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, curDay + due);
        preDate = sdf.format(c.getTime());
        return preDate;
    }

}

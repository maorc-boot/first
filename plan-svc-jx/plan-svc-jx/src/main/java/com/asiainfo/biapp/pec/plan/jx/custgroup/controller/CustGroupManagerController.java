package com.asiainfo.biapp.pec.plan.jx.custgroup.controller;


import com.asiainfo.biapp.pec.core.common.ActionResponse;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupDefInfo;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.McdCustgroupPushLogJx;
import com.asiainfo.biapp.pec.plan.jx.custgroup.model.NoticeInfo;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.ICustGroupInfoService;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.IMcdCustgroupPushLogJxService;
import com.asiainfo.biapp.pec.plan.jx.custgroup.service.INoticeInfoService;
import com.asiainfo.biapp.pec.plan.jx.custgroup.vo.McdCustGroupHttpVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Api(tags = "江西:客户群管理")
@RestController
@RequestMapping("/api/action/custgroup/custGroupManager")
public class CustGroupManagerController {
    protected final Log log = LogFactory.getLog(getClass());
	@Autowired
	private HttpServletRequest request;
	
	@Resource(name="custGroupInfoService")
	private ICustGroupInfoService custGroupInfoService;

	@Resource
	private IMcdCustgroupPushLogJxService mcdCustgroupPushLogJxService;


	@Resource
	private INoticeInfoService noticeInfoService;
	/**
	 * 查询客户群
	 *
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "江西:客户群管理查询",notes = "客户群管理查询列表")
	@PostMapping(value = "/getMoreMyCustom")
	@ResponseBody
	public IPage<McdCustgroupDefInfo> getMoreMyCustomNew(@RequestBody McdCustGroupHttpVo custGroupHttpVo )  {

		UserSimpleInfo user = UserUtil.getUser(request);

		custGroupHttpVo.setUserId(user.getUserId());
		custGroupHttpVo.setUserName(user.getUserName());


		return custGroupInfoService.searchCustGroup(custGroupHttpVo);

	}

	/**
	 * 查询客户群详情
	 *
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "客户群详情",notes = "客户群详情")
	@PostMapping("/viewCustGroupDetail")
	@ResponseBody
	public McdCustgroupDefInfo  viewCustGroupDetail(@RequestParam(value = "custGroupId") String custGroupId)  {

		McdCustgroupDefInfo custgroupDefInfo = custGroupInfoService.searchCustomDetail(custGroupId);

		return custgroupDefInfo;
	}



	/**
	 * 删除客户群
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "客户群删除",notes = "删除客户群")
	@PostMapping("/deleteCustom")
	@ResponseBody
	public ActionResponse  deleteCustom(@RequestParam(value = "custGroupId") String custGroupId)  {
		 custGroupInfoService.deleteCustom(custGroupId);

		 return ActionResponse.getSuccessResp();

	}



	/**
	 * 检查周期性客群未同步定时任务
	 *
	 * @return
	 */
	@PostMapping(path = "/mcdCustNoSyncTask")
	public void checkMcdCustNoSyncTask() {

        log.info("开始执行周期客群未同步通知任务!");
		//获取未过期的周期客群数据
		List<McdCustgroupDefInfo> todoTaskList = custGroupInfoService.searchCycleCustGroups();
		if (Objects.isNull(todoTaskList)){
			log.info("没有周期性客群!");
			return;
		}

		String prefix ="【客群更新异常】您的客群名称:";
		String custGroupCode = "，客户群编号:";
		String suffix ="，周期性更新异常，请及时处理。";

		int noticeNum = 0;
		for (McdCustgroupDefInfo defInfo:todoTaskList){
			NoticeInfo noticeInfo = new NoticeInfo();
			if (getCustGroupIsUpdate(defInfo.getCustomGroupId(),defInfo.getUpdateCycle())){
				String noticeName = prefix + defInfo.getCustomGroupName() + custGroupCode + defInfo.getCustomGroupId()+ suffix;

				noticeInfo.setNoticeUserId(defInfo.getCreateUserId());
				noticeInfo.setCreateUser(defInfo.getCreateUserId());
				noticeInfo.setNoticeType(4);//周期客群未同步通知类型
				noticeInfo.setNoticeContent(noticeName);
				noticeInfo.setNoticeName(noticeName);
				noticeInfo.setCreateTime(new Date());
				noticeInfo.setIsRead(0);

				noticeInfoService.save(noticeInfo);
				noticeNum++;
			}

		}

		log.info("共生成周期客群未同步通知信息"+ noticeNum+"条");
	}

	private boolean  getCustGroupIsUpdate(String custGroupId,int updateType ){


		if (updateType == 3){
			if (monthFirstDay("yyyy-MM-dd")){
				return false;
			}
		}
		//获取未过期的周期客群数据
		LambdaQueryWrapper<McdCustgroupPushLogJx> emisWrapper = new LambdaQueryWrapper<>();
		emisWrapper.eq(McdCustgroupPushLogJx::getCustomGroupId,custGroupId)
				.ge(McdCustgroupPushLogJx::getLogTime,getYesterday("yyyy-MM-dd"));
		List<McdCustgroupPushLogJx> todoTaskList = mcdCustgroupPushLogJxService.list(emisWrapper);
		if (todoTaskList != null && todoTaskList.size() > 0){
			return false;//客群已经更新
		}

		return true;//客群未更新,需要加通知信息
	}

	private Date getYesterday(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int curDay = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, curDay - 1);
        String startTime = dateFormat.format(calendar.getTime());

        try {
            return  dateFormat.parse(startTime);
        } catch (ParseException e) {
           return new Date();
        }

    }


    private boolean monthFirstDay(String dateStr){
		SimpleDateFormat format = new SimpleDateFormat(dateStr);
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.MONTH,0);
		rightNow.set(Calendar.DAY_OF_MONTH,1);
		String firstDay = format.format(rightNow.getTime());//当月第一天
		String nowaDay = format.format(new Date());//当前时间
		if (nowaDay.equals(firstDay)){
			return false;  //需要判断是否更新
		}

		return true;//不需要判断是否更新,跳过
	}
}

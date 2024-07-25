package com.asiainfo.biapp.pec.plan.jx.enterprise.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.biapp.client.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.utils.SimpleCache;
import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.GroupMemberPortraitEnum;
import com.asiainfo.biapp.pec.plan.jx.enterprise.enums.GroupPortraitEnum;
import com.asiainfo.biapp.pec.plan.jx.enterprise.service.EnterpriseGroupService;
import com.asiainfo.biapp.pec.plan.jx.enterprise.util.RestFullUtil;
import com.asiainfo.biapp.pec.plan.jx.enterprise.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 画像信息
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/2 14:54
 */
@Service
public class EnterpriseGroupServiceImpl implements EnterpriseGroupService {
	private static final Logger logger = LoggerFactory.getLogger(EnterpriseGroupServiceImpl.class);
	private static  final String MCD_SYS_DIC = "mcd_sys_dic";
	//政企集团画像和集团成员画像URL（IP+PORT）
	@Value("${plan.zq.ZQ_PROTRAIT_URL}")
	private String ZQ_PROTRAIT_URL;
	//集团画像场景配置，多个场景用英文逗号隔开
	@Value("${plan.zq.GROUP_SCENECODES}")
	private String GROUP_SCENECODES;
	//集团成员画像场景配置，多个场景用英文逗号隔开
	@Value("${plan.zq.GROUP_MEMBER_SCENECODES}")
	private String GROUP_MEMBER_SCENECODES;


	/**
	 * 获取集团画像信息
	 *
	 * @param grouPortraitVo
	 * @return
	 */
	@Override
	public JSONObject getGroupPortraitInfo(GrouPortraitVo grouPortraitVo, User user) {
		JSONObject object = new JSONObject();
		List<CustPortraitModel> custPortraitModelList = new ArrayList<>();
		try {
			String prefixUrl = ZQ_PROTRAIT_URL;
			//集团画像-多个场景用逗号（英文逗号）隔开
			String sceneCodes = GROUP_SCENECODES;
			//待最后确定修改
			logger.info("user=" + user.getId() + ",pwd=" + user.getPwd());
			String token = "token";
			//String token = getCocToken(user.getId(), user.getPwd());
			//http://{IP}:{PORT}/loc/api/portrait/external/queryListByScenes?keyId=13587331265&sceneCodes=f3239b5b43c642b8,f3239b5b43c642b6&keyType=1&provinceCode=bj
			String url = prefixUrl + "/loc/api/portrait/external/queryListByScenes" + "?keyId=" + grouPortraitVo.getGroupId() + "&sceneCodes=" + sceneCodes + "&keyType=5&provinceCode=jx";
			logger.info("调用coc获取集团画像接口cepWebUrl=" + url);
			String resultCopyRule = RestFullUtil.getInstance().sendGetRequest(url, token);
			//20230718
			// String resultCopyRule ="{\"status\":\"200\",\"msg\":\"客户画像调用成功\",\"data\":[{\"crossTable\":[{\"categoryName\":\"集团合约渗透率\",\"labelId\":\"LJX0022565\",\"labelName\":\"合约渗透率\",\"mapValue\":[{\"成员类型\":\"关键人员\",\"合约渗透率\":\"0.01\",\"rowNum\":\"1\"},{\"成员类型\":\"普通人员\",\"合约渗透率\":\"0.56\",\"rowNum\":\"2\"}],\"unitMap\":{\"成员类型\":null,\"合约渗透率\":null}},{\"categoryName\":\"集团宽带渗透率\",\"labelId\":\"LJX0022576\",\"labelName\":\"集团客户宽带渗透率\",\"mapValue\":[{\"集团成员类型\":\"普通人员\",\"宽带渗透率\":\"0.35\",\"rowNum\":\"1\"},{\"集团成员类型\":\"关键人员\",\"宽带渗透率\":\"0.01\",\"rowNum\":\"2\"}],\"unitMap\":{\"集团成员类型\":null,\"宽带渗透率\":null}},{\"categoryName\":\"集团5G渗透率\",\"labelId\":\"LJX0022577\",\"labelName\":\"集团客户5G渗透率\",\"mapValue\":[{\"集团成员类型\":\"关键人员\",\"5G渗透率\":\"0.01\",\"rowNum\":\"1\"},{\"集团成员类型\":\"普通人员\",\"5G渗透率\":\"0.67\",\"rowNum\":\"2\"}],\"unitMap\":{\"集团成员类型\":null,\"5G渗透率\":null}},{\"categoryName\":\"集团统付成员数\",\"labelId\":\"LJX0022578\",\"labelName\":\"集团统付成员数\",\"mapValue\":[{\"集团成员类型\":\"普通人员\",\"集团统付成员数\":\"399\",\"rowNum\":\"1\"},{\"集团成员类型\":\"关键人员\",\"集团统付成员数\":\"2\",\"rowNum\":\"2\"}],\"unitMap\":{\"集团统付成员数\":null,\"集团成员类型\":null}}],\"sceneCode\":\"6DB4BA02F187089DAF05989D33C2725F\"},{\"longTable\":[{\"categoryName\":\"\",\"labelId\":\"LJX0022524\",\"labelName\":\"政企企宽零流量预警模型_是否零流量企宽用户\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022527\",\"labelName\":\"政企移动办公潜客识别模型_是否移动办公潜客\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022528\",\"labelName\":\"政企移动办公潜客识别模型_概率值\",\"labelValue\":\"0\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022529\",\"labelName\":\"政企移动办公潜客识别模型_集团内钉钉APP使用人数占比\",\"labelValue\":\"0\",\"unit\":\"\"}],\"sceneCode\":\"6DB4BA02F187089D4E97A6973C19755C\"},{\"longTable\":[{\"categoryName\":\"\",\"labelId\":\"LJX0022524\",\"labelName\":\"政企企宽零流量预警模型_是否零流量企宽用户\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0015487\",\"labelName\":\"集团成员价值流失预警模型_潜在价值流失用户数(月)\",\"labelValue\":\"15\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0015488\",\"labelName\":\"集团成员离网预警模型_潜在离网用户数(月)\",\"labelValue\":\"4\",\"unit\":\"\"}],\"sceneCode\":\"A8B71765633A6AD15A711F89E6DCC3E0\"},{\"longTable\":[{\"categoryName\":\"\",\"labelId\":\"LJX0014282\",\"labelName\":\"集团成员数（日）\",\"labelValue\":\"806\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022583\",\"labelName\":\"本网成员数\",\"labelValue\":\"796\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022584\",\"labelName\":\"异网成员数\",\"labelValue\":\"0\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022585\",\"labelName\":\"三类关键人物数\",\"labelValue\":\"12\",\"unit\":\"\"}],\"sceneCode\":\"6DB4BA02F187089D816F69E10CE81654\"},{\"longTable\":[{\"categoryName\":\"\",\"labelId\":\"LJX0018808\",\"labelName\":\"集团编码\",\"labelValue\":\"xt9671502\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014285\",\"labelName\":\"集团名称(日）\",\"labelValue\":\"吉安市公安局\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014296\",\"labelName\":\"是否归属乡镇（日）\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014288\",\"labelName\":\"集团归属区县（日）\",\"labelValue\":\"城区\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014297\",\"labelName\":\"是否归属网格（日）\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014283\",\"labelName\":\"集团行业类型（日）\",\"labelValue\":\"党政军\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014295\",\"labelName\":\"客户经理名称（日）\",\"labelValue\":\"曾荟\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014284\",\"labelName\":\"集团级别(日）\",\"labelValue\":\"A类客户\",\"unit\":\"\"},{\"categoryName\":\"基础\",\"labelId\":\"LJX0014287\",\"labelName\":\"集团归属地市（日）\",\"labelValue\":\"吉安市\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022687\",\"labelName\":\"集团关键人信息（日）\",\"labelValue\":\"18827790365\",\"unit\":\"\"}],\"sceneCode\":\"ECC9D2A5AFDB5FDA8E636BD3468BFF36\"},{\"longTable\":[{\"categoryName\":\"\",\"labelId\":\"LJX0022646\",\"labelName\":\"信息化产品订购数\",\"labelValue\":\"54\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022648\",\"labelName\":\"信息化产品月收入（月）\",\"labelValue\":\"8888066.92\",\"unit\":\"\"},{\"categoryName\":\"\",\"labelId\":\"LJX0022650\",\"labelName\":\"重点产品订购\",\"labelValue\":\"3\",\"unit\":\"\"}],\"sceneCode\":\"ECC9D2A5AFDB5FDACC27D799A652F216\"},{\"longTable\":[{\"categoryName\":\"\",\"labelId\":\"LJX0022422\",\"labelName\":\"是否已订购企宽\",\"labelValue\":\"否\",\"unit\":\"\"}],\"sceneCode\":\"6DB4BA02F187089DA840857FD6F6B762\"}]}";
			logger.info("调用coc获取集团画像结果：" + resultCopyRule);
			JSONObject resultJson = JSONObject.parseObject(resultCopyRule);
			String status = resultJson.getString("status");
			if ("200".equals(status)) {
				String data = resultJson.getString("data");
				custPortraitModelList = JSON.parseArray(data, CustPortraitModel.class);
			}
			//数据转化
			GroupPortraitVo groupPortraitVo = groupPortraitDataConvert(custPortraitModelList, sceneCodes);
			String key = MCD_SYS_DIC+"_refresh";
			String time = RedisUtils.getValue(key);
			if(StringUtils.isBlank(time)){
				groupPortraitVo.setRefresh("1");
			}else{
				groupPortraitVo.setRefresh("0");
			}
			object.put("resultCode","0");
			object.put("resultInfo","ok");
			object.put("data",groupPortraitVo);
		} catch (Exception e) {
			logger.error("getGroupPortraitInfod exceptionMessage="+e);
			object.put("resultCode","1");
			object.put("resultInfo",e.getMessage());
			object.put("data",null);
		}
		return object;
	}

	/**
	 * 获取集团成员画像信息
	 *
	 * @param groupMemberVo
	 * @return
	 */
	@Override
	public JSONObject groupMemberDetail(GroupMemberVo groupMemberVo, User user) {
		JSONObject object = new JSONObject();
		List<CustPortraitVo> custPortraitVoList = new ArrayList<>();
		try {
			String prefixUrl = ZQ_PROTRAIT_URL;
			//集团成员画像-多个场景用逗号（英文逗号）隔开
			String sceneCodes = GROUP_MEMBER_SCENECODES;
			//待最后确定修改
			String token = "token";
			List<String> memberIds = groupMemberVo.getMemberId();
			for (String memberId : memberIds) {
				List<CustPortraitModel> custPortraitModelList = new ArrayList<>();
				//String token = getCocToken(user.getId(), user.getPwd());
				//http://{IP}:{PORT}/loc/api/portrait/external/queryListByScenes?keyId=13587331265&sceneCodes=f3239b5b43c642b8,f3239b5b43c642b6&keyType=1&provinceCode=bj
				String url = prefixUrl + "/loc/api/portrait/external/queryListByScenes" + "?keyId=" + memberId + "&sceneCodes=" + sceneCodes + "&keyType=1&provinceCode=jx";
				logger.info("调用coc获取成员画像接口cepWebUrl=" + url);
				String resultCopyRule = RestFullUtil.getInstance().sendGetRequest(url, token);
				//String resultCopyRule = "{\"status\":\"200\",\"msg\":\"客户画像调用成功\",\"data\":[{\"longTable\":[{\"categoryName\":\"personalInfo\",\"labelId\":\"LJX0005504\",\"labelName\":\"成员姓名(日)\",\"labelValue\":\"王小英\",\"unit\":\"\"},{\"categoryName\":\"personalInfo\",\"labelId\":\"LJX0007612\",\"labelName\":\"是否集团关键联系人(日)\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"busiOrderInfo\",\"labelId\":\"LJX0002114\",\"labelName\":\"当前月生效的资费名称(日)\",\"labelValue\":\"2019飞享套餐18\",\"unit\":\"\"},{\"categoryName\":\"busiOrderInfo\",\"labelId\":\"LJX0005557\",\"labelName\":\"是否5G套餐用户(日)\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"accountInfo\",\"labelId\":\"LJX0012975\",\"labelName\":\"客户前3个月平均ARPU值(月)\",\"labelValue\":\"59.66\",\"unit\":\"\"},{\"categoryName\":\"busiOrderInfo\",\"labelId\":\"LJX0005560\",\"labelName\":\"是否5G终端用户(日)\",\"labelValue\":\"是\",\"unit\":\"\"},{\"categoryName\":\" busiUsed\",\"labelId\":\"LJX0679790\",\"labelName\":\"用户持机月份(日)\",\"labelValue\":\"13\",\"unit\":\"\"},{\"categoryName\":\" busiUsed\",\"labelId\":\"LJX0002538\",\"labelName\":\"终端活动到期时间(日)\",\"labelValue\":\"\",\"unit\":\"\"}],\"sceneCode\":\"ECC9D2A5AFDB5FDA956797CA5B48ECBA\"},{\"longTable\":[{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0005504\",\"labelName\":\"成员姓名(日)\",\"labelValue\":\"王小英\",\"unit\":\"\"},{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0007612\",\"labelName\":\"是否集团关键联系人(日)\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"业务订购\",\"labelId\":\"LJX0002114\",\"labelName\":\"当前月生效的资费名称(日)\",\"labelValue\":\"2019飞享套餐18\",\"unit\":\"\"},{\"categoryName\":\"业务订购\",\"labelId\":\"LJX0002538\",\"labelName\":\"终端活动到期时间(日)\",\"labelValue\":\"\",\"unit\":\"\"},{\"categoryName\":\"业务使用\",\"labelId\":\"LJX0012975\",\"labelName\":\"客户前3个月平均ARPU值(月)\",\"labelValue\":\"59.66\",\"unit\":\"\"},{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0005560\",\"labelName\":\"是否5G终端用户(日)\",\"labelValue\":\"是\",\"unit\":\"\"},{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0679790\",\"labelName\":\"用户持机月份(日)\",\"labelValue\":\"13\",\"unit\":\"\"},{\"categoryName\":\"业务使用\",\"labelId\":\"LJX0005557\",\"labelName\":\"是否5G套餐用户(日)\",\"labelValue\":\"否\",\"unit\":\"\"}],\"sceneCode\":\"ECC9D2A5AFDB5FDAA840857FD6F6B762\"}]}";
				// String resultCopyRule ="{\"status\":\"200\",\"msg\":\"客户画像调用成功\",\"data\":[{\"longTable\":[{\"categoryName\":\"personalInfo\",\"labelId\":\"LJX0005504\",\"labelName\":\"成员姓名(日)\",\"labelValue\":\"1\",\"unit\":\"\"},{\"categoryName\":\"personalInfo\",\"labelId\":\"LJX0007612\",\"labelName\":\"是否集团关键联系人(日)\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"busiOrderInfo\",\"labelId\":\"LJX0002114\",\"labelName\":\"当前月生效的资费名称(日)\",\"labelValue\":\"\",\"unit\":\"\"},{\"categoryName\":\"busiOrderInfo\",\"labelId\":\"LJX0005557\",\"labelName\":\"是否5G套餐用户(日)\",\"labelValue\":\"是\",\"unit\":\"\"},{\"categoryName\":\"accountInfo\",\"labelId\":\"LJX0012975\",\"labelName\":\"客户前3个月平均ARPU值(月)\",\"labelValue\":\"120.54\",\"unit\":\"\"},{\"categoryName\":\"busiOrderInfo\",\"labelId\":\"LJX0005560\",\"labelName\":\"是否5G终端用户(日)\",\"labelValue\":\"是\",\"unit\":\"\"},{\"categoryName\":\" busiUsed\",\"labelId\":\"LJX0679790\",\"labelName\":\"用户持机月份(日)\",\"labelValue\":\"10\",\"unit\":\"\"},{\"categoryName\":\" busiUsed\",\"labelId\":\"LJX0002538\",\"labelName\":\"终端活动到期时间(日)\",\"labelValue\":\"\",\"unit\":\"\"}],\"sceneCode\":\"ECC9D2A5AFDB5FDA956797CA5B48ECBA\"},{\"longTable\":[{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0005504\",\"labelName\":\"成员姓名(日)\",\"labelValue\":\"1\",\"unit\":\"\"},{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0007612\",\"labelName\":\"是否集团关键联系人(日)\",\"labelValue\":\"否\",\"unit\":\"\"},{\"categoryName\":\"业务订购\",\"labelId\":\"LJX0002114\",\"labelName\":\"当前月生效的资费名称(日)\",\"labelValue\":\"\",\"unit\":\"\"},{\"categoryName\":\"业务订购\",\"labelId\":\"LJX0002538\",\"labelName\":\"终端活动到期时间(日)\",\"labelValue\":\"\",\"unit\":\"\"},{\"categoryName\":\"业务使用\",\"labelId\":\"LJX0012975\",\"labelName\":\"客户前3个月平均ARPU值(月)\",\"labelValue\":\"120.54\",\"unit\":\"\"},{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0005560\",\"labelName\":\"是否5G终端用户(日)\",\"labelValue\":\"是\",\"unit\":\"\"},{\"categoryName\":\"个人信息\",\"labelId\":\"LJX0679790\",\"labelName\":\"用户持机月份(日)\",\"labelValue\":\"10\",\"unit\":\"\"},{\"categoryName\":\"业务使用\",\"labelId\":\"LJX0005557\",\"labelName\":\"是否5G套餐用户(日)\",\"labelValue\":\"是\",\"unit\":\"\"}],\"sceneCode\":\"ECC9D2A5AFDB5FDAA840857FD6F6B762\"}]}";
				logger.info("调用coc获取成员画像结果：" + resultCopyRule);
				JSONObject resultJson = JSONObject.parseObject(resultCopyRule);
				String status = resultJson.getString("status");
				if ("200".equals(status)) {
					String data = resultJson.getString("data");
					custPortraitModelList = JSON.parseArray(data, CustPortraitModel.class);
				}

				//数据转化
				CustPortraitVo custPortraitVo = custPortraitDetailDataConvert(custPortraitModelList, sceneCodes, memberId);
				custPortraitVoList.add(custPortraitVo);
			}
			object.put("resultCode","0");
			object.put("resultInfo","ok");
			object.put("data",custPortraitVoList);
		} catch (Exception e) {
			logger.error("groupMemberDetail exceptionMessage="+e);
			object.put("resultCode","1");
			object.put("resultInfo",e.getMessage());
			object.put("data",null);
		}
		return object;
	}

	public CustPortraitVo custPortraitDetailDataConvert(List<CustPortraitModel> custPortraitModelList, String sceneCodes,String memberId){
		List<String> sceneCodeList = Arrays.asList(sceneCodes.split(","));
		CustPortraitVo custPortraitVo = new CustPortraitVo();
		List<BaseInfo> busiOrderInfos = new ArrayList<>();
		List<BaseInfo> familyBusiInfos = new ArrayList<>();
		List<BaseInfo> activityInfos = new ArrayList<>();
		List<BaseInfo> accountInfos = new ArrayList<>();
		List<BaseInfo> personalInfos = new ArrayList<>();
		List<BaseInfo> busiUseds = new ArrayList<>();
		List<BaseInfo> consumptions = new ArrayList<>();
		CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
		for (String sceneCode : sceneCodeList) {
			for (CustPortraitModel custPortraitModel : custPortraitModelList) {
				if(sceneCode.equals(custPortraitModel.getSceneCode())){
					List<PortraitLongTable> longTable = custPortraitModel.getLongTable();
					//基础信息
					if(sceneCode.equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_BASEINFO.getSceneCode())){
						customerBaseInfo = convertMemverBaseInfo(longTable);
						customerBaseInfo.setMemberId(memberId);
					}
					//业务订购信息+家庭业务+活动信息+账单信息+个人信息+业务使用+消费情况    当前7个分类公用一个场景编码
					if(sceneCode.equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_SUBTOTAL.getSceneCode())){
						for (PortraitLongTable portraitLongTable : longTable) {
							String categoryName = portraitLongTable.getCategoryName().trim();
							switch (categoryName) {
								case "busiOrderInfo":
									busiOrderInfos.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
								case "familyBusiInfo":
									familyBusiInfos.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
								case "activityInfo":
									activityInfos.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
								case "accountInfo":
									accountInfos.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
								case "personalInfo":
									personalInfos.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
								case "busiUsed":
									busiUseds.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
								case "consumption":
									consumptions.add(new BaseInfo(portraitLongTable.getLabelId(), portraitLongTable.getLabelName(), portraitLongTable.getLabelValue()));
									break;
							}
						}
					}
				}
			}
		}
		custPortraitVo.setBaseInfo(customerBaseInfo);
		custPortraitVo.setBusiOrderInfo(busiOrderInfos);
		custPortraitVo.setFamilyBusiInfo(familyBusiInfos);
		custPortraitVo.setActivityInfo(activityInfos);
		custPortraitVo.setAccountInfo(accountInfos);
		custPortraitVo.setPersonalInfo(personalInfos);
		custPortraitVo.setBusiUsed(busiUseds);
		custPortraitVo.setConsumption(consumptions);
		return custPortraitVo;
	}

	public CustomerBaseInfo convertMemverBaseInfo(List<PortraitLongTable> longTable){
		CustomerBaseInfo baseInfo = new CustomerBaseInfo();
		for (PortraitLongTable portraitLongTable : longTable) {
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_MEMBERID.getSceneCode())){
				//baseInfo.setMemberId(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_MEMBERNAME.getSceneCode())){
				//脱敏操作
				String phoneNum = portraitLongTable.getLabelValue();
				if(StringUtils.isNotBlank(phoneNum) && phoneNum.length() == 11){
					String newProductNo = "";
					Pattern pattern = Pattern.compile("[0-9]*");
					if(pattern.matcher(phoneNum).matches()){
						newProductNo = phoneNum.substring(0,3)+"****"+phoneNum.substring(8);
						baseInfo.setMemberId(portraitLongTable.getLabelValue());
						//因为标签没有用户名，则此处塞电话号码
						baseInfo.setMemberName(newProductNo);
					}else{
						//因为标签没有用户名，则此处塞电话号码
						baseInfo.setMemberName(phoneNum);
					}
				}else {
					//如果是姓名，则姓名最后一位脱敏
					if(phoneNum.length()>1){
						String phoneNumLike = phoneNum.substring(0,phoneNum.length()-1)+"*";
						baseInfo.setMemberName(phoneNumLike);
					}else{
						baseInfo.setMemberName(phoneNum);
					}
				}

			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_ISKEY.getSceneCode())){
				baseInfo.setIsKey(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_ISCONTACT.getSceneCode())){
				baseInfo.setIsContact(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_TELNO.getSceneCode())){
				baseInfo.setTelNo(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_PACKAGENAME.getSceneCode())){
				baseInfo.setPackageName(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_EXPIRE.getSceneCode())){
				baseInfo.setExpire(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_ARPU.getSceneCode())){
				baseInfo.setArpu(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_IS5G.getSceneCode())){
				baseInfo.setIs5G(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_NETAGE.getSceneCode())){
				baseInfo.setNetAge(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupMemberPortraitEnum.GROUP_PORTRAIT_IS5GPACKAGE.getSceneCode())){
				baseInfo.setIs5GPackage(portraitLongTable.getLabelValue());
			}
		}
		return baseInfo;
	}

	/**
	 * 集团信息-groupInfo信息组装
	 * @param longTable
	 * @return
	 */
	public GroupBaseInfo convertGroupBaseInfo(List<PortraitLongTable> longTable){
		GroupBaseInfo baseInfo = new GroupBaseInfo();
		for (PortraitLongTable portraitLongTable : longTable) {
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_GROUPCODE.getSceneCode())){
				baseInfo.setGroupCode(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_GROUPNAME.getSceneCode())){
				baseInfo.setGroupName(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_MANAGERNAME.getSceneCode())){
				String labelValue = portraitLongTable.getLabelValue();
				//如果是姓名，则姓名最后一位脱敏
				if(labelValue.length()>1){
					String labelValueLike = labelValue.substring(0,labelValue.length()-1)+"*";
					baseInfo.setManagerName(labelValueLike);
				}else{
					baseInfo.setManagerName(labelValue);
				}
			}
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_KEYPERSON.getSceneCode())){
				String labelValue = portraitLongTable.getLabelValue();
				//关键人，传号码  脱敏操作
				if(StringUtils.isNotBlank(labelValue) && labelValue.length() == 11){
					String newProductNo = "";
					Pattern pattern = Pattern.compile("[0-9]*");
					if(pattern.matcher(labelValue).matches()){
						newProductNo = labelValue.substring(0,3)+"****"+labelValue.substring(8);
						baseInfo.setKeyPerson(newProductNo);
					}else{
						baseInfo.setKeyPerson(labelValue);
					}
				}else{
					baseInfo.setKeyPerson(labelValue);
				}
			}
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_VALUEGRADE.getSceneCode())){
				String valueGrade = portraitLongTable.getLabelValue();
				if(StringUtils.isNotBlank(valueGrade)){
					valueGrade = valueGrade.substring(0,1);
				}
				baseInfo.setValueGrade(valueGrade);
			}
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_ADDRESS.getSceneCode())){
				baseInfo.setAddress(portraitLongTable.getLabelValue());
			}
			if(portraitLongTable.getLabelId().equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO_INDUSTRYATTR.getSceneCode())){
				baseInfo.setIndustryAttr(portraitLongTable.getLabelValue());
			}
		}
		//用经理名称代替关键人信息
		// baseInfo.setKeyPerson(baseInfo.getManagerName());
		return baseInfo;
	}


	public GroupPortraitVo groupPortraitDataConvert(List<CustPortraitModel> custPortraitModelList, String sceneCodes){
		List<String> sceneCodeList = Arrays.asList(sceneCodes.split(","));
		GroupPortraitVo groupPortraitVo = new GroupPortraitVo();
		groupPortraitVo.initSceneCodeMap();
		List<ChartInfos> chartInfoList = new ArrayList<>();
		for (String sceneCode : sceneCodeList) {
			for (CustPortraitModel custPortraitModel : custPortraitModelList) {
				if(sceneCode.equals(custPortraitModel.getSceneCode())){
					List<PortraitLongTable> longTable = custPortraitModel.getLongTable();
					List<PortraitCrossTable> crossTable = custPortraitModel.getCrossTable();
					//集团信息
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_GROUPINFO.getSceneCode())){
						GroupBaseInfo groupBaseInfo = convertGroupBaseInfo(longTable);
						groupPortraitVo.setGroupInfo(groupBaseInfo);
					}
					//集团成员信息
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_MEMBERINFO.getSceneCode())){
						List<BaseInfo> baseInfos = longTable.stream().map(result -> new BaseInfo(result.getLabelId(), result.getLabelName(), result.getLabelValue())).collect(Collectors.toList());
						groupPortraitVo.setMemberInfo(baseInfos);
					}
					//集团成员信息
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_MSGINFO.getSceneCode())){
						List<BaseInfo> msginfos = longTable.stream().map(result -> new BaseInfo(result.getLabelId(), result.getLabelName(), result.getLabelValue())).collect(Collectors.toList());
						groupPortraitVo.setMsgInfo(msginfos);
					}
					//预警
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_PREWARNINGINFO.getSceneCode())){
						List<BaseInfo> msginfos = longTable.stream().map(result -> new BaseInfo(result.getLabelId(), result.getLabelName(), result.getLabelValue())).collect(Collectors.toList());
						groupPortraitVo.setPrewarningInfo(msginfos);
					}
					//商机
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_OPPORTUNITYINFO.getSceneCode())){
						List<BaseInfo> msginfos = longTable.stream().map(result -> new BaseInfo(result.getLabelId(), result.getLabelName(), result.getLabelValue())).collect(Collectors.toList());
						groupPortraitVo.setOpportunityInfo(msginfos);
					}
					//集团客户政策信息
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_POLICYINFO.getSceneCode())){
						List<BaseInfo> msginfos = longTable.stream().map(result -> new BaseInfo(result.getLabelId(), result.getLabelName(), result.getLabelValue())).collect(Collectors.toList());
						groupPortraitVo.setPolicyInfo(msginfos);
					}
					//集团成员业务渗透信息
					if(sceneCode.equals(GroupPortraitEnum.GROUP_PORTRAIT_CHARTINFO.getSceneCode())){
						List<ChartSingle> chartList = new ArrayList<>();
						for (PortraitCrossTable portraitCrossTable : crossTable) {
							ChartSingle chartSingle = new ChartSingle();
							chartSingle.setCategoryName(StringUtils.isNotBlank(portraitCrossTable.getCategoryName())?portraitCrossTable.getCategoryName():portraitCrossTable.getLabelName());
							chartSingle.setLabelId(portraitCrossTable.getLabelId());
							chartSingle.setLabelName(portraitCrossTable.getLabelName());
							List<Map<String, String>> mapValue = portraitCrossTable.getMapValue();
							List<Map<String, String>> labeldata = new ArrayList<>();

							//是否为同一组数据（展示）标识
							int i = 1;
							for (Map<String, String> map : mapValue) {
								for (Map.Entry<String, String> entry : map.entrySet()) {
									Map<String, String> labelDataMap = new HashMap<>();
									String mapKey = entry.getKey();
									String mapValue1 = entry.getValue();
									if(!mapKey.equalsIgnoreCase("rowNum")){
										labelDataMap.put("dataName", mapKey);
										//含有数或者率的判断，含有为1，不含为0
										if(mapKey.contains("数") || mapKey.contains("率")){
											labelDataMap.put("hasKey", "1");
										}else{
											labelDataMap.put("hasKey", "0");
										}
										labelDataMap.put("dataValue", mapValue1);
										labelDataMap.put("dataUnit", "");
										//同组数据标识
										labelDataMap.put("sameGroupType", i+"");
										labeldata.add(labelDataMap);
									}
								}
								i++;
							}
							chartSingle.setLabeldata(labeldata);
							chartList.add(chartSingle);
						}
						//对结果分组
						Map<String, List<ChartSingle>> collectGroups = chartList.stream().collect(Collectors.groupingBy(chartSingle -> chartSingle.getCategoryName()));
						for (Map.Entry<String, List<ChartSingle>> entry : collectGroups.entrySet()) {
							ChartInfos chartInfos = new ChartInfos();
							String categoryName = entry.getKey();
							List<ChartSingle> value = entry.getValue();
							chartInfos.setLabelTypeId(categoryName);
							chartInfos.setLabelTypeName(categoryName);
							// chartInfos.setCategoryName(categoryName);
							chartInfos.setChartList(value);
							chartInfoList.add(chartInfos);
						}
						groupPortraitVo.setChartInfo(chartInfoList);
					}
				}
			}
		}
		return groupPortraitVo;
	}

	public String getCocToken(String userId, String pwd) {
		String token = (String) SimpleCache.getInstance().get(userId);
		if (StringUtils.isNotEmpty(token)) {
			logger.info(userId + "从缓存获取token:" + token);
			return token;
		}
		try {
			String prefixUrl = ZQ_PROTRAIT_URL;
			//http://10.226.23.66:8080/loc/api/user/login
			String url = prefixUrl + "/loc/api/user/login" + "?username=" + userId + "&password=" + pwd;
			String sendPost = RestFullUtil.getInstance().sendPost2(url, null);
			logger.info("getCocToken sendPost:{}", sendPost);
			JSONObject jsonObject = JSONObject.parseObject(sendPost);
			if ("200".equals(jsonObject.get("status"))) {
				JSONObject data = jsonObject.getJSONObject("data");
				token = data.getString("token");
				logger.info(userId + "获取token:" + token);
				SimpleCache.getInstance().put(userId, token, 2 * 60 * 60);
				logger.info(userId + "将token存入缓存");
			}
		} catch (Exception e) {
			logger.error("getCocToken 获取token异常：" + e);
		}
		return token;
	}

	/**
	 * 根据集团ID,指标ID/策略ID，查看阈值指标数据
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, String>> queryTargetThreshold(Map<String, Object> param) throws Exception {
		List<Map> targetThresholdModelList = new ArrayList<>();
		String prefixUrl = ZQ_PROTRAIT_URL;
		String groupId = (String) param.get("groupId");
		String targetId = (String) param.get("targetId");
		User user = (User) param.get("user");

		String token = getCocToken(user.getId(), user.getPwd());
		//http://{IP}:{PORT}/loc/api/portrait/external/queryListByScenes?keyId=13587331265&sceneCodes=f3239b5b43c642b8,f3239b5b43c642b6&keyType=1&provinceCode=bj
		String url = prefixUrl + "/loc/api/external/tactic/kpi/result/queryList";
		logger.info("调用coc获取阈值指标数据查看接口cepWebUrl=" + url);

		//组装post参数
		JSONObject jsonObject = new JSONObject();
		JSONObject busiparam = new JSONObject();
		busiparam.put("groupId", groupId);
		busiparam.put("tacticId", targetId);
		jsonObject.put("busiparam", busiparam);
		jsonObject.put("appKey", "1");
		String jsonData = JSONObject.toJSONString(jsonObject);

		String resultCopyRule = RestFullUtil.getInstance().sendPostRequest(url, jsonData, token);
//        String resultCopyRule = "{\"data\":[{\"ruleId\":\"规则id\",\"ruleStr\":\"<30%\",\"labelValue\":\"<40%\",\"isTrigger\":0,\"labelId\":\"标签ID\",\"labelName\":\"5G渗透率\",\"currentLabelValue\":\"<50%\"},{\"ruleId\":\"规则id\",\"ruleStr\":\"<10\",\"labelValue\":\"5\",\"isTrigger\":0,\"labelId\":\"标签ID\",\"labelName\":\"集团成员数\",\"currentLabelValue\":\"30\"}],\"status\":\"200\",\"msg\":\"查询成功\",\"exception\":null}";
		logger.info("调用coc获取阈值指标数据查看结果：" + resultCopyRule);
		JSONObject resultJson = JSONObject.parseObject(resultCopyRule);
		String status = resultJson.getString("status");
		if ("200".equals(status)) {
			String data = resultJson.getString("data");
			targetThresholdModelList = JSON.parseArray(data, Map.class);
		}
		return targetThresholdDataConvert(targetThresholdModelList);
	}

	/**
	 * 阈值指标数据
	 *
	 * @param targetThresholdModelList
	 * @return
	 */
	public List<Map<String, String>> targetThresholdDataConvert(List<Map> targetThresholdModelList) {
		List<Map<String, String>> result = new ArrayList<>();
		for (Map targetThresholdModel : targetThresholdModelList) {
			Map<String, String> map = new HashMap<>();
			String labelName = (String) targetThresholdModel.get("labelName");
			String ruleStr = (String) targetThresholdModel.get("ruleStr");
			String labelValue = (String) targetThresholdModel.get("labelValue");
			String currentLabelValue = (String) targetThresholdModel.get("currentLabelValue");
			map.put("labelName", labelName);
			map.put("configThreshold", ruleStr);
			map.put("triggerThreshold", labelValue);
			map.put("currentThreshold", currentLabelValue);
			result.add(map);
		}
		return result;
	}

}

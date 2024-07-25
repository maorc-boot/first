package com.asiainfo.biapp.pec.approve.jx.model;

public class OutboundCallResult {
	private String projectName;//项目名称
	private String key;//接入密码
	private String targets;//办理人
	private String targetName;//办理人
	private String processor;//上一个环节的处理人, 如果只生成待办不为处理人生成已办，那么发送人可不填写
	private String creator;//流程工单拟稿人
	private String opinion;//流程处理意见
	private String title;//工单标题
	private String unid;//应用系统的待办唯一ID
	private String additionalUnid;//上级流程实例（工单）的唯一标识，如果有多层，可以用逗号分隔，主流程在第一位
	private String feature;//流程实例（工单）唯一标识
	private String reserve;//决策名称：送办理|归档|送审批等等
	private String composeTime;//数据生成时间：2011-08-30 12:00:00
	private String process;//直接写流程名称
	private String fileType;//文档类型，直接写流程名称
	private String activity;//当前环节名称
	private boolean newly;//是否是草稿


	private String category;//分类名称，直接写流程名称
	private String urgency;//紧急程度：请默认“一般”
	private String secrecy;//密级：请默认“普通文件”
	private String serial;//文号：对公文必须，可以为空
	private int expirationMinutes;//不要求，固定为0
	private String expirationTime;//不要求，固定为""
	private boolean hastenEnable;//不要求，固定为false
	private int hastenMinutes;//不要求，固定为0
	private String hastenTime;//不要求，催办时间，可以为空
	private boolean remindEnable;//不要求，固定为false
	private boolean remindForce;//不要求，固定为false
	private String link;//访问链接
	private String linkView;//配合流程监控平台，江西移动环境接入为拟稿时间
	private String linkFlow;//办理链接, 可以为空
	private String linkRemove;//删除链接, 可以为空
	private String linkSupervise;//督办链接, 可以为空
	private String linkSuspend;//挂起链接, 可以为空
	private String linkReset;//重置链接, 可以为空
	private String linkReroute;//调度链接, 可以为空
	private String msgType;//消息类型
	private String deptCode;
	private String comCode;
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getTargets() {
		return targets;
	}
	
	public void setTargets(String targets) {
		this.targets = targets;
	}
	
	public String getProcessor() {
		return processor;
	}
	
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getOpinion() {
		return opinion;
	}
	
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUnid() {
		return unid;
	}
	
	public void setUnid(String unid) {
		this.unid = unid;
	}
	
	public String getAdditionalUnid() {
		return additionalUnid;
	}
	
	public void setAdditionalUnid(String additionalUnid) {
		this.additionalUnid = additionalUnid;
	}
	
	public String getFeature() {
		return feature;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public String getReserve() {
		return reserve;
	}
	
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	
	public String getComposeTime() {
		return composeTime;
	}
	
	public void setComposeTime(String composeTime) {
		this.composeTime = composeTime;
	}
	
	public String getProcess() {
		return process;
	}
	
	public void setProcess(String process) {
		this.process = process;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getActivity() {
		return activity;
	}
	
	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	public boolean getNewly() {
		return newly;
	}
	
	public void setNewly(boolean newly) {
		this.newly = newly;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getUrgency() {
		return urgency;
	}
	
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
	public String getSecrecy() {
		return secrecy;
	}
	
	public void setSecrecy(String secrecy) {
		this.secrecy = secrecy;
	}
	
	public String getSerial() {
		return serial;
	}
	
	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	public int getExpirationMinutes() {
		return expirationMinutes;
	}
	
	public void setExpirationMinutes(int expirationMinutes) {
		this.expirationMinutes = expirationMinutes;
	}
	
	public String getExpirationTime() {
		return expirationTime;
	}
	
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public boolean getHastenEnable() {
		return hastenEnable;
	}
	
	public void setHastenEnable(boolean hastenEnable) {
		this.hastenEnable = hastenEnable;
	}
	
	public int getHastenMinutes() {
		return hastenMinutes;
	}
	
	public void setHastenMinutes(int hastenMinutes) {
		this.hastenMinutes = hastenMinutes;
	}
	
	public String getHastenTime() {
		return hastenTime;
	}
	
	public void setHastenTime(String hastenTime) {
		this.hastenTime = hastenTime;
	}
	
	public boolean getRemindEnable() {
		return remindEnable;
	}
	
	public void setRemindEnable(boolean remindEnable) {
		this.remindEnable = remindEnable;
	}
	
	public boolean getRemindForce() {
		return remindForce;
	}
	
	public void setRemindForce(boolean remindForce) {
		this.remindForce = remindForce;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getLinkView() {
		return linkView;
	}
	
	public void setLinkView(String linkView) {
		this.linkView = linkView;
	}
	
	public String getLinkFlow() {
		return linkFlow;
	}
	
	public void setLinkFlow(String linkFlow) {
		this.linkFlow = linkFlow;
	}
	
	public String getLinkRemove() {
		return linkRemove;
	}
	
	public void setLinkRemove(String linkRemove) {
		this.linkRemove = linkRemove;
	}
	
	public String getLinkSupervise() {
		return linkSupervise;
	}
	
	public void setLinkSupervise(String linkSupervise) {
		this.linkSupervise = linkSupervise;
	}
	
	public String getLinkSuspend() {
		return linkSuspend;
	}
	
	public void setLinkSuspend(String linkSuspend) {
		this.linkSuspend = linkSuspend;
	}
	
	public String getLinkReset() {
		return linkReset;
	}
	
	public void setLinkReset(String linkReset) {
		this.linkReset = linkReset;
	}
	
	public String getLinkReroute() {
		return linkReroute;
	}
	
	public void setLinkReroute(String linkReroute) {
		this.linkReroute = linkReroute;
	}
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
}

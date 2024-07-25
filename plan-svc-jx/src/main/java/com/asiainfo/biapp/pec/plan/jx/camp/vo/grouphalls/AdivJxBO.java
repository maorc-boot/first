package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdivJxBO implements Serializable{


	private static final long serialVersionUID = 1L;

	private String channelId;//渠道id
	private String contactId;//触点id
	private String adivId;//运营位id
	private String adivName;//运营位名称
	private String adivType;//运营位类型 1：标准活动；2：产品类活动；3：文本类活动【无图片】；8：手厅首页轮播图活动【有背景图】；   IOP规范：1：横幅 3：弹窗 4：全屏 5：广告条  7：文字链广告  8：图文广告。
	private String adivSize;//运营位图片长宽
	private String adivSize2;//运营位图片2【背景图】长宽
	private String adivPicSize;//运营位图片大小上限
	private String adivPic2Size;//运营位图片2大小上限
	// 活动名称字数上限
	private String activityNameLimit ;
	// 活动描述字数上限
	private String activityDescLimit ;


	
   

}

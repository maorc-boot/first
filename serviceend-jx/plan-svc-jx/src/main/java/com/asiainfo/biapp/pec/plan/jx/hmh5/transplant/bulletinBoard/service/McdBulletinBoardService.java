package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.ModifyBulletinParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.PublishBulletinParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.SelectBulletinBoardParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.model.McdBulletinBoard;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 江西客户通公告栏表 服务类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
public interface McdBulletinBoardService extends IService<McdBulletinBoard> {

    Page selectAllBulletins(SelectBulletinBoardParam bulletinParam);

    List<Map<String,String>> uploadBulletinPictures(String userId, MultipartFile[] pictureFiles) throws Exception;

    void publishBulletin(UserSimpleInfo user, PublishBulletinParam publishParam);

    void downloadImage(String pictureName, HttpServletResponse response);

    void deleteImage(String userId, String bulletinCode, List<String> pictures);

    void modifyBulletin(String userId, ModifyBulletinParam modifyParam);

    void deleteBulletin(String userId, String bulletinCode);

    void modifyBulletinStatus(String userId, String bulletinCode);
}

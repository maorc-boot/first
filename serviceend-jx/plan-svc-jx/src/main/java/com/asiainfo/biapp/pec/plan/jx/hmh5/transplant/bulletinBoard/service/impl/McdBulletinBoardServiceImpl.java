package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.ModifyBulletinParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.PublishBulletinParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.controller.reqParam.SelectBulletinBoardParam;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.mapper.McdBulletinBoardMapper;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.model.McdBulletinBoard;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.service.McdBulletinBoardService;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.bulletinBoard.service.impl.resultInfo.SelectBulletinBoardResultInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.utils.CustomPictureStorageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Stream;

/**
 * <p>
 * 江西客户通公告栏表 服务实现类
 * </p>
 *
 * @author chenlin
 * @since 2023-05-28
 */
@Service
public class McdBulletinBoardServiceImpl extends ServiceImpl<McdBulletinBoardMapper, McdBulletinBoard> implements McdBulletinBoardService {
    @Autowired
    private CustomPictureStorageUtil pictureStorageUtil;

    private final String LAST_FOLDER = "bulletin";

    private final String UPLOAD_OF = "_uploadOf_";

    @Override
    public Page selectAllBulletins(SelectBulletinBoardParam bulletinParam) {
        Page bulletins = baseMapper.selectAllBulletins(
                new Page<>(bulletinParam.getPageNum(), bulletinParam.getPageSize()),
                bulletinParam
        );
        return bulletins.setRecords(BeanUtil.copyToList(bulletins.getRecords(), SelectBulletinBoardResultInfo.class));
    }

    @Override
    public List<Map<String, String>> uploadBulletinPictures(String userId, MultipartFile[] pictureFiles) throws Exception {
        ArrayList<Map<String, String>> pictureNames = new ArrayList<>();
        for (int i = 0; i < pictureFiles.length; i++) {
            //包含有上传失败的图片，前端根据status来判断
            pictureNames.add(pictureStorageUtil.uploadImage(LAST_FOLDER, UPLOAD_OF + userId, pictureFiles[i]));
        }
        return pictureNames;
    }

    @Override
    public void publishBulletin(UserSimpleInfo user, PublishBulletinParam publishParam) {
        McdBulletinBoard bulletin = BeanUtil.copyProperties(publishParam, McdBulletinBoard.class);
        //todo 需要根据情况修改workerId等
        bulletin.setBulletinCode(new Snowflake(1, 1).nextIdStr());
        bulletin.setCreateUserid(user.getUserId());
        bulletin.setCreateTime(new Date());
        bulletin.setStatus(1);//默认已下线（什么道理？？？）
        baseMapper.insert(bulletin);
    }

    @Override
    public void downloadImage(String pictureName, HttpServletResponse response) {
        pictureStorageUtil.downloadImage(LAST_FOLDER, pictureName, response);
    }

    @SneakyThrows
    @Override
    public void deleteImage(String userId, String bulletinCode, List<String> pictures) {
        //存储删除失败的文件，也就是不存在的文件
        ArrayList<String> deleteFaildPictures = new ArrayList<>();

        if (StringUtils.isEmpty(bulletinCode)) {   //bulletinCode为空时，说明是删除临时文件，也就是图片还没有与公告关联，就直接删除
            for (int i = 0; i < pictures.size(); i++) {
                String picture = pictures.get(i);
                if (picture.matches(".+" + UPLOAD_OF + userId + "\\..{3,}")) {
                    Map<String, String> result = pictureStorageUtil.deleteImage(LAST_FOLDER, picture);
                    if ("0".equals(result.get(CustomPictureStorageUtil.STATUS))) {
                        deleteFaildPictures.add(result.get(CustomPictureStorageUtil.ERROR));
                    }
                } else {
                    deleteFaildPictures.add("你无权删除照片：{" + picture + "}，因为不是你上传的！");
                }
            }
        } else { //如果是从已发布的公告中删除图片，需要修改数据库
            McdBulletinBoard bulletinBoard = baseMapper.selectById(bulletinCode);
            if (bulletinBoard == null) throw new BaseException("不存在此公告编号！");

            if (!bulletinBoard.getCreateUserid().equals(userId)) throw new BaseException("你无权删除此公告的图片！");

            String bulletinImage = bulletinBoard.getBulletinImage();
            if (bulletinImage == null) throw new BaseException("此公告没有上传相应图片！");

            List<String> uploadImages = new ArrayList<>(Arrays.asList(bulletinImage.split(",")));
            if (!uploadImages.containsAll(pictures)) throw new BaseException("此公告中不包含相应的图片！");

            //先删除服务器的图片
            for (int i = 0; i < pictures.size(); i++) {
                Map<String, String> result = pictureStorageUtil.deleteImage(LAST_FOLDER, pictures.get(i));
                if ("0".equals(result.get(CustomPictureStorageUtil.STATUS))) {
                    deleteFaildPictures.add(result.get(CustomPictureStorageUtil.ERROR));
                }
            }
            if (deleteFaildPictures.size() == 0) {
                //删除文件过程中没有出错的话，再更改数据库中的图片字段内容
                uploadImages.removeAll(pictures);
                bulletinBoard.setBulletinImage(uploadImages.size() == 0 ? null : String.join(",", uploadImages));
                baseMapper.updateById(bulletinBoard);
            }
        }
        if (deleteFaildPictures.size() > 0) throw new BaseException(deleteFaildPictures.toString());
    }

    @SneakyThrows
    @Override
    public void modifyBulletin(String userId, ModifyBulletinParam modifyParam) {
        McdBulletinBoard bulletin = baseMapper.selectById(modifyParam.getBulletinCode());
        if (!bulletin.getCreateUserid().equals(userId))
            throw new BaseException("你无权修改当前公告！");

        BeanUtil.copyProperties(modifyParam, bulletin);
        bulletin.setStatus(1);  //修改了公告应当是下线的状态

        baseMapper.updateById(bulletin);
    }

    @SneakyThrows
    @Override
    public void deleteBulletin(String userId, String bulletinCode) {
        McdBulletinBoard bulletin = baseMapper.selectById(bulletinCode);
        if (!bulletin.getCreateUserid().equals(userId))
            throw new BaseException("你无权删除当前公告！");

        //删除该公告涉及的图片
        String bulletinImages = bulletin.getBulletinImage();
        if (bulletinImages != null) {
            Stream.of(bulletinImages.split(",")).forEach(o -> pictureStorageUtil.deleteImage(LAST_FOLDER, o));
        }
        baseMapper.deleteById(bulletinCode);
    }

    @SneakyThrows
    @Override
    public void modifyBulletinStatus(String userId, String bulletinCode) {
        McdBulletinBoard bulletin = baseMapper.selectById(bulletinCode);
        if (!bulletin.getCreateUserid().equals(userId))
            throw new BaseException("你无权更改此公告的状态！");
        bulletin.setStatus((bulletin.getStatus() + 1) % 2);
        baseMapper.updateById(bulletin);
    }
}

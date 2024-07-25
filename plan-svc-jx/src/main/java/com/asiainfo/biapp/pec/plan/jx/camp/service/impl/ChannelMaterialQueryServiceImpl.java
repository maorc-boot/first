package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.asiainfo.biapp.pec.core.enums.CampStatus;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.plan.jx.camp.dao.IChannelMaterialQueryDao;
import com.asiainfo.biapp.pec.plan.jx.camp.model.ChannelMaterialModel;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ChannelMaterialListQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ChannelSelectMaterialListQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.req.DimMaterialPreviewRequest;
import com.asiainfo.biapp.pec.plan.jx.camp.req.McdCommunicationQuery;
import com.asiainfo.biapp.pec.plan.jx.camp.service.IChannelMaterialQueryService;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.McdCommunicationUsers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.util.URLUtil.decode;

@Slf4j
@Service
public class ChannelMaterialQueryServiceImpl extends ServiceImpl<IChannelMaterialQueryDao, ChannelMaterialModel> implements IChannelMaterialQueryService {


    public static final String LOCAL_PATH = "MCD_MATERIAL_LOCAL_PATH";
    public static final String SFTP_IP = "MCD_MATERIAL_SFTP_IP";
    public static final String SFTP_PASSWORD = "MCD_MATERIAL_SFTP_PASSWORD";
    public static final String SFTP_PATH = "MCD_MATERIAL_SFTP_PATH";
    public static final String SFTP_PORT = "MCD_MATERIAL_SFTP_PORT";
    public static final String SFTP_USERNAME = "MCD_MATERIAL_SFTP_USERNAME";

    @Resource
    private IChannelMaterialQueryDao channelMaterialQueryDao;

    @Autowired
    private IChannelMaterialQueryDao materialQueryDao;


    private static final Map<String,String> imageContentType = new HashMap<>();
    static {
        imageContentType.put("jpg", "image/jpeg");

        imageContentType.put("jpeg","image/jpeg");

        imageContentType.put("png","image/png");

        imageContentType.put("tif", "image/tiff");

        imageContentType.put("tiff","image/tiff");

        imageContentType.put("ico","image/x-icon");

        imageContentType.put("bmp","image/bmp");

        imageContentType.put("gif","image/gif");

        imageContentType.put("mp3","audio/mpeg");

        imageContentType.put("avi","video/x-msvideo");

        imageContentType.put("mp4","video/mp4");
    }

    @Override
    public IPage<ChannelMaterialModel> queryChannelMaterialList(ChannelMaterialListQuery req) {
        Page page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        //状态2审批通过的  类型为1 文字
        LambdaQueryWrapper<ChannelMaterialModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(req.getChannelId()),ChannelMaterialModel::getChannelId,req.getChannelId())
                     .eq(StringUtils.isNotEmpty(req.getCreator()),ChannelMaterialModel::getCreator,req.getCreator())
                     .eq(StringUtils.isNotEmpty(req.getPlanId()),ChannelMaterialModel::getPlanId,req.getPlanId())
                     .eq(StringUtils.isNotEmpty(req.getPositionId()),ChannelMaterialModel::getPositionId,req.getPositionId())
                     .eq(ChannelMaterialModel::getMaterialStatus,"2")
                     .eq(ChannelMaterialModel::getMaterialType,"1")
                     .like(StringUtils.isNotEmpty(req.getMaterialName()),ChannelMaterialModel::getMaterialName,req.getMaterialName());
       return channelMaterialQueryDao.selectPage(page,queryWrapper);

    }

    @Override
    public List<ChannelMaterialModel> getSelectMaterialList(ChannelSelectMaterialListQuery req) {
        //状态2审批通过的  类型为0 图片
        LambdaQueryWrapper<ChannelMaterialModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(req.getChannelId()),ChannelMaterialModel::getChannelId,req.getChannelId())
                .eq(StringUtils.isNotEmpty(req.getPositionId()),ChannelMaterialModel::getPositionId,req.getPositionId())
                .eq(ChannelMaterialModel::getMaterialStatus,"2")
                .eq(ChannelMaterialModel::getMaterialType,"0");
        List<ChannelMaterialModel> channelMaterialModels = channelMaterialQueryDao.selectList(queryWrapper);
        log.info("getSelectMaterialList-->查询满足条件的素材集合={}", JSONUtil.toJsonStr(channelMaterialModels));
        if (StrUtil.equals("968", req.getChannelId())) { // 广点通968，需要判断此素材的审批状态(仅查询草稿、预演完成状态的素材信息)
            // 查询出非草稿、预演完成状态素材
            List<ChannelMaterialModel> channelMaterialModelList = channelMaterialQueryDao.queryMaterialRelChannelList(req.getChannelId());
            log.info("getSelectMaterialList-->968渠道查询需要过滤的素材集合={}", JSONUtil.toJsonStr(channelMaterialModelList));
            List<ChannelMaterialModel> channelMaterialModelListNew = new ArrayList<>();
            // 需要特殊判断驳回状态的 区分是iop的审批流驳回还是广点通审核驳回
            for (ChannelMaterialModel channelMaterialModel : channelMaterialModelList) {
                if (CampStatus.APPROVE_BACK.getId().equals(channelMaterialModel.getMaterialStatus())) {
                    Map<String, Object> fallbackStat = materialQueryDao.chkMaterialFallbackStat(channelMaterialModel.getMaterialId());
                    if (Integer.parseInt(String.valueOf(fallbackStat.get("count"))) == 0) { // =0表示非广点通素材审核驳回，素材可以重复使用 >0表示广点通素材审核驳回，素材不可以重复使用
                        channelMaterialModelListNew.add(channelMaterialModel);
                    }
                } else { // 没有审批驳回状态
                    channelMaterialModelListNew.add(channelMaterialModel);
                }
            }
            // 根据素材id 排除掉不符合的(非草稿、预演完成状态)素材
            return channelMaterialModels.stream()
                    .filter(m -> !channelMaterialModelListNew.stream().map(ChannelMaterialModel::getMaterialId).collect(Collectors.toList()).contains(m.getMaterialId()))
                    .collect(Collectors.toList());
        } else { // 其他渠道正常逻辑
            return channelMaterialModels;
        }
    }


    /**
     * 根据素材 ID 查询所有素材信息
     *
     * @param materialId
     * @return
     */
    @Override
    public List<ChannelMaterialModel> queryMaterialById(String materialId) {
        LambdaQueryWrapper<ChannelMaterialModel> wrapper = Wrappers.<ChannelMaterialModel>query()
                .lambda().eq(ChannelMaterialModel::getMaterialId, materialId);
        return channelMaterialQueryDao.selectList(wrapper);
    }


    /**
     * 图片 视频预览
     *
     * @param resourceUrl
     * @param dimMaterialRequest
     */
    @Override
    public void loadImage(String resourceUrl, DimMaterialPreviewRequest dimMaterialRequest, HttpServletResponse response) {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            if(org.apache.commons.lang.StringUtils.isNotBlank(resourceUrl)){
                if(resourceUrl.lastIndexOf(".") == -1){
                    resourceUrl = decode(resourceUrl);
                }
            }

            File file = new File(resourceUrl);
            log.info("文件 ====> {}",file);
            if (!file.exists()) {
                this.downFile(dimMaterialRequest, resourceUrl);
            }

            // 获取图片存放路径
            ips = new FileInputStream(file);
            String fileType = resourceUrl.substring(resourceUrl.lastIndexOf(".") + 1).toLowerCase();

            if ("png".equalsIgnoreCase(fileType) ||
                    "jpg".equalsIgnoreCase(fileType) ||
                    "jpeg".equalsIgnoreCase(fileType)) {
                response.setContentType(imageContentType.get(fileType));
                response.setHeader("Content-disposition", "inline;");
            } else if ("mp3".equalsIgnoreCase(fileType) ||
                    "mp4".equalsIgnoreCase(fileType) ||
                    "avi".equalsIgnoreCase(fileType)) {
                response.setContentType(imageContentType.get(fileType));
                response.setHeader("Accept-Ranges", "bytes");
            }
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("预览失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
            if (ips != null){
                try {
                    ips.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
    }


    /**
     *  预览文件下载到本地服务器
     * @param dimMaterialRequest
     * @param resourceUrl
     */
    private void downFile(DimMaterialPreviewRequest dimMaterialRequest, String resourceUrl) {
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            String sftpServerIp = RedisUtils.getDicValue(SFTP_IP);
            int sftpServerPort =
                    Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
            String sftpUserName = RedisUtils.getDicValue(SFTP_USERNAME);
            String sftpUserPwd = RedisUtils.getDicValue(SFTP_PASSWORD);
            String sftpPath = RedisUtils.getDicValue(SFTP_PATH);
            String sftpLocalPath =RedisUtils.getDicValue(LOCAL_PATH);

            String fileName = resourceUrl.split("/")[resourceUrl.split("/").length - 1];

            log.info("1.素材id是：" +dimMaterialRequest.getMaterialId());
            sftp = sftpUtils.connect(sftpServerIp, sftpServerPort, sftpUserName, sftpUserPwd);
            log.info( "2.sftp登录成功！ip=" + sftpServerIp + ";port=" + sftpServerPort+
                            ";userName="+ sftpUserName + ",sftpPath："+ sftpPath+
                            ",sftpLocalPath:" + sftpLocalPath);
            sftpUtils.download(sftpPath, fileName, sftpLocalPath, sftp);
            log.info("3.下载成功！fileName=" + fileName);
        } catch (Exception e) {
            log.error("下载素材到本地目录出错：" + e);
        }finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常", e);
                }
            }
        }
    }


    @Override
    public IPage<McdCommunicationUsers> queryCommunicationUsers(McdCommunicationQuery pageQuery) {
        Page page = new Page();
        page.setSize(pageQuery.getSize());
        page.setCurrent(pageQuery.getCurrent());
        return channelMaterialQueryDao.queryCommunicationUsers(page,pageQuery);
    }
}

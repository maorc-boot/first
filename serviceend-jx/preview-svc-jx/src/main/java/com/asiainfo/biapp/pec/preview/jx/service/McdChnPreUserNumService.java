package com.asiainfo.biapp.pec.preview.jx.service;

import com.asiainfo.biapp.pec.preview.jx.entity.McdChnPreUserNum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户群渠道偏好用户数统计表 服务类
 * </p>
 *
 * @author mamp
 * @since 2022-09-30
 */
public interface McdChnPreUserNumService extends IService<McdChnPreUserNum> {

    /**
     * 根据客户群ID查询渠道偏好数据
     *
     * @param custgroupId 客户群ID
     * @param preLevel    偏好级别（1：第一偏好，2：第二偏好）
     * @return
     */
    List<McdChnPreUserNum> queryPreData(String custgroupId, Integer preLevel);
}

package com.asiainfo.biapp.pec.approve.jx.service.impl;


import com.asiainfo.biapp.pec.approve.jx.dao.McdDimMaterialJxDao;
import com.asiainfo.biapp.pec.approve.jx.model.McdDimMaterialJxModel;
import com.asiainfo.biapp.pec.approve.jx.service.IMcdDimMaterialJxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 营销素材 服务实现类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-13
 */
@Service
@Slf4j
@RefreshScope
public class McdDimMaterialJxServiceImpl extends ServiceImpl<McdDimMaterialJxDao, McdDimMaterialJxModel> implements IMcdDimMaterialJxService {

}

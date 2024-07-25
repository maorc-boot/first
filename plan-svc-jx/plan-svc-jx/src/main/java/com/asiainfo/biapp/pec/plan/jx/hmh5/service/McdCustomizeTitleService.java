package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCustTitleList;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCustomTitleQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface McdCustomizeTitleService extends IService<McdCustTitleList> {

    Page<McdCustTitleList> getCustomizeTitleList(McdCustomTitleQuery query);

    /**
     * 个性化称谓批量导入
     *
     * @param file 需要导入的文件数据
     * @param user 当前登录用户信息
     */
    Map<String,String> batchImportCustomizeTitleData(MultipartFile file, UserSimpleInfo user);

    void delCustomTitleById(String phoneNum);



}

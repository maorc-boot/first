package com.asiainfo.biapp.pec.plan.jx.hmh5.service;

import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdFrontBlacklistCust;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdFrontBlackListQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

public interface McdFrontBlackListService extends IService<McdFrontBlacklistCust> {

    Page<McdFrontBlacklistCust> queryMcdFrontBlackList(McdFrontBlackListQuery req);

    /**
     * 导入删除模板下载
     *
     * @param response
     */
    void downloadImportOrDelBlackListTemplate(HttpServletResponse response);


    /**
     * 黑名单导入
     *
     * @param impOrDelBlacklistFile 导入文件
     * @return
     */
    boolean impOrDelBlacklistFile(MultipartFile impOrDelBlacklistFile, String userId) throws Exception;

    /**
     * 黑名单导入(通过本地文件)
     *
     * @param file 导入文件
     * @return
     */
    boolean impOrDelBlacklistFile(File file, String userId) throws Exception;


}

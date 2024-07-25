package com.asiainfo.biapp.pec.plan.jx.camp.service;

import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.camp.model.McdCampImportTask;
import com.asiainfo.biapp.pec.plan.jx.camp.req.ImportTaskQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mamp
 * @since 2023-04-10
 */
public interface McdCampImportTaskService extends IService<McdCampImportTask> {

     /**
      * 通过上传的excel，生成活动
      * @param file
      * @param task
      * @return
      * @throws Exception
      */
     boolean loadCampFromFile(MultipartFile file, McdCampImportTask task, UserSimpleInfo user) throws Exception ;

     /**
      * 查询任务列表
      * @param query
      * @return
      */
     Page<McdCampImportTask>  queryTask( ImportTaskQuery query);

}

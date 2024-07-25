package com.asiainfo.biapp.cmn.jx.service;

import com.asiainfo.biapp.cmn.model.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-16
 */
public interface IDepartmentJxService extends IService<Department> {

    /**
     * 根据城市ID查询
     * @param cityId
     * @return
     */
    List<Department> selectByCityID(String cityId);
}

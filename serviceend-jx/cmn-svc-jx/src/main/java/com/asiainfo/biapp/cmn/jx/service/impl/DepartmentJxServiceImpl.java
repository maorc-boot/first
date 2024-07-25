package com.asiainfo.biapp.cmn.jx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.cmn.dao.DepartmentDao;
import com.asiainfo.biapp.cmn.jx.service.IDepartmentJxService;
import com.asiainfo.biapp.cmn.model.Department;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-16
 */
@Slf4j
@Service
public class DepartmentJxServiceImpl extends ServiceImpl<DepartmentDao, Department> implements IDepartmentJxService {

    @Resource
    private DepartmentDao departmentDao;


    @Value("${Department.List.Grade: }")
    private String DepartmentGrade;

    @Override
    public List<Department> selectByCityID(String cityId) {

        //调用dao
        List<Department> companies = departmentDao.selectList(Wrappers.<Department>lambdaQuery()
                .eq(Department::getDelFlag, 0)
                .eq(StringUtils.isNotEmpty(cityId),Department::getCityId, cityId)
                .in(StrUtil.isNotBlank(DepartmentGrade),Department::getGrade,DepartmentGrade.split(",")));
        if (companies == null || companies.size() == 0) {
            log.info("根据地市ID查询部门结果为空");
            return new ArrayList<>();
        }

        return companies;
    }
}

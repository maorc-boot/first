package com.asiainfo.biapp.cmn.jx.dao;

import com.asiainfo.biapp.cmn.vo.RoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mamp
 * @date 2022/12/13
 */
@Mapper
public interface RoleJxDao {
    /**
     * 查询 角色列表
     *
     * @param page
     * @param keyWords
     * @param deptId
     * @return
     */

    IPage<RoleVO> pageListRoleVo(IPage<RoleVO> page, @Param("keyWords") String keyWords, @Param("deptId") String deptId);
}

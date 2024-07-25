package com.asiainfo.biapp.pec.plan.jx.camp.dao;

import com.asiainfo.biapp.pec.plan.jx.camp.model.CustomerDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客群管理 Mapper 接口
 * </p>
 *
 * @author imcd
 * @since 2022-12-27
 */
@Mapper
public interface CustomerManageDao extends BaseMapper<CustomerDetail> {

    List<CustomerDetail> selectCustomerDetail();

    void updateCustStatus(@Param("params") List<String> customerDetailList);



}

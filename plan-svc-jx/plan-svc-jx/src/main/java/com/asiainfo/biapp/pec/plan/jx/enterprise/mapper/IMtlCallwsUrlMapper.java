package com.asiainfo.biapp.pec.plan.jx.enterprise.mapper;


import com.asiainfo.biapp.pec.plan.jx.enterprise.model.McdSysInterfaceDef;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMtlCallwsUrlMapper  {

    public List<McdSysInterfaceDef> findByCond(McdSysInterfaceDef mtlCallwsUrl) throws Exception;

}

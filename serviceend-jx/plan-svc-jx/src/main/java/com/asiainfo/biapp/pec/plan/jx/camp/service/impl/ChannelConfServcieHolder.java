package com.asiainfo.biapp.pec.plan.jx.camp.service.impl;

import com.asiainfo.biapp.pec.plan.jx.camp.service.ChannelConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mamp
 * @date 2023/4/11
 */
@Component
public class ChannelConfServcieHolder {
    private Map<String, ChannelConfService> serviceImplHolder =  new HashMap<>();
    @Autowired
    private void setServiceImplHolder(ChannelConfService[] serviceImpls){
        for(ChannelConfService seviceImpl : serviceImpls){
            serviceImplHolder.put(seviceImpl.getChannelId(), seviceImpl);
        }
    }

    public ChannelConfService getService(String channelId){
        return serviceImplHolder.get(channelId);
    }


}

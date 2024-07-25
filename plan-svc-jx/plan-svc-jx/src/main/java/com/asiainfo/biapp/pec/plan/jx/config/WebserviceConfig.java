package com.asiainfo.biapp.pec.plan.jx.config;

import com.asiainfo.biapp.pec.plan.jx.webservice.ICustgroupAPIServiceJx;
import com.asiainfo.biapp.pec.plan.jx.webservice.UserService;
import com.asiainfo.biapp.pec.plan.jx.webservice.Ws4AService;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * webservice接口配置
 *
 * @author mamp
 * @date 2022/6/28
 */
@Configuration
public class WebserviceConfig {

    @Autowired
    private ICustgroupAPIServiceJx custgroupAPIServiceJx;

    @Autowired
    private UserService userService;

    @Autowired
    private Ws4AService ws4AService;

    @Autowired
    private SpringBus springBus;

    @Bean
    public Endpoint endpointjx() {
        EndpointImpl endpoint = new EndpointImpl(springBus, custgroupAPIServiceJx);
        endpoint.publish("/SendCustInfoJx");
        return endpoint;
    }

    @Bean
    public Endpoint userServicendpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus, userService);
        endpoint.publish("/UserService");
        return endpoint;
    }


    @Bean
    public Endpoint UpdateAppAcctSoapServicendpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus, ws4AService);
        endpoint.publish("/UpdateAppAcctSoap");
        return endpoint;
    }

}

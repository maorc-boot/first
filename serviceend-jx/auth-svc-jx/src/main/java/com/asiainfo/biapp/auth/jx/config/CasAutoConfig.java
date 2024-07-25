package com.asiainfo.biapp.auth.jx.config;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * CAS spring-boot方式配置。
 * 和web.xml配置类似，只采用java代码方式进行配置。
 *
 * @author qxo
 */
@Configuration
@ConfigurationProperties("cas")
public class CasAutoConfig implements InitializingBean {

    /**
     * cas服务端URL casServerUrlPrefix
     */
    private String serverUrlPrefix;

    /**
     * 应用访问地址
     */
    private String serverName;

    /**
     * cas登录页面URL casServerLoginUrl
     */
    private String serverLoginUrl;

    /**
     * 要拦截的urlPatterns
     */
    private String filterUrlPatterns = "/login/emis1";

    /**
     * 配置不需要单点拦截的地址，可为空配合/*配置使用
     * ignorePattern
     */
    private String ignorePattern;

    /**
     * @see UrlPatternMatcherStrategy
     */
    private String ignoreUrlPatternType;

    private String validateServerUrlPrefix;

    /**
     * 应用实例地址，用于后台注销
     */
    private String clusterNodeUrls;

    @Override
    public void afterPropertiesSet() throws Exception {
        if ((serverLoginUrl == null || serverLoginUrl.isEmpty()) && serverUrlPrefix != null) {
            serverLoginUrl = serverUrlPrefix + "/login";
        }
        if (StringUtils.isEmpty(filterUrlPatterns)) {

            filterUrlPatterns = "/*";
        }
        if (validateServerUrlPrefix == null || validateServerUrlPrefix.isEmpty()) {
            validateServerUrlPrefix = serverUrlPrefix;
        }
    }


    private final int baseOrder = Integer.MIN_VALUE;

    /**
     * @return 配置登出过滤器(过滤器顺序有要求 ， 先登出 - 》 授权过滤器 - 》 配置过滤验证器 - 》 wraper过滤器)
     */
    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        final SingleSignOutFilter filter = new SingleSignOutFilter();
        final FilterRegistrationBean registration = create(filter);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", serverUrlPrefix);
        if (clusterNodeUrls != null) {
            initParameters.put("clusterNodeUrls", clusterNodeUrls);
        }
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(baseOrder + 1);
        return registration;
    }

    /**
     * @return 配置授权过滤器
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        final FilterRegistrationBean registration = create(new AuthenticationFilter());
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerLoginUrl", serverLoginUrl);
        initParameters.put("serverName", serverName);
        initParameters.put("casServerUrlPrefix", serverUrlPrefix);
        if (ignorePattern != null && !"".equals(ignorePattern)) {
            initParameters.put("ignorePattern", ignorePattern);
        }
        //自定义UrlPatternMatcherStrategy 验证规则
        if (ignoreUrlPatternType != null && !"".equals(ignoreUrlPatternType)) {
            initParameters.put("ignoreUrlPatternType", ignoreUrlPatternType);
        }
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(baseOrder + 2);
        return registration;
    }

    /**
     * @return 配置过滤验证器 这里用的是Cas30ProxyReceivingTicketValidationFilter
     */
    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        final FilterRegistrationBean registration = create(new Cas30ProxyReceivingTicketValidationFilter());
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", validateServerUrlPrefix);
        initParameters.put("serverName", serverName);
        initParameters.put("useSession", "true");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(baseOrder + 3);
        return registration;
    }

    /**
     * @return request wraper过滤器
     */
    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        final FilterRegistrationBean registration = create(new HttpServletRequestWrapperFilter());
        // 设定加载的顺序
        registration.setOrder(baseOrder + 4);
        return registration;
    }

    /**
     * @return AssertionThreadLocalFilter过滤器
     */
    @Bean
    public FilterRegistrationBean assertionThreadLocalRegistration() {
        final FilterRegistrationBean registration = create(new AssertionThreadLocalFilter());
        // 设定加载的顺序
        registration.setOrder(baseOrder + 5);
        return registration;
    }

    /**
     * @return 添加监听器
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }


    protected FilterRegistrationBean create(final Filter filter) {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        // 设定匹配的路径
        for (String urlPattern : filterUrlPatterns.split(",")) {
            registration.addUrlPatterns(urlPattern);
        }
        return registration;
    }

    public String getServerUrlPrefix() {
        return serverUrlPrefix;
    }

    public void setServerUrlPrefix(String serverUrlPrefix) {
        this.serverUrlPrefix = serverUrlPrefix;
    }

    public String getServerLoginUrl() {
        return serverLoginUrl;
    }

    public void setServerLoginUrl(String serverLoginUrl) {
        this.serverLoginUrl = serverLoginUrl;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String clientHostUrl) {
        this.serverName = clientHostUrl;
    }

    public String getIgnorePattern() {
        return ignorePattern;
    }

    public void setIgnorePattern(String ignorePattern) {
        this.ignorePattern = ignorePattern;
    }

    public String getIgnoreUrlPatternType() {
        return ignoreUrlPatternType;
    }

    public void setIgnoreUrlPatternType(String ignoreUrlPatternType) {
        this.ignoreUrlPatternType = ignoreUrlPatternType;
    }

    public String getValidateServerUrlPrefix() {
        return validateServerUrlPrefix;
    }

    public void setValidateServerUrlPrefix(String validateServerUrlPrefix) {
        this.validateServerUrlPrefix = validateServerUrlPrefix;
    }

    public String getClusterNodeUrls() {
        return clusterNodeUrls;
    }

    public void setClusterNodeUrls(String clusterNodeUrls) {
        this.clusterNodeUrls = clusterNodeUrls;
    }

}

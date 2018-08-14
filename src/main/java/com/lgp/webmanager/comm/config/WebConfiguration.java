package com.lgp.webmanager.comm.config;

import com.lgp.webmanager.comm.filter.SecurityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lgp.webmanager.util.ConstUtil.SECURITY_FILTER;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * 页面限制
 */
@Configuration
public class WebConfiguration {

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SecurityFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName(SECURITY_FILTER);
        registration.setOrder(1);
        return registration;
    }
}




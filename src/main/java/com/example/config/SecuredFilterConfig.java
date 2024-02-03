package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.Filter;


@Configuration
public class SecuredFilterConfig {
    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(tokenFilter);
        bean.addUrlPatterns("/profile/adm");
        bean.addUrlPatterns("/emailHistory/adm");
        bean.addUrlPatterns("/article/adm");
        bean.addUrlPatterns("/category/adm");
        bean.addUrlPatterns("/region/adm");

        bean.addUrlPatterns("/profile/adm/*");
        bean.addUrlPatterns("/emailHistory/adm/*");
        bean.addUrlPatterns("/article/adm/*");
        bean.addUrlPatterns("/category/adm/*");
        bean.addUrlPatterns("/region/adm/*");

        bean.addUrlPatterns("/profile/adm/**");
        bean.addUrlPatterns("/emailHistory/adm/**");
        bean.addUrlPatterns("/article/adm/**");
        bean.addUrlPatterns("/category/adm/**");
        bean.addUrlPatterns("/region/adm/**");

        return bean;
    }
}

package com.beyond.report.config;

import com.beyond.report.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 1. 【核心修复】静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 告诉 Spring，所有 /css/**, /jquery/**, /image/** 开头的请求，
        // 都去 classpath:/static/ 下面找对应的子文件夹。
        // 注意：不要写成 classpath:/static/css/ 给 /css/**，那样会造成路径错位。
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/jquery/**").addResourceLocations("classpath:/static/jquery/");
        registry.addResourceHandler("/image/**").addResourceLocations("classpath:/static/image/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/easyui/**").addResourceLocations("classpath:/static/easyui/");
    }

    // 2. 登录拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/",
                        "/Report/",
                        "/Report/LoginServlet",
                        "/Report/login",
                        "/LoginServlet",
                        "/Report/api/login",
                        "/api/login",
                        "/demo1",
                        "/css/**",   
                        "/jquery/**",
                        "/image/**",
                        "/js/**",
                        "/easyui/**", 
                        "/error"
                );
    }
}
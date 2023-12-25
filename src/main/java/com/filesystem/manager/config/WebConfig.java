package com.filesystem.manager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description MVC配置
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截规则
        registry.addInterceptor(loginInterceptor)
                // 添加 api/file/*所有接口和 / 首页到拦截器进行拦截
                .addPathPatterns("/api/file/*","/","/index")
                // 放行 api/user所有接口 和 登录页 以及 注册页面
                .excludePathPatterns("/api/user/*","/login","/register");
    }
}

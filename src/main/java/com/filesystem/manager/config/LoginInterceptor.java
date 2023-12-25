package com.filesystem.manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filesystem.manager.entity.SysUser;
import com.filesystem.manager.vo.ResponseVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 登录拦截器
 * @Author Efar <efarxs@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2023/12/8
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 前置拦截处理 即请求完成前处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否登录
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        if (null == user) {
            if (request.getRequestURI().startsWith("/api/")) {
                // API请求
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().println(new ObjectMapper().writeValueAsString(ResponseVo.fail(-403,"未登录")));
            } else {
                // 未登录 跳转到登录页
                response.sendRedirect("/login");
            }
            return false;
        }

        return true;
    }
}

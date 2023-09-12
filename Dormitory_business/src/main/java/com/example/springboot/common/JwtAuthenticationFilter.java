package com.example.springboot.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            unauthorized(response, "未授权，请先登录");
            return false;
        }

        String token = authorization.substring(7).trim();
        if (!jwtUtil.validateToken(token)) {
            unauthorized(response, "Token 无效或已过期");
            return false;
        }

        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));
        request.setAttribute("role", jwtUtil.getRoleFromToken(token));
        request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        request.setAttribute("displayName", jwtUtil.getDisplayNameFromToken(token));
        request.setAttribute("avatar", jwtUtil.getAvatarFromToken(token));
        return true;
    }

    private void unauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":\"401\",\"msg\":\"" + message + "\"}");
    }
}

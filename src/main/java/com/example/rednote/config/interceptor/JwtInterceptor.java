package com.example.rednote.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.rednote.common.utils.JwtUtils;
import com.example.rednote.common.utils.ThreadLocalUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            sendError(response, "缺少令牌");
            return false;
        }

        token = token.substring(7);
        if (!jwtUtils.validateToken(token)) {
            sendError(response, "令牌无效或已过期");
            return false;
        }

        try {
            Claims claims = jwtUtils.parseToken(token);

            ThreadLocalUtils.set("userId", claims.getSubject());
            ThreadLocalUtils.set("username", claims.get("username"));

            return true;
        } catch (Exception e) {
            sendError(response, "令牌解析失败");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) {
        // 清理ThreadLocal，防止内存泄漏
        ThreadLocalUtils.remove();
    }

    private void sendError(HttpServletResponse response, String message) throws IOException, java.io.IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
    }
}

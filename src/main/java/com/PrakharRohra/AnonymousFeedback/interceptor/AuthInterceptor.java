package com.PrakharRohra.AnonymousFeedback.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // Allow login and register endpoints without authentication
        if (requestURI.contains("/api/auth/login") || requestURI.contains("/api/auth/register")) {
            return true;
        }

        // Extract token from request header
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing or invalid token");
            return false;
        }

        // Validate the token (You need to implement JWT validation logic here)
        boolean isValidToken = validateToken(token.substring(7)); // Remove "Bearer " prefix
        if (!isValidToken) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
            return false;
        }

        return true;
    }

    private boolean validateToken(String token) {
        return true; // Replace this with actual token verification
    }
}

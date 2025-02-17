package com.PrakharRohra.AnonymousFeedback.interceptor;

import com.PrakharRohra.AnonymousFeedback.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return true; // Let OPTIONS requests pass through
        }

        String requestURI = request.getRequestURI();

        if (requestURI.contains("/api/auth/login") ||
                requestURI.contains("/api/auth/register") ||
                requestURI.contains("/api/auth/verify") ||
                requestURI.contains("/api/auth/resend-otp") ||
                requestURI.contains("/error") || requestURI.contains("/reject")) {
            return true;
        }

        // Extract token from request header
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing or invalid token");
            return false;
        }

        // Remove "Bearer " prefix
        token = token.substring(7);

        // Validate the token
        if (!jwtTokenProvider.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
            return false;
        }

        // Optionally, store the authenticated user's email in the request for further processing
        String email = jwtTokenProvider.getEmailFromToken(token);
        request.setAttribute("requesterEmail", email);

        return true;
    }
}

package com.example.springboot.common;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7).trim();
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        String userId = jwtUtil.getUserIdFromToken(token);
        String displayName = jwtUtil.getDisplayNameFromToken(token);
        String avatar = jwtUtil.getAvatarFromToken(token);

        String normalizedRole = role == null ? "USER" : role.toUpperCase();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + normalizedRole))
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        request.setAttribute("username", username);
        request.setAttribute("role", role);
        request.setAttribute("userId", userId);
        request.setAttribute("displayName", displayName);
        request.setAttribute("avatar", avatar);
        filterChain.doFilter(request, response);
    }
}

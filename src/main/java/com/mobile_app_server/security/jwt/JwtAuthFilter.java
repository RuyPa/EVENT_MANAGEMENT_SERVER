package com.mobile_app_server.security.jwt;

import com.mobile_app_server.service.impl.CustomUserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final String securityType = "Bearer ";

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsServiceImpl customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String path = request.getServletPath();

        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwtToken = null;

        if (authHeader != null && authHeader.startsWith(securityType)) {

            jwtToken = authHeader.substring(securityType.length());
            username = jwtUtil.extractUsername(jwtToken);

        }

        if (username != null && jwtToken != null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ===== Kiểm tra ngay =====
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    System.out.println("=== JWT Filter ===");
                    System.out.println("Username: " + auth.getName());
                    System.out.println("Authorities: " + auth.getAuthorities());
                    System.out.println("Authenticated: " + auth.isAuthenticated());
                } else {
                    System.out.println("=== JWT Filter === No authentication set!");
                }
            } else {
                System.out.println("=== JWT Filter === Token không hợp lệ cho user: " + username);
            }
        } else {
            System.out.println("=== JWT Filter === Không tìm thấy username hoặc jwtToken");
        }

        filterChain.doFilter(request, response);
    }
}

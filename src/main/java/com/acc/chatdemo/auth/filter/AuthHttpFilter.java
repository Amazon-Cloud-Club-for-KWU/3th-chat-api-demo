package com.acc.chatdemo.auth.filter;

import com.acc.chatdemo.auth.JwtProvider;
import com.acc.chatdemo.auth.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthHttpFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // /api/auth/** 경로는 필터링 제외
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            try {
                token = token.substring(7);
                System.out.println("Extracted token: " + token);
                Long userId = jwtProvider.parseUserIdFromAccessToken(token);
                System.out.println("Parsed userId: " + userId);
                
                UserDetails userDetails = userDetailService.loadUserByUsername(String.valueOf(userId));
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    System.out.println("Authentication set for user: " + userId);
                }
            } catch (Exception e) {
                System.err.println("JWT 토큰 처리 오류: " + e.getMessage());
                e.printStackTrace();
                // 토큰이 유효하지 않아도 필터 체인은 계속 진행 (Spring Security가 나중에 인증 실패 처리)
            }
        } else {
            System.out.println("No valid Authorization header found");
        }
        
        filterChain.doFilter(request, response);
    }
}

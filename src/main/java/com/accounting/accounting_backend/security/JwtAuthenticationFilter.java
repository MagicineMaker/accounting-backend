package com.accounting.accounting_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        System.out.println("=== JWT Filter Processing: " + request.getRequestURI() + " ===");

        String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + header);

        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
            System.out.println("Extracted token: " + token);
        }

        if (token != null && jwtUtils.validateToken(token)) {
            System.out.println("Token is valid");
            String username = jwtUtils.getUsernameFromToken(token);
            System.out.println("Username from token: " + username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails authorities: " + userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Authentication set in SecurityContext");

        } else {
            System.out.println("Token is invalid or missing");
        }

        filterChain.doFilter(request, response);
    }
}
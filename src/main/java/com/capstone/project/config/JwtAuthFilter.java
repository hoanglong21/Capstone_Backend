package com.capstone.project.config;

import com.capstone.project.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            Object check = requestMappingHandlerMapping.getHandler(request);
            if (check == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.getWriter().write("Requested URL does not exist: " + request.getRequestURL());
                response.getWriter().flush();
                return;
            }
            Object handler = requestMappingHandlerMapping.getHandler(request).getHandler();

            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                PreAuthorize preAuthorizeAnnotation = handlerMethod.getMethodAnnotation(PreAuthorize.class);
                if (preAuthorizeAnnotation != null && authHeader == null) {
                    throw new Exception("Not authorized");
                }

                if (authHeader != null && authHeader.startsWith("Bearer ") && preAuthorizeAnnotation != null) {
                    token = authHeader.substring(7);
                    if (token == null || token.equals("")) {
                        throw new Exception("Invalid JWT Token");
                    }
                    username = jwtService.extractUsername(token);
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new Exception("Invalidate token");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("You need to login first to continue");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
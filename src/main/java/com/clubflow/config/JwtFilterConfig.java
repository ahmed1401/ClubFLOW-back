package com.clubflow.config;

import com.clubflow.security.JwtTokenProvider;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilterConfig.class);

    private final JwtTokenProvider tokenProvider;

    @Bean
    public Filter jwtAuthenticationFilter() {
        logger.debug("Creating JWT authentication filter bean");
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                try {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    String jwt = tokenProvider.resolveToken(httpRequest);

                    if (jwt != null && tokenProvider.validateToken(jwt)) {
                        Authentication auth = tokenProvider.getAuthentication(jwt);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (Exception ex) {
                    logger.error("Could not set user authentication in security context", ex);
                }

                chain.doFilter(request, response);
            }
        };
    }
}
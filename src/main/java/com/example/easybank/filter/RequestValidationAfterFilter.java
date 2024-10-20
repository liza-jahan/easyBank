package com.example.easybank.filter;


import com.mysql.cj.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Slf4j
public class RequestValidationAfterFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(null!=authentication){
            log.info("User"+authentication.getName()+"is successfully authenticated and"+"has the authorities "+authentication.getAuthorities().toString());
        }
        filterChain.doFilter(request,response);
    }
}

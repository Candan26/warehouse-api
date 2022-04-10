package com.readingisgood.warehouseapi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor  implements HandlerInterceptor {

    @Autowired
    private   WarehouseJwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if(token !=null && token.startsWith("Bearer ")){
            token = token.substring(7);
        }
       if(token == null || token.equals("") || isTokenExpire(token)){
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           return false;
       }else {
           return true;
       }
    }

    private boolean isTokenExpire(String token) {
        return jwtUtil.validateToken(token);
    }
}

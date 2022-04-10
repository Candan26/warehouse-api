package com.readingisgood.warehouseapi.service.impl;

import com.readingisgood.warehouseapi.dto.AuthServiceDto;
import com.readingisgood.warehouseapi.service.AuthService;
import com.readingisgood.warehouseapi.util.WarehouseJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${token.jwt.expiration.time.second}")
    private long expirationTime;

    private  final WarehouseJwtUtil jwtUtil;

    @Override
    public AuthServiceDto getToken(AuthServiceDto request) {
        AuthServiceDto response  = new AuthServiceDto();
        response.setError(false);
        response.setToken(jwtUtil.generateToken(request.getSubject()));
        response.setIdleTime(expirationTime);
        return response;
    }
}

package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.AuthServiceDto;

public interface AuthService {
     AuthServiceDto getToken(AuthServiceDto request);
}

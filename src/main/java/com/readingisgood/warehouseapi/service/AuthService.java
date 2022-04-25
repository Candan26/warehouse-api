package com.readingisgood.warehouseapi.service;

import com.readingisgood.warehouseapi.dto.AuthServiceDto;
import com.readingisgood.warehouseapi.model.WarehouseResponse;

public interface AuthService {
     WarehouseResponse getToken(AuthServiceDto request);
}

package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.dto.AuthServiceDto;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.service.AuthService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> getAuth(@RequestBody AuthServiceDto request) {
        return new ResponseEntity<>(new WarehouseResponse(WarehouseUtil.SUCCEED, authService.getToken(request), null), HttpStatus.OK);
    }

}

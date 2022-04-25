package com.readingisgood.warehouseapi.controller;

import com.readingisgood.warehouseapi.dto.AuthServiceDto;
import com.readingisgood.warehouseapi.model.WarehouseResponse;
import com.readingisgood.warehouseapi.service.AuthService;
import com.readingisgood.warehouseapi.util.WarehouseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(value = "Controller for authentication jwt token")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully jwt token created."),
        @ApiResponse(code = 500, message = "If token service get exception.")})
public class AuthController {
    private final AuthService authService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> getAuth(@RequestBody AuthServiceDto request) {
        try {
            WarehouseResponse response = authService.getToken(request);
            if(response.getError()!=null){
                return new ResponseEntity<>(response, response.getError().getStatus());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception on ", ex);
            return new ResponseEntity<>("Service Error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

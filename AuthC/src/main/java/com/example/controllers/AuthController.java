package com.example.controllers;

import com.example.model.LoginRequest;
import com.example.service.JWTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints for authenticate the user")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    JWTService service;

    @Operation(summary = "Signin the user")
    @PostMapping("/signin")
    public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        return service.createJWT(loginRequest);
    }

}

package com.authcentral.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.authcentral.authservice.dto.LoginRequest;
import com.authcentral.authservice.dto.LoginResponse;
import com.authcentral.authservice.service.LoginService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/auth")
@Validated
public class UserAuthController {

    private final LoginService loginService;

    public UserAuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok(response);
    }
}

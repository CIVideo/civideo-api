package com.jjss.civideo.domain.auth.controller;

import com.nimbusds.oauth2.sdk.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/auth/token")
    public ResponseEntity<?> token() {
        Jwt.withTokenValue("tokenValue");
        return ResponseEntity.ok("");
    }

}

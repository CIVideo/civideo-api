package com.jjss.civideo.domain.user.controller;

import com.jjss.civideo.domain.user.dto.TokenRequestDto;
import com.jjss.civideo.domain.user.dto.TokenResponseDto;
import com.jjss.civideo.domain.user.service.UserService;
import com.jjss.civideo.global.exception.BadRequestResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/token")
    public ResponseEntity<?> sendToken(@Valid TokenRequestDto tokenRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(BadRequestResponseDto.of(errors));
        }
        String accessToken = userService.createAccessToken(tokenRequestDto);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "invalid token"));
        }

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.ok(tokenResponseDto);
    }

}
